package com.hospital.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name="patient_checkout")
public class PatientCheckout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="patient_checkout_id", unique = true, nullable = false)
    private Integer id;

    @Column(name="patient_id", nullable = false)
    private Integer patientId;

    @Column(name="discharge_status")
    private String dischargeStatus;

    @Column(name="treatment_given")
    private String treatmentGiven;

    @Column(name="experience_id")
    private Integer experienceId;

    @Column(name="referral_id")
    private Integer referralId;

    @Column(name="facility_id")
    private Integer facilityId;

    @Column(name="discharge_date")
    private Timestamp dischargeDate;

    @Column(name="acknowledgement")
    private String acknowledgement;
}
