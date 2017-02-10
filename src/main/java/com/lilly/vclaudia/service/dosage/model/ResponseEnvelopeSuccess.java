package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;

/**
 * Api success response class, which holds the api response body
 * and success status.
 * 
 * @author cramaswamy
 *
 */
public class ResponseEnvelopeSuccess implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String success="true";
	
	private Object payload;
	
	public ResponseEnvelopeSuccess(Object payload) {
		this.payload = payload;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
}

