package com.lilly.vclaudia.service.dosage.dao;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lilly.vclaudia.service.dosage.exception.DaoException;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;
import com.lilly.vclaudia.service.dosage.repository.DosageRepository;

/**
 * Dosage DAO layer which performs database CRUD operations.
 * 
 * @author cramaswamy
 *
 */
@Component
public class DosageDaoImpl implements DosageDao {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DosageDaoImpl.class);

	@Inject
	private DosageRepository dosageRepository;

	/**
	 * Saves dosage profile in database.
	 * 
	 * @param dosageProfile
	 * @return dosage profile
	 * @throws DaoException
	 */
	@Override
	public DosageProfile saveDosage(DosageProfile dosageProfile) throws DaoException {
		try {
			DosageProfile dosageProfileResponse = dosageRepository.save(dosageProfile);
			LOGGER.debug(String.format("Dosage save successfully for patientId: %s and productId %s", dosageProfile.getDosageProfileId().getPatientId(),
					dosageProfile.getDosageProfileId().getProductId()));
			return dosageProfileResponse;
		} catch (Exception e) {
			throw new DaoException("Error while saving dosage profile information in database.", e);
		}
	}

	/**
	 * Retrieves dosage profile for a given patient and product from database.
	 * 
	 * @param dosageProfileId
	 * @return dosage profile
	 * @throws DaoException
	 * @throws EntityNotFoundException 
	 */
	@Override
	public DosageProfile getDosage(DosageProfileId dosageProfileId) throws DaoException, EntityNotFoundException {
		try {
			DosageProfile dosageProfileResponse = dosageRepository.findOne(dosageProfileId);
			if(null == dosageProfileResponse){
				LOGGER.error(String.format("No dosage retreived for patientId: %s and productId %s", dosageProfileId.getPatientId(),
						dosageProfileId.getProductId()));
				throw new EntityNotFoundException(String.format("No dosage retreived for patientId: %s and productId %s", dosageProfileId.getPatientId(),
						dosageProfileId.getProductId()));
			}
			LOGGER.debug(String.format("Dosage retreived successfully for patientId: %s and productId %s", dosageProfileId.getPatientId(),
					dosageProfileId.getProductId()));
			return dosageProfileResponse;
		} catch (EntityNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("Error retrieving dosage profile information from database.", e);
		}
	}

}
