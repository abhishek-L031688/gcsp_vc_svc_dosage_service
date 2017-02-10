package com.lilly.vclaudia.service.dosage.model;

/**
 * This class holds api error code.
 * 
 * @author cramaswamy
 *
 */
public class ErrorEnv {
	
	private String code;

	public ErrorEnv(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
