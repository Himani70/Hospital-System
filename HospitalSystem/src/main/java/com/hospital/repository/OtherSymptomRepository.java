package com.hospital.repository;

import com.hospital.model.OtherSymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherSymptomRepository extends JpaRepository<OtherSymptom, Integer> {
}
