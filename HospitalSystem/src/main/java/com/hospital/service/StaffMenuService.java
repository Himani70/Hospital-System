package com.hospital.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.model.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StaffMenuService {

    @Autowired
    private PatientStatusRepository patientStatusRepository;
    @Autowired
    private PatientSymptomDetailRepository patientSymptomDetailRepository;
    @Autowired
    private BodyPartServiceDepartmentRepository bodyPartServiceDepartmentRepository;
    @Autowired
    private PatientCheckoutService patientCheckoutService;
    @Autowired
    private PatientRepository patientRepository;
    private StaffServiceDepartmentRepository staffServiceDepartmentRepository;
    @Autowired
    private SymptomSevice symptomSevice;
    @Autowired
    private MedicalFacilityService medicalFacilityService;

    @PersistenceContext
    private EntityManager em;

    private ObjectMapper mapper = new ObjectMapper();

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void StaffMenu(Staff staff, MedicalFacility medicalFacility) throws ParseException, Exception {

        System.out.println("Staff Menu\n" +
                "1. Checked-in patient list\n" +
                "2. Treated patient list\n" +
                "3. Add symptoms\n" +
                "4. Add severity scale\n" +
                "5. Add assessment rule\n" +
                "6. Go back");

        int action = Integer.parseInt(input.readLine());

        if(action == 6) {
            return;
        }
        while (action != 6) {
            if (action == 1) {
                //Display the list of patients who finished self check in
                System.out.println("List of checked in patients in facility "
                        + medicalFacility.getFacilityId() + ". " + medicalFacility.getFacilityName());
                List<PatientStatus> CheckedInPatientList = patientStatusRepository.findCheckedInPatients(medicalFacility.getFacilityId());
                System.out.println("Id  FirstName LastName  CheckInTime  IsTreated\n\n");
                for (PatientStatus patientStatus : CheckedInPatientList) {
                    Patient patient = em.find(Patient.class, patientStatus.getPatientId());
                    System.out.println(patient.getId() + " " + patient.getFirstName()
                            + " " + patient.getLastName() + "  " + patientStatus.getStartTime()
                            + "  " + patientStatus.getIsTreated());
                }
                StaffProcessPatient(staff, medicalFacility);
            } else if (action == 2) {
                //Display the Treated patient list
                System.out.println("List of all treated patients in Facility "
                        + medicalFacility.getFacilityId() + ". " + medicalFacility.getFacilityName());
                List<PatientStatus> TreatedPatientList = patientStatusRepository.findByIsTreatedAndIsCheckoutAndFacilityId(true, false, medicalFacility.getFacilityId());
                if (TreatedPatientList == null || TreatedPatientList.isEmpty()){
                    System.out.println("\nAll patients have checkout from this facility");
                } else {
                    System.out.println("\nId FirstName LastName  CheckInTime  VitalsEnterAt" +
                            "  Temperature  LowBP HighBP\n\n");
                }
                for (PatientStatus patientStatus : TreatedPatientList) {
                    Patient patient = em.find(Patient.class, patientStatus.getPatientId());
                    System.out.println(patient.getId() + "  " + patient.getFirstName()
                            + " " + patient.getLastName() + " " + patientStatus.getIsTreated()
                            +"  " + patientStatus.getStartTime() + "  " + patientStatus.getEndTime()
                            + "  " + patientStatus.getTemperature() + "  " + patientStatus.getLowBp()
                            + " " + patientStatus.getHighBp());
                }
                TreatedPatientList(staff, medicalFacility);
            } else if (action == 3) {
                //Add Symptoms
                symptomSevice.addNewSymptom();

            } else if (action == 4) {
                //Add severity
                medicalFacilityService.addSeverityMenu();
            } else if (action == 5) {
                //Add assessment rule
                medicalFacilityService.addRule();
            }
            System.out.println("Staff Menu\n" +
                    "1. Checked-in patient list\n" +
                    "2. Treated patient list\n" +
                    "3. Add symptoms\n" +
                    "4. Add severity scale\n" +
                    "5. Add assessment rule\n" +
                    "6. Go back");

            action = Integer.parseInt(input.readLine());
        }
    }

    //Staff Process Patient
    public void StaffProcessPatient(Staff staff, MedicalFacility medicalFacility) throws Exception{
        System.out.println("Staff Process Patient Menu\n" +
                "1. Enter Vitals\n" +
                "2. Treat Patient\n" +
                "3. Go back");
        int action = Integer.parseInt(input.readLine());

        if(action == 3){
            return;
        }
        while (action != 3) {
            if (action == 1) {
                //Check if the user is a valid medical staff
                if (staff.getDesignation().equalsIgnoreCase("medical")) {
                    enterVitalData(staff, medicalFacility);
                } else {
                    System.out.println("Inadequate privileges to enter patient vital data");
                    return;
                }
            } else if (action == 2) {
                treatPatient(staff, medicalFacility);
            }
            System.out.println("Staff Process Patient Menu\n" +
                    "1. Enter Vitals\n" +
                    "2. Treat Patient\n" +
                    "3. Go back");
            action = Integer.parseInt(input.readLine());
        }
    }

    private void treatPatient(Staff staff, MedicalFacility medicalFacility) throws IOException {
        List<PatientStatus> PatientsToBeTreatedList = patientStatusRepository.findPatientsToBeTreated(medicalFacility.getFacilityId());
        if(PatientsToBeTreatedList == null || PatientsToBeTreatedList.isEmpty()){
            System.out.println("No patient available for treatment");
            return;
        }
        System.out.println("Id  FirstName LastName CheckInTime  VitalsEnterAt  Temperature  Low BP  High BP\n\n");
        for(PatientStatus patientStatus: PatientsToBeTreatedList){
            Patient patient = em.find(Patient.class, patientStatus.getPatientId());
            System.out.println(patient.getId() + " " + patient.getFirstName()
                    + " " + patient.getLastName() + "  " + patientStatus.getStartTime()
                    + "  " + patientStatus.getEndTime() + "  " + patientStatus.getTemperature()
            + "  " + patientStatus.getLowBp() + "  " + patientStatus.getHighBp());
        }
        System.out.println("Select patient from the list of patients who completed the checked in process");
        Integer patientId = Integer.parseInt(input.readLine());

        Integer facilityId = medicalFacility.getFacilityId();
        Integer staffId = staff.getStaffId();
        //Check if the user can treat body part associated to patient symptoms
        //If yes, move patient to the treated list else prompt inadequate privileges
        PatientStatus checkIdPatient = patientStatusRepository.findByPatientIdAndFacilityId(patientId,facilityId);
        if(checkIdPatient == null) {
            System.out.println("Invalid input. NO patient with given id in this facility");
            return;
        }
        List<PatientSymptomDetail> patientSymptomDetailList = patientSymptomDetailRepository.findByPatientStatusId(checkIdPatient.getId());
        List<Integer> partIdList = new ArrayList<>();
        for(PatientSymptomDetail patientSymptomDetail : patientSymptomDetailList) {
            partIdList.add(patientSymptomDetail.getBodyPartId());
        }
        List<BodyPartServiceDepartment> BodyPartServiceDepartmentList = bodyPartServiceDepartmentRepository.findByPartIdIn(partIdList);
        List<StaffServiceDepartment> StaffServiceDepartmentList = staffServiceDepartmentRepository.findByEmployeeId(staffId);
        boolean flag = false;

        loop:
        for(BodyPartServiceDepartment bodyPartServiceDepartment: BodyPartServiceDepartmentList){
            for(StaffServiceDepartment staffServiceDepartment: StaffServiceDepartmentList){
                if(bodyPartServiceDepartment.getDeptId().equals(staffServiceDepartment.getDepartmentId())){
                    flag = true;
                    break loop;
                }
            }
        }
        if(flag){
            PatientStatus patientStatus = patientStatusRepository.findByPatientIdAndFacilityId(patientId,facilityId);
            patientStatus.setIsTreated(true);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            patientStatus.setTreatedTime(timestamp);
            try{
                patientStatusRepository.save(patientStatus);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Failed to insert patient vitals into DB" + e.getMessage());
                return;
            }
            System.out.println("The patient is treated successfully");
        }
        else{
            System.out.println("Inadequate privileges to treat patient");
            System.out.println("Redirecting back to Staff Process Patient Menu");
        }
    }

    public void TreatedPatientList(Staff staff, MedicalFacility medicalFacility) throws Exception{
        System.out.println("Staff Treated Patient List Menu\n" +
                "1. Check out\n" +
                "2. Go back");
        int action = Integer.parseInt(input.readLine());
        if(action == 1) {
            System.out.println("Select the patient to check out\n");
            Integer patientId = Integer.parseInt(input.readLine());
            Integer facilityId = medicalFacility.getFacilityId();
            PatientStatus patientStatus = patientStatusRepository.findByPatientIdAndFacilityId(patientId,facilityId);
            if(patientStatus == null) {
                System.out.println("Error. No Patient with given Id checked in Facility " + medicalFacility.getFacilityId());
                return;
            }
            System.out.println("Patient is selected to be checked out\n");
            int p_id = patientId;
            Patient pt = patientRepository.findById(p_id);
            patientCheckoutService.checkout(pt,medicalFacility);
            patientStatus.setIsCheckout(true);
            try{
                patientStatusRepository.save(patientStatus);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Failed to checkout patient " + e.getMessage());
                return;
            }
            System.out.println("Patient is checked out successfully");
        }
        if(action == 2){
           return;
        }
    }

    public void enterVitalData(Staff staff,MedicalFacility medicalFacility) throws Exception{

        System.out.println("Staff Enter Patient Vital Data Menu\n" +
                "1. Record Vitals\n" +
                "2. Go back");
        int action = Integer.parseInt(input.readLine());

        if(action == 2){
            return;
        }
        //getPatientVitalData(patientStatus);
        if(action == 1) {
            System.out.println("Select the patient from the list to record the vitals\n");
            Integer patientId = Integer.parseInt(input.readLine());
            Patient patient = em.find(Patient.class, patientId);
            while (patient == null){
                System.out.println("Invalid entry. Enter patient Id again");
                patientId = Integer.parseInt(input.readLine());
                patient = em.find(Patient.class, patientId);
            }
            Integer facilityId = medicalFacility.getFacilityId();
            PatientStatus patientStatus = patientStatusRepository.findByPatientIdAndFacilityId(patientId, facilityId);
            if(patientStatus == null){
                System.out.println("Patient " + patient.getFirstName()
                        + patient.getLastName() + " is not checked in this facility");
                return;
            }
            System.out.println("Enter the requested vital data for the patient");

            System.out.println("Enter Temperature\n");
            float temperature = Float.parseFloat(input.readLine());
            while( temperature < 97f || temperature > 106f){
                System.out.println("Invalid Entry\n" +
                        "Enter again");
                System.out.println("Enter Temperature\n");
                temperature = Float.parseFloat(input.readLine());
            }

            System.out.println("Enter Systolic Blood Pressure\n");
            int systolic_bp = Integer.parseInt(input.readLine());

            System.out.println("Enter Diastolic Blood Pressure\n");
            int diastolic_bp = Integer.parseInt(input.readLine());

            //Date date=java.util.Calendar.getInstance().getTime();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            patientStatus.setEndTime(timestamp);

            patientStatus.setTemperature(temperature);
            patientStatus.setLowBp(systolic_bp);
            patientStatus.setHighBp(diastolic_bp);
            patientStatus.setEndTime(timestamp);
            patientStatus.setPriorityStatus(2);

            try {
                patientStatusRepository.save(patientStatus);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to insert patient vitals into DB" + e.getMessage());
                return;
            }
            System.out.println("Successfully entered patient vitals");
            System.out.println("Redirecting to Staff Process Patient Menu\n");
        }
    }

}