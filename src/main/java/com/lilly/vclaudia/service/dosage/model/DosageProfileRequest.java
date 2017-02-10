package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dosage profile request details.
 * 
 * @author cramaswamy
 *
 */
public class DosageProfileRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Pattern(regexp="((0[0-9]|1[0-9]|2[0-3]):[0-5][0,5])", message = "Reminder time must match pattern ((0[0-9]|1[0-9]|2[0-3]):[0-5][0,5])")
	private String reminderTime;
	
	@NotEmpty(message = "Dosages must not be null or empty")
	@Valid
	private List<Dosage> dosages;

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	public List<Dosage> getDosages() {
		return dosages;
	}

	public void setDosages(List<Dosage> dosages) {
		this.dosages = dosages;
	}

}
