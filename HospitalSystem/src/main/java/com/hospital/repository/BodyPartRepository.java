package com.hospital.repository;

import com.hospital.model.BodyPart;
import com.hospital.model.MedicalFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyPartRepository extends JpaRepository<BodyPart, Integer> {
}
