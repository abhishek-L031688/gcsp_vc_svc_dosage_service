package com.lilly.vclaudia.service.dosage.exception;

/**
 * Checked service exception.
 * 
 * @author cramaswamy
 *
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
	
	public ServiceException(String msg)
	{
		super(msg);
	}
	
	public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
