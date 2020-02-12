package com.hospital.repository;

import com.hospital.model.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SeverityRepository extends JpaRepository<Severity, Integer> {

    List<Severity> findByIdIn(List<Integer> severityIdList);

    Severity findBySeverityValue(String value);
}
