package com.hospital.repository;

import com.hospital.model.PatientSymptomDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientSymptomDetailRepository extends JpaRepository<PatientSymptomDetail,Integer> {

    List<PatientSymptomDetail> findByPatientStatusId(Integer patientId);
}
