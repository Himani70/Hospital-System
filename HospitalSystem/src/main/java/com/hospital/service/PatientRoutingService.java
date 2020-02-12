package com.hospital.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.model.MedicalFacility;
import com.hospital.model.Patient;
import com.hospital.model.PatientStatus;
import com.hospital.model.Symptom;
import com.hospital.repository.MedicalFacilityRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PatientStatusRepository;
import com.hospital.repository.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PatientRoutingService {


    @Autowired
    SymptomSevice symptomSevice;
    @Autowired
    PatientStatusRepository patientStatusRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PatientCheckoutService patientCheckoutService;

    private ObjectMapper mapper = new ObjectMapper();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


    public void patientRoutingMenu(Patient patient, Integer facilityId) throws Exception {
        System.out.println("Patient Routing Menu\n");
        System.out.println("1. Check-in\n" +
                "2. Check-out acknowledgement\n" +
                "3. Return back");

        int in = Integer.parseInt(input.readLine());
        if(in == 3){
            System.out.println("Returning to Home...");
            return;
        }
        MedicalFacility medicalFacility = em.find(MedicalFacility.class, facilityId);
        if(in == 1) {
            checkIn(patient, facilityId);
        }

        if (in == 2) {
           patientCheckoutService.patientCheckoutAcknowledgment(patient, medicalFacility);
        }

    }

    private void checkIn(Patient patient, Integer facilityId) throws IOException {
        System.out.println("Check In process...");

        PatientStatus patientStatus = patientStatusRepository.findByPatientIdAndFacilityId(patient.getId(), facilityId);
        if(patientStatus == null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            patientStatus =  PatientStatus.builder()
                    .patientId(patient.getId())
                    .facilityId(facilityId)
                    .startTime(timestamp)
                    .highBp(120)
                    .lowBp(80)
                    .temperature(98f)
                    .isCheckout(false)
                    .isTreated(false)
                    .build();
            try {
                patientStatusRepository.save(patientStatus);
                System.out.println("Checked in patient " + patient.getFirstName() + " "
                        + patient.getLastName() + " in facility " + facilityId);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Failed to check in patient at facility " + facilityId + e.getMessage());
                return;
            }
        } else {
            System.out.println("You are already checked in this facility");
        }

        //System.out.println("\n");
        symptomSevice.getSymptomMetaDataFromUser(patientStatus);
    }

}
