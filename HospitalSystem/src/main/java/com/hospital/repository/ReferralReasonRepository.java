package com.hospital.repository;


import com.hospital.model.Reason;
import com.hospital.model.ReferralReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralReasonRepository extends JpaRepository<ReferralReason, Integer> {

     List <ReferralReason> findByReferralId(Integer id);
}
