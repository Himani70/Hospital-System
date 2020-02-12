package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="service_id", nullable = false)
    private Integer id;

    @Column(name="service_code", nullable = false)
    private String serviceCode;

    @Column(name="service_name", nullable = false)
    private String serviceName;

}
