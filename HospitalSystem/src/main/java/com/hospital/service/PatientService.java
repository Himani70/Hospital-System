package com.hospital.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.model.MedicalFacility;
import com.hospital.model.Patient;
import com.hospital.model.Staff;
import com.hospital.repository.MedicalFacilityRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.StaffRepository;
import com.hospital.repository.StaffServiceDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientRoutingService patientRoutingService;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffMenuService staffMenuService;

    @PersistenceContext
    private EntityManager em;


    private ObjectMapper mapper = new ObjectMapper();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void signUp() throws ParseException, Exception {

        Patient patient = getPatientSignUpInput();

        System.out.println("Sign Up Page\n" +
                "1. Sign Up\n" +
                "2. Return Back\n");

        int action = Integer.parseInt(input.readLine());

        if(action == 2){
            return;
        }

        try {
            patientRepository.save(patient);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to persist patient in DB " + e.getMessage());
            return;
        }

        System.out.println("Successfully created Patient " + patient.getFirstName() + " " + patient.getLastName() + "\n");
        System.out.println("Redirecting to sign In Page\n");
        signIn();
    }

    public void signIn() throws Exception{

//        System.out.println("Sign In Page\n" +
//                "1. Sign In\n" +
//                "2. Return Back\n");
//        int action = Integer.parseInt(input.readLine());
//        if(action == 2){
//            return;
//        }
        System.out.println("Enter details to sign in");

        List<MedicalFacility> medicalFacilityList = medicalFacilityRepository.findAll();

        System.out.println("\nList of all Medical Facilities");
        for(MedicalFacility medicalFacility : medicalFacilityList) {
            System.out.println(medicalFacility.getFacilityId() + ". " + medicalFacility.getFacilityName());
        }
        SignInDetails signInDetails = getSignInInput();
        Patient patient = null;
        Staff staff = null;
        if(signInDetails.getIsPatient()) {
            patient = getSignInPatient(signInDetails);
            System.out.println("Sign In Page\n" +
                    "1. Sign In\n" +
                    "2. Return Back\n");
            int action = Integer.parseInt(input.readLine());
            if(action == 2){
                return;
            }
            while (patient == null) {
                System.out.println("Incorrect Sign in Details\n" +
                        " Enter Sign in details again\n");
                signInDetails = getSignInInput();
                patient = getSignInPatient(signInDetails);
            }
            System.out.println("Patient Sign In successful. Redirecting to Patient Routing\n");
            patientRoutingService.patientRoutingMenu(patient, signInDetails.getFacilityId());
        } else {
            staff = getSignInStaff(signInDetails);
            System.out.println("Sign In Page\n" +
                    "1. Sign In\n" +
                    "2. Return Back\n");
            int action = Integer.parseInt(input.readLine());
            if(action == 2){
                return;
            }
            while (staff == null) {
                System.out.println("Incorrect Sign in Details\n" +
                        " Enter Sign in details again\n");
                signInDetails = getSignInInput();
                staff = getSignInStaff(signInDetails);
            }
            System.out.println("Staff Sign In successful. Redirecting to Staff Menu\n");
            MedicalFacility medicalFacility = em.find(MedicalFacility.class, signInDetails.getFacilityId());
            staffMenuService.StaffMenu(staff, medicalFacility);
        }

    }


    private Staff getSignInStaff(SignInDetails signInDetails) {
        Staff staff = staffRepository.findByLastNameAndCityAndDob(
                signInDetails.getLastName(),
                signInDetails.getCity() ,
                signInDetails.getDateOfBirth()
        );
        return staff;
    }


    private Patient getSignInPatient(SignInDetails signInDetails) {
        Patient patient = patientRepository.findByLastNameAndCityAndDateOfBirth(
                signInDetails.getLastName(),
                signInDetails.getCity() ,
                signInDetails.getDateOfBirth()
        );
        return patient;
    }

    private SignInDetails getSignInInput() throws Exception{
        System.out.println("Sign In User... Enter following details\n");

        System.out.println("Enter Facitily Id to check in");
        Integer facilityId = Integer.valueOf(input.readLine());

        System.out.println("Enter Last Name");
        String lastName = input.readLine();

        System.out.println("Enter DOB in format dd-MM-yyyy");
        String dob = input.readLine();
        Date date = null;
        try{
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error in parsing date" + e.getMessage());
            return null;
        }

        System.out.println("Enter city");
        String city = input.readLine();
        System.out.println("Enter yes or no if user is patient");
        Boolean isPatient = input.readLine().equalsIgnoreCase("yes") ? true : false;

        SignInDetails signInDetails = SignInDetails.builder()
                .city(city)
                .dateOfBirth(date)
                .facilityId(facilityId)
                .lastName(lastName)
                .isPatient(isPatient)
                .build();
        return signInDetails;
    }

    private Patient getPatientSignUpInput() throws Exception{
        System.out.println("Sign Up Patient... Enter following details\n");

        System.out.println("Enter First Name");
        String firstName = input.readLine();
        firstName = firstName.isEmpty() ? null : firstName;

        System.out.println("Enter Last Name");
        String lastName = input.readLine();
        lastName = lastName.isEmpty() ? null : lastName;

        System.out.println("Enter DOB in format dd-MM-yyyy");
        String dob = input.readLine();
        dob = dob.isEmpty() ? null : dob;
        Date date = null;
        try{
           DateFormat simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error in parsing date" + e.getMessage());
            return null;
        }

        System.out.println("Enter phone Number");
        String phone = input.readLine();

        System.out.println("Enter House Number");
        String house = input.readLine();

        System.out.println("Enter street Name");
        String street = input.readLine();

        System.out.println("Enter city");
        String city = input.readLine();
        city = city.isEmpty() ? null : city;

        System.out.println("Enter state Name");
        String state = input.readLine();
        state = state.isEmpty() ? null : state;

        System.out.println("Enter Country");
        String country = input.readLine();
        country = country.isEmpty() ? null : country;

        Patient patient = Patient.builder()
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(date)
                .phone(phone)
                .city(city)
                .country(country)
                .houseNumber(house)
                .stateName(state)
                .streetName(street)
                .build();
        return patient;

    }
}
