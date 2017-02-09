package com.lilly.vclaudia.service.dosage.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lilly.vclaudia.service.dosage.common.TestCommon;
import com.lilly.vclaudia.service.dosage.exception.DaoException;
import com.lilly.vclaudia.service.dosage.exception.EntityNotFoundException;
import com.lilly.vclaudia.service.dosage.model.DosageProfile;
import com.lilly.vclaudia.service.dosage.model.DosageProfileId;
import com.lilly.vclaudia.service.dosage.repository.DosageRepository;

/**
 * Junit test suite for dosage dao implementation.
 * 
 * @author cramaswamy
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DosageDaoImplTest extends TestCommon {

	@Mock
	private DosageRepository dosageRepository;

	@InjectMocks
	private DosageDaoImpl dosageDaoImpl;

	@Rule()
	public ExpectedException exception = ExpectedException.none();

	/**
	 * tests save dosage dao method, where dosage information is successfully
	 * stored in the database
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_success() throws Exception {
		// set up
		final DosageProfile dosageProfile = buildDosageProfile();
		when(dosageRepository.save(any(DosageProfile.class))).thenReturn(dosageProfile);

		// execute
		DosageProfile dosageProfileResponse = dosageDaoImpl.saveDosage(dosageProfile);

		// assert
		verify(dosageRepository).save(any(DosageProfile.class));
		assertEquals(dosageProfile.getDosageProfileId().getPatientId(), dosageProfileResponse.getDosageProfileId().getPatientId());
	}

	/**
	 * tests save dosage dao method, where dosage information is successfully
	 * stored in the database
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSaveDosage_failure_daoException() throws Exception {
		// set up
		final DosageProfile dosageProfile = buildDosageProfile();
		dosageProfile.setDosageProfileId(new DosageProfileId("patientId", "productId"));
		doThrow(Exception.class).when(dosageRepository).save(any(DosageProfile.class));
		exception.expect(DaoException.class);

		// execute
		dosageDaoImpl.saveDosage(dosageProfile);

		// assert
		verify(dosageRepository).save(any(DosageProfile.class));
	}
	
	/**
	 * tests get dosage dao method, where dosage information is successfully
	 * retrieved from the database
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_success() throws Exception {
		// set up
		final DosageProfile dosageProfile = buildDosageProfile();
		final DosageProfileId dosageProfileId = buildDosageProfileId();
		when(dosageRepository.findOne(dosageProfileId)).thenReturn(dosageProfile);

		// execute
		DosageProfile dosageProfileResponse = dosageDaoImpl.getDosage(dosageProfileId);

		// assert
		verify(dosageRepository).findOne(any(DosageProfileId.class));
		assertEquals(dosageProfile.getDosageProfileId().getPatientId(), dosageProfileResponse.getDosageProfileId().getPatientId());
	}
	
	/**
	 * tests get dosage dao method, where no dosage information is
	 * retrieved from the database for the given patientId and productId.
	 * Expects EntityNotFoundException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_entityNotFoundException() throws Exception {
		// set up
		final DosageProfileId dosageProfileId = buildDosageProfileId();
		when(dosageRepository.findOne(dosageProfileId)).thenReturn(null);
		exception.expect(EntityNotFoundException.class);

		// execute
		DosageProfile dosageProfileResponse = dosageDaoImpl.getDosage(dosageProfileId);

		// assert
		verify(dosageRepository).findOne(any(DosageProfileId.class));
		assertNull(dosageProfileResponse);
	}
	
	/**
	 * tests get dosage dao method, where an exception occurs while
	 * retrieving dosage information from databse.
	 * Expects EntityNotFoundException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDosage_failure_daoException() throws Exception {
		// set up
		doThrow(Exception.class).when(dosageRepository).findOne(any(DosageProfileId.class));
		exception.expect(DaoException.class);

		// execute
		dosageDaoImpl.getDosage(any(DosageProfileId.class));

		// assert
		verify(dosageRepository).findOne(any(DosageProfileId.class));
	}

}
