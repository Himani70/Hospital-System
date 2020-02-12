package com.hospital.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.model.MedicalFacility;
import com.hospital.model.Patient;
import com.hospital.model.Staff;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hospital.model.Referral;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ReferralService {


    @Autowired
    ReferralReasonService referralReasonService;
    @Autowired
    ReferralRepository referralRepository;
    @Autowired
    MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    StaffRepository staffRepository;


    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    int p_id=0, f_id=0, ref_id=0;
    public void referrals(Patient pt) throws Exception {
        p_id = f_id = ref_id = 0;
        p_id = pt.getId();
        System.out.println("Referral Page");
        System.out.println("Please Enter Information");
        referredFacility();
        referrerId();
        referralReasonService.reasons();
    }


    public void referredFacility() throws IOException {
        MedicalFacility mf = null;
        List<MedicalFacility> medicalFacilityList = medicalFacilityRepository.findAll();
        System.out.println("List of all Medical Facilities");
        for(MedicalFacility medicalFacility : medicalFacilityList) {
            System.out.println(medicalFacility.getFacilityId() + ". " + medicalFacility.getFacilityName());
        }
        System.out.println("Give Facility ID  to which patient was referred or give 0 if no facility was referred");
        f_id = Integer.parseInt(input.readLine());
        if  (f_id == 0 ) {
            return;
        }
        mf = medicalFacilityRepository.findByFacilityId(f_id);
        while(mf == null) {
            System.out.println("Medical Facility invalid, Please enter from above list");
            f_id = Integer.parseInt(input.readLine());
            mf = medicalFacilityRepository.findByFacilityId(f_id);
        }

    }

    public void referrerId() throws IOException {
        Staff stf = null;
        System.out.println("Please enter referrer's ID");
        ref_id = Integer.parseInt(input.readLine());
        stf = staffRepository.findByStaffId(ref_id);
        while( stf == null) {
            System.out.println("This employee ID does not exist. Please enter correct ID");
            ref_id = Integer.parseInt(input.readLine());
            stf = staffRepository.findByStaffId(ref_id);
        }
    }
    public int referral_save() {

        Referral refer = Referral.builder()
                .patientId(p_id)
                .facilityId(f_id)
                .empId(ref_id)
                .build();

        try {
            referralRepository.save(refer);
            p_id = f_id = ref_id = 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save referral in DB " + e.getMessage());
        }
        return refer.getId();
    }

    public void referralDetailsWithoutSave(){
        System.out.println("Referral Details: \n" );
        if(f_id == 0) { System.out.println("Referred Medical Facility: None "); }
        else {
            System.out.println("Referred Medical Facility: " + medicalFacilityRepository.findByFacilityId(f_id).getFacilityName());
        }
        System.out.println("Referred by Employee: " + staffRepository.findByStaffId(ref_id).getFirstName() + " " + staffRepository.findByStaffId(ref_id).getLastName());
        referralReasonService.reasonsDisplayWithoutSave();
    }

        public void referralDetailsAfterSave(Patient pt) {
            List<Referral> refList = referralRepository.findByPatientId(pt.getId());
            for (Referral ref1 : refList) {
                if(ref1.getFacilityId() == 0 ) {
                    System.out.println("Referred Medical Facility: None ");
                }
                else {
                    System.out.println("Referred Medical Facility: " + medicalFacilityRepository.findByFacilityId(ref1.getFacilityId()).getFacilityName());
                }

                System.out.println("Referred by Employee: " + staffRepository.findByStaffId(ref1.getEmpId()).getFirstName() +
                       " " + staffRepository.findByStaffId(ref1.getEmpId()).getLastName());
                referralReasonService.reasonsDisplayAfterSave(ref1);
                System.out.println();
            }
        }
}
