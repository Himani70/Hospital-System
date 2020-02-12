package com.hospital.model;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)

public class StaffServiceDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="staff_department_mapping_id", nullable = false)
    private Integer id;

    @Column(name="employee_id", nullable = false)
    private Integer employeeId;

    @Column(name="department_id", nullable = false)
    private Integer departmentId;

    @Column(name="is_primary", nullable = false)
    private boolean isPrimary;


}
