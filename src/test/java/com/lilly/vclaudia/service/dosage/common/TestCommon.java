package com.lilly.vclaudia.service.dosage.common;

import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;
import com.lilly.vclaudia.service.dosage.model.DosageProfileRequest;
import com.newrelic.agent.deps.com.google.gson.JsonElement;
import com.newrelic.agent.deps.com.google.gson.JsonObject;
import com.newrelic.agent.deps.com.google.gson.JsonParser;

public class TestCommon {
	
	protected static final String SAMPLE_PATIENT_ID = "patientId123";
	protected static final String SAMPLE_PRODUCT_ID = "productId123";
	
	/**
	 * builds sample dosage profile request.
	 * 
	 * @return dosageProfile
	 * @throws Exception 
	 */
	protected DosageProfile buildDosageProfile() throws Exception{
		DosageProfile dosageProfile = new DosageProfile();
		dosageProfile.setDosageProfileId(new DosageProfileId(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID));
		dosageProfile.setDosages(buildDosageListAsJsonString());
		return dosageProfile;
	}

	/**
	 * Builds sample dosage profile request.
	 * 
	 * @return DosageProfileRequest
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	protected DosageProfileRequest buildDosageProfileRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(readSaveDosageRequestJson().toString(), DosageProfileRequest.class);
	}
	
	/**
	 * Returns a sample dosage profile request as json string.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected String buildDosageProfileRequestJson() throws Exception{
		return readSaveDosageRequestJson().toString();
	}
	
	/**
	 * Reads save dosage request json file.
	 * 
	 * @return
	 * @throws Exception
	 */
	private JsonObject readSaveDosageRequestJson() throws Exception{
		JsonParser parser = new JsonParser();
        JsonElement jsonElement = (JsonElement)parser.parse(new FileReader(
        		TestCommon.class.getClassLoader().getResource("save-dosage-request.json").getFile()));
        return jsonElement.getAsJsonObject();
	}
	
	/**
	 * builds sample dosage profile request.
	 * 
	 * @return dosageProfile
	 * @throws Exception 
	 */
	protected String buildDosageListAsJsonString() throws Exception{
		return readSaveDosageRequestJson().get("dosages").toString();
		
	}
	
	/**
	 * builds sample dosage profile id request.
	 * 
	 * @return dosageProfile
	 * @throws Exception 
	 */
	protected DosageProfileId buildDosageProfileId() throws Exception{
		return new DosageProfileId(SAMPLE_PATIENT_ID, SAMPLE_PRODUCT_ID);
	}
}
