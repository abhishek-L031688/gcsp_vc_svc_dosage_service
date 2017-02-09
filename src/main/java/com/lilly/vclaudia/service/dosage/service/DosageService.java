package com.lilly.vclaudia.service.dosage.service;

import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.lilly.vclaudia.service.dosage.model.DosageProfileResponse;

/**
 * 
 * 
 * @author cramaswamy
 *
 */
public interface DosageService {
	
	/**
	 * Saves dosage profile information for a particular patient and product.
	 * 
	 * @param patientId
	 * @param productId
	 * @param dosageProfileRequest
	 * @return
	 * @throws ServiceException
	 */
	DosageProfileResponse saveDosage(String patientId, String productId, DosageProfileRequest dosageProfileRequest) throws ServiceException;

	/**
	 * Retrieves dosage information for a given patientId and productId.
	 * 
	 * @param patientId
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	DosageProfileResponse getDosage(String patientId, String productId) throws ServiceException;
}
