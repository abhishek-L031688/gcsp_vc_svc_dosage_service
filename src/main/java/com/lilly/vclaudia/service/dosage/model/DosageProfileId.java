package com.lilly.vclaudia.service.dosage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Dosage profile composite primary key.
 * 
 * @author cramaswamy
 *
 */
@Embeddable
public class DosageProfileId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PATIENT_ID", nullable = false)
	private String patientId;

	@Column(name = "PRODUCT_ID", nullable = false)
	private String productId;
	
	public DosageProfileId(){
		
	}
	public DosageProfileId(String patientId, String productId) {
		this.patientId = patientId;
		this.productId = productId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DosageProfileId other = (DosageProfileId) obj;
		if (patientId == null) {
			if (other.patientId != null)
				return false;
		} else if (!patientId.equals(other.patientId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

}
