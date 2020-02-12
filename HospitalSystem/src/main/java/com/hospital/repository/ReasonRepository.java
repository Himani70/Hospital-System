package com.hospital.repository;

import com.hospital.model.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Integer> {

    Reason findByReasonCode(int id);

}
