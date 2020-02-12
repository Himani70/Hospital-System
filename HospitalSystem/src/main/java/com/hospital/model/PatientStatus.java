package com.hospital.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)

public class PatientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="patient_status_id", nullable = false)
    private Integer id;

    @Column(name="patient_id", nullable = false)
    private Integer patientId;

    @Column(name="facility_id", nullable = false)
    private Integer facilityId;

    @Column(name="start_time", nullable = false)
    private Timestamp startTime;

    @Column(name="end_time")
    private Timestamp endTime;

    @Column(name="low_bp")
    private Integer lowBp;

    @Column(name="high_bp")
    private Integer highBp;

    @Column(name="temperature")
    private Float temperature;

    @Column(name="is_treated")
    private Boolean isTreated;

    @Column(name="treated_time")
    private Timestamp treatedTime;

    @Column(name="priority_status_id")
    private Integer priorityStatus;

    @Column(name="is_checkout",nullable = false)
    private Boolean isCheckout;

}
