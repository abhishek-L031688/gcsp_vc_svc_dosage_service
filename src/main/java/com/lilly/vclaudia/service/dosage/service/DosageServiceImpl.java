package com.lilly.vclaudia.service.dosage.service;

import static com.lilly.vclaudia.service.dosage.common.DosageConstants.DAYS_BETWEEN_FOUR_WEEKS_INJECTION;
import static com.lilly.vclaudia.service.dosage.common.DosageConstants.DAYS_BETWEEN_TWO_WEEKS_INJECTION;
import static com.lilly.vclaudia.service.dosage.common.DosageConstants.DOSAGE_SCHEDULE_REQUIRED_IN_YEARS;
import static com.lilly.vclaudia.service.dosage.common.DosageConstants.NOT_TAKEN_DOSAGE_STATUS;
import static com.lilly.vclaudia.service.dosage.common.DosageConstants.TOTAL_NO_OF_TWO_WEEKS_INJECTIONS;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilly.vclaudia.service.dosage.dao.DosageDao;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.model.Dosage;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.lilly.vclaudia.service.dosage.model.DosageProfileResponse;
import com.newrelic.agent.deps.com.google.gson.JsonElement;
import com.newrelic.agent.deps.com.google.gson.JsonIOException;
import com.newrelic.agent.deps.com.google.gson.JsonObject;
import com.newrelic.agent.deps.com.google.gson.JsonParser;
import com.newrelic.agent.deps.com.google.gson.JsonSyntaxException;

/**
 * Dosage service implementation.
 * 
 * @author cramaswamy
 *
 */
