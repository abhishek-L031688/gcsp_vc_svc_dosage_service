package com.lilly.vclaudia.service.dosage.exception;

/**
 * Authentication exception.
 * 
 * @author cramaswamy
 *
 */
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	
	public AuthenticationException(String msg)
	{
		super(msg);
	}
	
	public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
