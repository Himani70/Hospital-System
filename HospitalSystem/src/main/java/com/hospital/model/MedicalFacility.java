package com.hospital.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MedicalFacility {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="facility_id")
    private Integer facilityId;

    @Column(name="facility_name")
    private String facilityName;

    @Column(name="classification")
    private Integer classification;

    @Column(name="num_of_service_dept")
    private Integer numServiceDepartment;

    @Column(name="capacity")
    private Integer capacity;

}
