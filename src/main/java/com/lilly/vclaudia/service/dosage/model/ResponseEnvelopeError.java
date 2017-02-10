package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;

/**
 * Api error response class which holds the details about api failure.
 * 
 * @author cramaswamy
 *
 */
public class ResponseEnvelopeError implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String success = "false";
	
	private Object payload;
	
	private ErrorEnv error;

	public ErrorEnv getError() {
		return error;
	}

	public void setError(ErrorEnv error) {
		this.error = error;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
