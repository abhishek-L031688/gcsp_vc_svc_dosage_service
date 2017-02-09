package com.lilly.vclaudia.service.dosage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;

/**
 * Dosage profile CRUD repository.
 * 
 * @author cramaswamy
 *
 */
@Repository
public interface DosageRepository extends CrudRepository<DosageProfile, DosageProfileId> {

}
