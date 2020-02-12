package com.hospital.repository;

import com.hospital.model.SeverityScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeverityScaleRepository  extends JpaRepository<SeverityScale, Integer> {

    List<SeverityScale> findBySymptomId(Integer symptomId);

    SeverityScale findBySeverityIdAndSymptomId(Integer severityId, Integer symptomId);
}
