package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Severity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="svr_id", nullable = false)
    private Integer id;

    @Column(name="svr_value", nullable = false)
    private String severityValue;

}