@Service
public class DosageServiceImpl implements DosageService {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DosageServiceImpl.class);
	
	private static final String appConfigJson = "app-config.json";

	@Inject
	private DosageDao dosageDao;

	@Inject
	private ObjectMapper objectMapper;
	
	private static JsonObject appConfigJsonObject;
	
	static{
		final JsonParser parser = new JsonParser();
        try {
			JsonElement jsonElement = (JsonElement)parser.parse(
					new FileReader(DosageServiceImpl.class.getClassLoader().getResource(appConfigJson).getFile()));
			appConfigJsonObject = jsonElement.getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			LOGGER.error("Error while readihng app-config.json", e);
		}
	}

	/**
	 * Saves dosage profile information for a particular patient and product.
	 * 
	 * @param patientId
	 * @param productId
	 * @param dosageProfileRequest
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public DosageProfileResponse saveDosage(String patientId, String productId,
			DosageProfileRequest dosageProfileRequest) throws ServiceException {
		try {
			// maps save dosage profile request to DosageProfile entity.
			DosageProfile dosageProfile = new DosageProfile();
			DosageProfileId dosageProfileId = new DosageProfileId(patientId, productId);
			dosageProfile.setDosageProfileId(dosageProfileId);
			dosageProfile.getDosageProfileId().setProductId(productId);
			dosageProfile.setReminderTime(dosageProfileRequest.getReminderTime());
			dosageProfile.setDosages(objectMapper.writeValueAsString(dosageProfileRequest.getDosages()));
			LOGGER.debug(String.format("Saving dosage profile request for patientId: %s and productId %s", patientId,
					productId));
			dosageDao.saveDosage(dosageProfile);
			final DosageProfileResponse dosageProfileResponse = new DosageProfileResponse();
			dosageProfileResponse.setRemiderTime(dosageProfileRequest.getReminderTime());
			dosageProfileResponse.setDosages(dosageProfileRequest.getDosages());
			// calculate next dosage due date.
			calculateNextDosageDueDateAndGenerateDosageSchedule(dosageProfileResponse);
			return dosageProfileResponse;
		} catch (Exception e) {
			throw new ServiceException("Error saving dosage profile information", e);
		}
	}

	/**
	 * Calculates patient next dosage due date and generates 
	 * dosage schedule from the last dosage taken date. 
	 * 
	 * @param dosageProfileResponse
	 */
	private void calculateNextDosageDueDateAndGenerateDosageSchedule(DosageProfileResponse dosageProfileResponse) {
		// sorts dosages taken by the patient on ascending order of dosage number.
		Collections.sort(dosageProfileResponse.getDosages(),
				(o1, o2) -> o1.getDosageNumber().compareTo(o2.getDosageNumber()));
		// reads last dosage in dosageProfileResponse.getDosages() which provides the last dosage taken details of patient.
		final Dosage lastDosageTakenDetails = dosageProfileResponse.getDosages()
				.get(dosageProfileResponse.getDosages().size() - 1);
		final Date lastDosageTakenDate = lastDosageTakenDetails.getDosageTakenDate();
		LOGGER.debug(String.format("The last dosage taken date of the patient is: %s", lastDosageTakenDate));
		dosageProfileResponse.setNextDosageDate(
				calculateNextDosageDueDate(lastDosageTakenDate, lastDosageTakenDetails.getDosageNumber()));

		// generates dosage schedule
		List<Dosage> scheduledDosageList = generateDosageSchedule(lastDosageTakenDetails, lastDosageTakenDate);
		dosageProfileResponse.getDosages().addAll(scheduledDosageList);
	}

	/**
	 * Generates one year of dosage schedule for patient from the last dosage taken date.
	 * 
	 * @param lastDosageTakenDetails
	 * @param lastDosageTakenDate
	 * @return scheduled one year of dosage details
	 */
	private List<Dosage> generateDosageSchedule(final Dosage lastDosageTakenDetails, final Date lastDosageTakenDate) {
		// adds one year to last dosage taken date to calculate dosage schedule for next one year.
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastDosageTakenDate);
		calendar.add(Calendar.YEAR, appConfigJsonObject.get(DOSAGE_SCHEDULE_REQUIRED_IN_YEARS).getAsInt());
		final Date dosageScheduleEndDate = calendar.getTime();
		Date nextScheduledDosageDate = lastDosageTakenDate;
		int dosageNumberCount = lastDosageTakenDetails.getDosageNumber();
		List<Dosage> scheduledDosageList = new ArrayList<>();
		while (!nextScheduledDosageDate.after(dosageScheduleEndDate)) {
			nextScheduledDosageDate = calculateNextDosageDueDate(nextScheduledDosageDate, dosageNumberCount);
			if(!nextScheduledDosageDate.after(dosageScheduleEndDate)){
				scheduledDosageList.add(new Dosage(++dosageNumberCount, 
						appConfigJsonObject.get(NOT_TAKEN_DOSAGE_STATUS).getAsString(), nextScheduledDosageDate));
			}
		}
		return scheduledDosageList;
	}

	/**
	 * Calculates next dosage due date for patient.
	 * If the patient has not taken 12 or more injections then the next dosage due date will be "last dosage date + 14 days". 
	 * If the patient has taken 12 or more injections then the next dosage due date will be "last dosage date + 28 days".
	 * 
	 * @param lastDosageDate
	 * @param lastDosageNumber
	 * @return next dosage date
	 */
	private Date calculateNextDosageDueDate(Date lastDosageDate, int lastDosageNumber) {
		final Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(lastDosageDate);
		if (lastDosageNumber < appConfigJsonObject.get(TOTAL_NO_OF_TWO_WEEKS_INJECTIONS).getAsInt()) {
			currentCalendar.add(Calendar.DATE, appConfigJsonObject.get(DAYS_BETWEEN_TWO_WEEKS_INJECTION).getAsInt());
			return currentCalendar.getTime();
		} else {
			currentCalendar.add(Calendar.DATE, appConfigJsonObject.get(DAYS_BETWEEN_FOUR_WEEKS_INJECTION).getAsInt());
			return currentCalendar.getTime();
		}
	}

	/**
	 * Retrieves dosage information for a given patientId and productId.
	 * 
	 * @param patientId
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public DosageProfileResponse getDosage(String patientId, String productId) throws ServiceException {
		try{
			LOGGER.debug(String.format("Retrieving dosage profile for patientId: %s and productId %s", patientId,
					productId));
			final DosageProfileId dosageProfileId = new DosageProfileId(patientId, productId);
			final DosageProfile dosageProfile = dosageDao.getDosage(dosageProfileId);
			final DosageProfileResponse dosageProfileResponse = new DosageProfileResponse();
			dosageProfileResponse.setRemiderTime(dosageProfile.getReminderTime());
			List<Dosage> dosageList = objectMapper.readValue(dosageProfile.getDosages(), new TypeReference<List<Dosage>>(){});
			dosageProfileResponse.setDosages(dosageList);
			// calculates next dosage due date and generates dosage schedule.
			calculateNextDosageDueDateAndGenerateDosageSchedule(dosageProfileResponse);
			return dosageProfileResponse;
		} catch (EntityNotFoundException e) {
			throw e;
		} catch(Exception e){
			throw new ServiceException("Error retrieving dosage profile information", e);
		}
	}

}
