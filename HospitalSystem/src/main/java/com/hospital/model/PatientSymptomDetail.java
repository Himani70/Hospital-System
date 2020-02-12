package com.hospital.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "symptom_detail")
public class PatientSymptomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="symptom_detail_id", nullable = false)
    private Integer symptomDetailId;

    @Column(name="part_id", nullable = false)
    private Integer bodyPartId;

    @Column(name="sym_id", nullable = false)
    private Integer symptomId;

    @Column(name="patient_status_id", nullable = false)
    private Integer patientStatusId;

    @Column(name="is_occuring", nullable = false)
    private Boolean isOccuring;

    @Column(name="duration", nullable = false)
    private Integer duration;

    @Column(name="severity_scale", nullable = false)
    private Integer severityScaleId;

    @Column(name="cause", nullable = false)
    private String cause;


}
