package com.hospital.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)

public class BodyPartServiceDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="part_service_id", nullable = false)
    private Integer id;

    @Column(name="part_id", nullable = false)
    private Integer partId;

    @Column(name="dept_id", nullable = false)
    private Integer deptId;
}
