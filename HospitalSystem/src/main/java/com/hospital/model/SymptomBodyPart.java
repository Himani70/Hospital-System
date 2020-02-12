package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name="symptom_body_part")
public class SymptomBodyPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sym_body_part_id", nullable = false)
    private Integer id;

    @Column(name="sym_id", nullable = false)
    private Integer symptomId;

    @Column(name="part_id", nullable = false)
    private Integer partId;
}
