package com.hospital.service;


import com.hospital.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;
    public List<com.hospital.model.Service> getServiceList(){
        return serviceRepository.findAll();
    }

}
