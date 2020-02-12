package com.hospital.repository;

import com.hospital.model.BodyPartServiceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyPartServiceDepartmentRepository extends JpaRepository<BodyPartServiceDepartment,Integer> {

    List<BodyPartServiceDepartment> findByPartIdIn(List<Integer> partId);

}
