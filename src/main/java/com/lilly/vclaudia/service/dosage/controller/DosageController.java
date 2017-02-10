package com.lilly.vclaudia.service.dosage.controller;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.exception.ExceptionList;
import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.handler.AbsractRequestHandler;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.lilly.vclaudia.service.dosage.model.DosageProfileResponse;
import com.lilly.vclaudia.service.dosage.service.DosageService;

/**
 * Controller class for Dosage CRUD operations.
 * 
 * @author cramaswamy
 *
 */
@RestController
public class DosageController extends AbsractRequestHandler {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DosageController.class);

	@Autowired
	private DosageService dosageService;

	@Autowired
	private HttpServletResponse response;

	/**
	 * Saves dosage information for a particular patient and product.
	 * 
	 * @param dosageProfileRequest
	 * @param patientId
	 * @param productId
	 * @param jwtBearerToken
	 * @param xForwardedProto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/patient/{patientId}/product/{productId}/dosage", method = RequestMethod.POST)
	public DosageProfileResponse saveDosage(@Valid @RequestBody DosageProfileRequest dosageProfileRequest,
			@PathVariable String patientId, @PathVariable String productId,
			@RequestHeader(required = false, value = "Authorization") String jwtBearerToken,
			@RequestHeader(required = false, value = "X-Forwarded-Proto") String xForwardedProto) throws Exception {
		LOGGER.debug(String.format("Request received to save dosage information for patientId: %s and productId %s",
				patientId, productId));
		authenticateServiceRequest(jwtBearerToken, xForwardedProto);
		try {
			final DosageProfileResponse dosageProfileResponse = dosageService.saveDosage(patientId, productId,
					dosageProfileRequest);
			response.setStatus(HttpURLConnection.HTTP_CREATED);
			return dosageProfileResponse;
		} catch (ServiceException e) {
			LOGGER.error("Error saving dosage profile.", e);
			response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			throw new ServiceException(ExceptionList.GENERICCODE);
		}
	}
	
	/**
	 * Saves dosage information for a particular patient and product.
	 * 
	 * @param dosageProfileRequest
	 * @param patientId
	 * @param productId
	 * @param jwtBearerToken
	 * @param xForwardedProto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/patient/{patientId}/product/{productId}/dosage", method = RequestMethod.GET)
	public DosageProfileResponse getDosage(@PathVariable String patientId, @PathVariable String productId,
			@RequestHeader(required = false, value = "Authorization") String jwtBearerToken,
			@RequestHeader(required = false, value = "X-Forwarded-Proto") String xForwardedProto) throws Exception {
		LOGGER.debug(String.format("Request received to save dosage information for patientId: %s and productId %s",
				patientId, productId));
		authenticateServiceRequest(jwtBearerToken, xForwardedProto);
		try {
			final DosageProfileResponse dosageProfileResponse = dosageService.getDosage(patientId, productId);
			response.setStatus(HttpURLConnection.HTTP_OK);
			return dosageProfileResponse;
		} catch (Exception e) {
			LOGGER.error("Error retreiving dosage profile.", e);
			if(e instanceof EntityNotFoundException){
				response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);	
			} else {
				response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			}
			return null;
		}
	}

}
