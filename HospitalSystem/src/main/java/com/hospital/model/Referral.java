package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="referral_id", nullable = false)
    private Integer id;

    @Column(name="patient_id", nullable = false)
    private Integer patientId;

    @Column(name="emp_id", nullable = false)
    private Integer empId;

    @Column(name="facility_id", nullable = false)
    private Integer facilityId;

}
