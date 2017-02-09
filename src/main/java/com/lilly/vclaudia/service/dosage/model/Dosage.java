package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dosage object which contains each dose details.
 * 
 * @author cramaswamy
 *
 */
public class Dosage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Dosage number must not be null")
	private Integer dosageNumber;
	
	@NotEmpty(message = "Dosage status must not be null or empty")
	private String dosageStatus;
	
	@NotNull(message = "Dosage taken date must not be null or empty")
	private Date dosageTakenDate;

	public Dosage() {
	}

	public Dosage(Integer dosageNumber, String dosageStatus, Date dosageTakenDate) {
		this.dosageNumber = dosageNumber;
		this.dosageStatus = dosageStatus;
		this.dosageTakenDate = dosageTakenDate;
	}

	public Integer getDosageNumber() {
		return dosageNumber;
	}

	public void setDosageNumber(Integer dosageNumber) {
		this.dosageNumber = dosageNumber;
	}

	public String getDosageStatus() {
		return dosageStatus;
	}

	public void setDosageStatus(String dosageStatus) {
		this.dosageStatus = dosageStatus;
	}

	public Date getDosageTakenDate() {
		return dosageTakenDate;
	}

	public void setDosageTakenDate(Date dosageTakenDate) {
		this.dosageTakenDate = dosageTakenDate;
	}
	

}
