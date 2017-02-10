package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dosage profile response object.
 * 
 * @author cramaswamy
 *
 */
public class DosageProfileResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date nextDosageDate;
	
	private String remiderTime;

	private List<Dosage> dosages;

	public Date getNextDosageDate() {
		return nextDosageDate;
	}

	public void setNextDosageDate(Date nextDosageDate) {
		this.nextDosageDate = nextDosageDate;
	}

	public String getRemiderTime() {
		return remiderTime;
	}

	public void setRemiderTime(String remiderTime) {
		this.remiderTime = remiderTime;
	}

	public List<Dosage> getDosages() {
		return dosages;
	}

	public void setDosages(List<Dosage> dosages) {
		this.dosages = dosages;
	}

}
