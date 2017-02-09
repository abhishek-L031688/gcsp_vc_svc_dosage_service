package com.lilly.vclaudia.service.dosage.exception;

/**
 * Thrown when a specified entity is not found.
 *
 * @author cramaswamy
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends ServiceException {
	
	public EntityNotFoundException(String message) {
		super(message);
	}
	
}
