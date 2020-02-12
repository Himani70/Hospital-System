package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "reason")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reason_id", unique = true, nullable = false)
    private Integer id;

    @Column(name="reason_code", nullable = false)
    private Integer reasonCode;

    @Column(name="reason_name", nullable = true)
    private String reasonName;


}
