package com.hospital.repository;

import com.hospital.model.SymptomBodyPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomBodyPartRepository extends JpaRepository<SymptomBodyPart, Integer> {

    public List<SymptomBodyPart> findBySymptomId(Integer symtomId);
}
