package com.lilly.vclaudia.service.dosage.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.lilly.vclaudia.service.dosage.common.TestCommon;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.exception.ServiceException;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.lilly.vclaudia.service.dosage.model.DosageProfileResponse;
import com.lilly.vclaudia.service.dosage.service.DosageService;

/**
 * junit test suite for dosage controller.
 * 
 * @author cramaswamy
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DosageController.class)
public class DosageControllerTest extends TestCommon {

	@MockBean
	private DosageService dosageService;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * tests save dosage request endpoint, where the dosage information is saved
	 * successfully.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success() throws Exception {
		// set up
		when(dosageService.saveDosage(anyString(), anyString(), any(DosageProfileRequest.class)))
				.thenReturn(new DosageProfileResponse());

		// test
		mockMvc.perform(post("/patient/patientId/product/productId/dosage").contentType(MediaType.APPLICATION_JSON)
				.content(buildDosageProfileRequestJson())).andExpect(status().isCreated());

	}

	/**
	 * tests save dosage request endpoint, when empty request body is passed.
	 * Expects 400 as response code.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_failure_badRequestException() throws Exception {

		// test
		mockMvc.perform(post("/patient/patientId/product/productId/dosage")).andExpect(status().isBadRequest());

	}
	
	/**
	 * tests save dosage request endpoint, where the dosage information is saved
	 * successfully.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_success() throws Exception {
		// set up
		when(dosageService.getDosage(anyString(), anyString())).thenReturn(new DosageProfileResponse());

		// test
		mockMvc.perform(get("/patient/patientId/product/productId/dosage")).andExpect(status().isOk());

	}
	
	/**
	 * tests save dosage request endpoint, where no dosage record
	 * is retrieved.
	 * Expects 404 not found as the response.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_entityNotFoundException() throws Exception {
		// set up
		doThrow(EntityNotFoundException.class).when(dosageService).getDosage(anyString(), anyString());
		
		// test
		mockMvc.perform(get("/patient/patientId/product/productId/dosage")).andExpect(status().isNotFound());
	}
	
	/**
	 * tests save dosage request endpoint, where an error occurs while
	 * retrieving dosage information.
	 * Expects 500 internal server error as the response.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_serviceException() throws Exception {
		// set up
		doThrow(ServiceException.class).when(dosageService).getDosage(anyString(), anyString());
		
		// test
		mockMvc.perform(get("/patient/patientId/product/productId/dosage")).andExpect(status().isInternalServerError());
	}

}
