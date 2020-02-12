package com.hospital.repository;

import com.hospital.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff findByStaffId(Integer staffId);

     Staff findByLastNameAndCityAndDob(String lastName, String city, Date dob);
}
