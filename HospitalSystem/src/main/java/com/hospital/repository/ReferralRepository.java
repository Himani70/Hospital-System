package com.hospital.repository;

import com.hospital.model.Patient;
import com.hospital.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Integer> {
    List<Referral> findByPatientId(int patientId);
}
