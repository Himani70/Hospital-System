package com.hospital.repository;

import com.hospital.model.NegativeExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NegativeExperienceRepository extends JpaRepository<NegativeExperience, Integer> {
    NegativeExperience findByExperienceId(Integer id);
}
