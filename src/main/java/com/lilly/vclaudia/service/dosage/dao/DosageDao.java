package com.lilly.vclaudia.service.dosage.dao;

import com.lilly.vclaudia.service.dosage.exception.DaoException;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;

/**
 * Dosage database access interface.
 * 
 * @author cramaswamy
 *
 */
public interface DosageDao {

	/**
	 * Saves dosage profile in database.
	 * 
	 * @param dosageProfile
	 * @return saved dosage profile
	 * @throws DaoException
	 */
	/*
	 */
	DosageProfile saveDosage(DosageProfile dosageProfile) throws DaoException;

	/**
	 * Retrieves dosage profile for a given patient and product from databse.
	 * 
	 * @param dosageProfileId
	 * @return dosage profile
	 * @throws DaoException
	 * @throws EntityNotFoundException 
	 */
	DosageProfile getDosage(DosageProfileId dosageProfileId) throws DaoException, EntityNotFoundException;
}
