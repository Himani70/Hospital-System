package com.hospital.repository;


import com.hospital.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Service findByServiceCode(String id);

    Service findByServiceName(String serv);
}
