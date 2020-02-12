package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sym_id", nullable = false)
    private Integer id;

    @Column(name="sym_name", nullable = false)
    private String symtomName;

    @Column(name="sym_code", nullable = false)
    private String symptomCode;

}
