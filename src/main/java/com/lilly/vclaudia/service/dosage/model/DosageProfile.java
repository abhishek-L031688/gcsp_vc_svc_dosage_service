package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Dosage profile request entity.
 * 
 * @author cramaswamy
 *
 */
@Entity
@Table(name = "DOSAGE_PROFILE")
public class DosageProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DosageProfileId dosageProfileId;

	@Column(name = "REMINDER_TIME", nullable = false)
	private Date reminderTime;

	@Column(name = "DOSAGE", nullable = false)
	private String dosages;

	public Date getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	public DosageProfileId getDosageProfileId() {
		return dosageProfileId;
	}

	public void setDosageProfileId(DosageProfileId dosageProfileId) {
		this.dosageProfileId = dosageProfileId;
	}

	public String getDosages() {
		return dosages;
	}

	public void setDosages(String dosages) {
		this.dosages = dosages;
	}
	
}
