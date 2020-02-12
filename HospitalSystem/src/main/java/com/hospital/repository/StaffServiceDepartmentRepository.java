package com.hospital.repository;

import com.hospital.model.StaffServiceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffServiceDepartmentRepository extends JpaRepository<StaffServiceDepartment,Integer> {

    List<StaffServiceDepartment> findByEmployeeId(Integer employeeId);
}
