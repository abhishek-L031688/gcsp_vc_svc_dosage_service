package com.lilly.vclaudia.service.dosage.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean to capture request validation errors.
 * 
 * @author cramaswamy
 *
 */
public class ValidationError {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<String> errors = new ArrayList<>();

	private final String errorMessage;

	public ValidationError(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void addValidationError(String error) {
		errors.add(error);
	}

	public List<String> getErrors() {
		return errors;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
