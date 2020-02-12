package com.hospital.repository;
import com.hospital.model.PatientStatus;
import com.hospital.model.Staff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PatientStatusRepository extends JpaRepository<PatientStatus, Integer> {

    @Query(value = "select ps from PatientStatus ps where ps.startTime is not null and ps.endTime is null and ps.facilityId = :facilityId")
    List<PatientStatus> findCheckedInPatients(Integer facilityId);

    @Query(value = "select ps from PatientStatus ps where ps.startTime is not null and ps.endTime is not null and ps.isTreated is false and ps.facilityId = :facilityId")
    List<PatientStatus> findPatientsToBeTreated(Integer facilityId);

    List<PatientStatus> findByIsTreatedAndIsCheckoutAndFacilityId(Boolean isTreated,Boolean isCheckout, Integer facilityId);

    PatientStatus findByPatientIdAndFacilityId(Integer patientId, Integer facilityId);





}
