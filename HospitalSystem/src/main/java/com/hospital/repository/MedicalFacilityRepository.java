package com.hospital.repository;

import org.springframework.data.jpa.repository.*;

import com.hospital.model.MedicalFacility;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalFacilityRepository extends JpaRepository<MedicalFacility, Integer>{

    MedicalFacility findByFacilityId(Integer id);

}
