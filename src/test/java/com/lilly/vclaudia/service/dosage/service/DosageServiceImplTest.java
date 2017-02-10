package com.lilly.vclaudia.service.dosage.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilly.vclaudia.service.dosage.common.TestCommon;
import com.lilly.vclaudia.service.dosage.dao.DosageDao;
import com.lilly.vclaudia.service.dosage.exception.DaoException;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.model.Dosage;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.lilly.vclaudia.service.dosage.model.DosageProfileResponse;

/**
 * Junit test suite for dosage service implementation.
 * 
 * @author cramaswamy
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DosageServiceImplTest extends TestCommon {

	@Mock
	private DosageDao dosageDao;

	@InjectMocks
	private DosageServiceImpl dosageServiceImpl;

	@Mock
	private ObjectMapper objectMapper;

	@Rule()
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		when(dosageDao.saveDosage(any(DosageProfile.class))).thenReturn(buildDosageProfile());
	}

	/**
	 * tests save dosage service method, where the dosage information is
	 * successfully saved. Asserts that the there is 14 days difference between
	 * last dosage taken date and next dosage due date as the patient is still
	 * in two weeks per injection phase.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success() throws Exception {
		// set up
		final DosageProfileRequest dosageProfileRequest = buildDosageProfileRequest();
		dosageProfileRequest.getDosages().get(0).setDosageTakenDate(new Date());
		when(objectMapper.writeValueAsString(dosageProfileRequest.getDosages()))
				.thenReturn(buildDosageListAsJsonString());

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.saveDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID,
				dosageProfileRequest);

		// assert
		verify(dosageDao).saveDosage(any(DosageProfile.class));
		assertEquals(dosageProfileRequest.getReminderTime(), dosageProfileResponse.getRemiderTime());
		// as the number of dosages taken passed in the request is 4, the
		// next dosage due date should be 14 days after last dosage taken date.
		long daysBetweenLastAndNextDosageDueDate = daysBetweenNextDosageAndLastDosageDates(dosageProfileRequest,
				dosageProfileResponse);
		assertEquals(daysBetweenLastAndNextDosageDueDate, 14);
	}

	/**
	 * tests save dosage service method, where the dosage information is
	 * successfully saved when the total number of dosages taken, passed in the
	 * request is 12. Asserts that there is 28 days difference between last
	 * dosage taken date and next dosage due date as the patient is now is four
	 * weeks per injection phase.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success_whenTotalNumberOfDosagesTakenAreTwelve() throws Exception {
		// set up
		final DosageProfileRequest dosageProfileRequest = buildDosageProfileRequest();
		dosageProfileRequest.getDosages().get(0).setDosageTakenDate(new Date());
		dosageProfileRequest.getDosages().get(0).setDosageNumber(12);
		when(objectMapper.writeValueAsString(dosageProfileRequest.getDosages()))
				.thenReturn(buildDosageListAsJsonString());

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.saveDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID,
				dosageProfileRequest);

		// assert
		verify(dosageDao).saveDosage(any(DosageProfile.class));
		assertEquals(dosageProfileRequest.getReminderTime(), dosageProfileResponse.getRemiderTime());
		// as the number of dosages taken passed in the request is 12, the
		// next dosage due date should be 28 days after last dosage taken date.
		long daysBetweenLastAndNextDosageDueDate = daysBetweenNextDosageAndLastDosageDates(dosageProfileRequest,
				dosageProfileResponse);
		assertEquals(daysBetweenLastAndNextDosageDueDate, 28);
	}

	/**
	 * tests save dosage service method, where the dosage information is
	 * successfully saved and also dosage schedule is generated and passed in
	 * dosage profile response.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success_dosageScheduleGenerated() throws Exception {
		// set up
		final DosageProfileRequest dosageProfileRequest = buildDosageProfileRequest();
		dosageProfileRequest.getDosages().get(0).setDosageTakenDate(new Date());
		when(objectMapper.writeValueAsString(dosageProfileRequest.getDosages()))
				.thenReturn(buildDosageListAsJsonString());
		final int noOfDosagesInRequest = dosageProfileRequest.getDosages().size();

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.saveDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID,
				dosageProfileRequest);

		// assert
		verify(dosageDao).saveDosage(any(DosageProfile.class));

		// verifies that dosage schedule is passed as part of dosage profile response.
		assertTrue(dosageProfileResponse.getDosages().size() > noOfDosagesInRequest);
	}

	/**
	 * Calculates the days between next dosage due date and last dosage taken
	 * date.
	 * 
	 * @param dosageProfileRequest
	 * @param dosageProfileResponse
	 * @return days between next dosage due and last dosage taken dates.
	 */
	private long daysBetweenNextDosageAndLastDosageDates(final DosageProfileRequest dosageProfileRequest,
			DosageProfileResponse dosageProfileResponse) {
		final long daysDifferenceInMilliseconds = dosageProfileResponse.getNextDosageDate().getTime()
				- dosageProfileRequest.getDosages().get(0).getDosageTakenDate().getTime();
		long daysBetweenLastAndNextDosageDueDate = TimeUnit.DAYS.convert(daysDifferenceInMilliseconds,
				TimeUnit.MILLISECONDS);
		return daysBetweenLastAndNextDosageDueDate;
	}
	
	/**
	 * tests save dosage service method, where not logged dosages are
	 * generated for the patient and saved successfully.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success_notLoggedDosagesGeneratedAndSaved() throws Exception {
		// set up
		final DosageProfileRequest dosageProfileRequest = buildDosageProfileRequest();
		dosageProfileRequest.getDosages().get(0).setDosageTakenDate(getOneMonthEarlierDate());
		when(objectMapper.writeValueAsString(dosageProfileRequest.getDosages()))
				.thenReturn(buildDosageListAsJsonString());

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.saveDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID,
				dosageProfileRequest);

		// assert
		verify(dosageDao).saveDosage(any(DosageProfile.class));
		assertEquals(dosageProfileRequest.getReminderTime(), dosageProfileResponse.getRemiderTime());
		// asserts that dosages with not_logged and not_taken statuses exists in the response.
		boolean dosageWithNotLoggedStatusExists = false;
		for (Dosage dosage : dosageProfileResponse.getDosages()) {
			if(NOT_LOGGED_DOSAGE_STATUS.equals(dosage.getDosageStatus())){
				dosageWithNotLoggedStatusExists = true;
				break;
			}
		}
		assertTrue(dosageWithNotLoggedStatusExists);
	}

	/**
	 * tests save dosage service method, when a service exception occurs while
	 * saving dosage information. Expects ServiceException to be thrown.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_failure_serviceExcepiton() throws Exception {
		// set up
		doThrow(DaoException.class).when(dosageDao).saveDosage(any(DosageProfile.class));
		DosageProfileRequest dosageProfileRequest = new DosageProfileRequest();
		exception.expect(ServiceException.class);

		// execute
		dosageServiceImpl.saveDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID, dosageProfileRequest);

		// assert
		verify(dosageDao).saveDosage(any(DosageProfile.class));
	}
	
	/**
	 * tests get dosage service method, where the dosage information is
	 * successfully retrieved.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_success() throws Exception {
		// set up
		final DosageProfile dosageProfile = buildDosageProfile();
		when(dosageDao.getDosage(any(DosageProfileId.class))).thenReturn(dosageProfile);
		when(objectMapper.readValue(any(String.class), any(TypeReference.class)))
				.thenReturn(buildDosageProfileRequest().getDosages());

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.getDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID);

		// assert
		verify(dosageDao).getDosage(any(DosageProfileId.class));
		assertNotNull(dosageProfileResponse);
	}
	
	/**
	 * tests get dosage service method, where the dosage information is
	 * successfully retrieved.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_success_notLoggedDosagesGeneratedInDosageSchedule() throws Exception {
		// set up
		final DosageProfile dosageProfile = buildDosageProfile();
		when(dosageDao.getDosage(any(DosageProfileId.class))).thenReturn(dosageProfile);
		DosageProfileRequest dosageProfileRequest = buildDosageProfileRequest();
		dosageProfileRequest.getDosages().get(0).setDosageTakenDate(getOneMonthEarlierDate());
		when(objectMapper.readValue(any(String.class), any(TypeReference.class)))
				.thenReturn(dosageProfileRequest.getDosages());

		// execute
		DosageProfileResponse dosageProfileResponse = dosageServiceImpl.getDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID);

		// assert
		verify(dosageDao).getDosage(any(DosageProfileId.class));
		assertNotNull(dosageProfileResponse);
		// asserts that dosages with not_logged and not_taken statuses exists in the response.
		boolean dosageWithNotLoggedStatusExists = false;
		boolean dosageWithNotTakenStatusExists = false;
		for (Dosage dosage : dosageProfileResponse.getDosages()) {
			if(NOT_LOGGED_DOSAGE_STATUS.equals(dosage.getDosageStatus())){
				dosageWithNotLoggedStatusExists = true;
			} else if(NOT_TAKEN_DOSAGE_STATUS.equals(dosage.getDosageStatus())){
				dosageWithNotTakenStatusExists = true;
			}
		}
		assertTrue(dosageWithNotLoggedStatusExists);
		assertTrue(dosageWithNotTakenStatusExists);
	}
	
	/**
	 * tests get dosage service method, where an exception occurs
	 * while retrieving dosage information.
	 * Expects ServiceException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_serviceException() throws Exception {
		// set up
		doThrow(DaoException.class).when(dosageDao).getDosage(any(DosageProfileId.class));
		exception.expect(ServiceException.class);
		
		// execute
		dosageServiceImpl.getDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID);

		// assert
		verify(dosageDao).getDosage(any(DosageProfileId.class));
	}
	
	/**
	 * tests get dosage service method, when no dosage record is retrieved.
	 * Expects EntityNotFoundException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_entityNotException() throws Exception {
		// set up
		doThrow(EntityNotFoundException.class).when(dosageDao).getDosage(any(DosageProfileId.class));
		exception.expect(EntityNotFoundException.class);
		
		// execute
		dosageServiceImpl.getDosage(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID);

		// assert
		verify(dosageDao).getDosage(any(DosageProfileId.class));
	}

}
