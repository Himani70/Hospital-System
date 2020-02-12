package com.hospital.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BodyPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="part_id", nullable = false)
    private Integer id;

    @Column(name="body_part_name", nullable = false)
    private String bodyPartName;

    @Column(name="part_code", nullable = false)
    private String bodyPartCode;

}
