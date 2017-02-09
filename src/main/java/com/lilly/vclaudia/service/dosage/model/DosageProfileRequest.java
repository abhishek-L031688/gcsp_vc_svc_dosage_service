package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dosage profile request details.
 * 
 * @author cramaswamy
 *
 */
public class DosageProfileRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Reminder time must not be null or empty")
	private Date reminderTime;
	
	@NotEmpty(message = "Dosages must not be null or empty")
	@Valid
	private List<Dosage> dosages;

	public Date getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	public List<Dosage> getDosages() {
		return dosages;
	}

	public void setDosages(List<Dosage> dosages) {
		this.dosages = dosages;
	}

}
