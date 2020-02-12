package com.hospital.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ReferralReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="referral_reason_id", unique = true, nullable = false)
    private Integer id;

    @Column(name="reason_code")
    private Integer reasonCode;

    @Column(name="referral_id")
    private Integer referralId;

    @Column(name="description")
    private String description;

    @Column(name="service_name")
    private String serviceName;
}
