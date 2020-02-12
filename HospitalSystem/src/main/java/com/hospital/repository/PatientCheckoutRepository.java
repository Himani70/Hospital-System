package com.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hospital.model.PatientCheckout;

@Repository
public interface PatientCheckoutRepository extends JpaRepository<PatientCheckout, Integer> {

    PatientCheckout findByPatientIdAndFacilityId(int patientId, int facilityId);
}
