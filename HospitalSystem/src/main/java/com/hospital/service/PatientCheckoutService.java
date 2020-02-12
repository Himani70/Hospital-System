        package com.hospital.service;


        import com.hospital.model.*;
        import com.hospital.repository.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.sql.Timestamp;
        import java.util.List;

        @Service
        public class PatientCheckoutService {

            @Autowired
            PatientCheckoutRepository patientCheckoutRepository;
            @Autowired
            ReferralService referralService;
            @Autowired
            ReferralReasonService referralReasonService;
            @Autowired
            NegativeExperienceRepository negativeExperienceRepository;

            String discharge_status = null;
            String treatment = null;
            int p_id = 0;
            int f_id = 0;
            int ref_id = 0;
            Integer experience = 0;
            PatientCheckout pc2 = null, pc3 = null;

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            public void checkout(Patient pt, MedicalFacility ft) throws Exception {

                discharge_status = null;
                treatment = null;
                p_id = f_id = ref_id = 0;
                pc2 = pc3 = null;
                experience = 0;
                System.out.println();
                System.out.println("Checkout Page\n" +
                        "1. Discharge Status\n" +
                        "2. Referral Status\n" +
                        "3. Treatment\n" +
                        "4. Negative Experience\n" +
                        "5. Go Back\n" +
                        "6. Submit\n");

                int in = Integer.parseInt(input.readLine());
                while (in != 5) {
                    switch (in) {
                        case 1:
                            discharge_status(pt, ft);
                            System.out.println(" Discharge status " + discharge_status + " has been set for " + pt.getFirstName());
                            break;
                        case 2:
                            if (discharge_status.equalsIgnoreCase("referred"))
                                referralService.referrals(pt);
                            else
                                System.out.println("To refer the patient, please set discharge status to referral first");
                            break;
                        case 3:
                            treatment(pt, ft);
                            break;
                        case 4:
                            negative_experience(pt, ft);
                            break;
                        case 6:
                            report(pt, ft);
                            return;
                        default:
                            System.out.println(" Invalid input try again...");
                            break;
                    }
                    System.out.println("Checkout Page\n" +
                            "1. Discharge Status\n" +
                            "2. Referral Status\n" +
                            "3. Treatment\n" +
                            "4. Negative Experience\n" +
                            "5. Go Back\n" +
                            "6. Submit\n");

                    in = Integer.parseInt(input.readLine());
                }
            }

            private void discharge_status(Patient pt, MedicalFacility ft) throws Exception {
                System.out.println("Discharge Status Page\n" +
                        "1. Successful\n" +
                        "2. Deceased\n" +
                        "3. Referred\n" +
                        "4. Go Back\n");
                int in = Integer.parseInt(input.readLine());
                if(in == 1) {
                    discharge_status = "Successful";
                    return;
                } else if(in == 2) {
                    discharge_status = "Deceased";
                    return;
                } else if(in == 3) {
                    discharge_status = "Referred";
                    referralService.referrals(pt);
                    return;
                } else if(in == 4) {
                    return;
                }
                else {
                    System.out.println("Please enter from options given in menu only");
                    discharge_status(pt,ft);
                }
            }

        private void treatment(Patient pt, MedicalFacility ft) throws Exception {
            System.out.println("Treatment page!!\n" + "Please Enter treatment Details");
            treatment = input.readLine();
            if (treatment != null) {
                System.out.println("Treatment " + treatment +
                        " has been set successfully for patient " +
                        pt.getFirstName());
            }
            else
            {
                System.out.println("treatment cannot be empty");
                treatment(pt,ft);
            }
        }

        private void negative_experience(Patient pt, MedicalFacility ft) throws Exception {
            NegativeExperience neg = null;
            System.out.println("Negative Experience");

            List<NegativeExperience> experienceList = negativeExperienceRepository.findAll();
            for(NegativeExperience ne: experienceList) {
                System.out.println(ne.getExperienceId()+ ". " + ne.getDescription());
            }
            Integer in = Integer.parseInt(input.readLine());
            neg = negativeExperienceRepository.findByExperienceId(in);
            while(neg == null) {
                System.out.println("Please enter negative experience mentioned in the list only");
                in = Integer.parseInt(input.readLine());
                neg = negativeExperienceRepository.findByExperienceId(in);
            }
            experience = in;
        }

        private void pc_save(Patient pt, MedicalFacility ft, int ref_id) throws Exception {
        if (discharge_status == null ) {
            System.out.println("Discharge status cannot be null. Please fill discharge details");
            discharge_status(pt,ft);
        }
        if (treatment == null) {
            System.out.println("Treatment cannot be null. Please fill treatment details");
            treatment(pt,ft);
        }

        if((!discharge_status.equalsIgnoreCase("referred")) && (experience == 0) ){
            p_id = pt.getId();
            f_id = ft.getFacilityId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PatientCheckout pc = PatientCheckout.builder()
                    .patientId(p_id)
                    .dischargeStatus(discharge_status)
                    .treatmentGiven(treatment)
                    .facilityId(f_id)
                    .dischargeDate(timestamp)
                    .build();
            try {
                patientCheckoutRepository.save(pc);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save patient checkout in DB " + e.getMessage());
            }

        }else if(!discharge_status.equalsIgnoreCase("referred") && experience != 0  ) {
            p_id = pt.getId();
            f_id = ft.getFacilityId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PatientCheckout pc = PatientCheckout.builder()
                    .patientId(p_id)
                    .dischargeStatus(discharge_status)
                    .treatmentGiven(treatment)
                    .experienceId(experience)
                    .facilityId(f_id)
                    .dischargeDate(timestamp)
                    .build();
            try {
                patientCheckoutRepository.save(pc);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save patient checkout in DB " + e.getMessage());
            }

        } else if(discharge_status.equalsIgnoreCase("referred") && ref_id == 0 && experience == 0) {
            System.out.println("You have selected discharge status as referral, please enter referral details");
            referralService.referrals(pt);
            ref_id = referralService.referral_save();
            referralReasonService.referralReasonSave(ref_id);
            p_id = pt.getId();
            f_id = ft.getFacilityId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PatientCheckout pc = PatientCheckout.builder()
                    .patientId(p_id)
                    .dischargeStatus(discharge_status)
                    .treatmentGiven(treatment)
                    .referralId(ref_id)
                    .facilityId(f_id)
                    .dischargeDate(timestamp)
                    .build();
            try {
                patientCheckoutRepository.save(pc);
            } catch (Exception e) {
            e.printStackTrace();
                System.out.println("Failed to save patient checkout in DB " + e.getMessage());
            }
            return;
        } else if (discharge_status.equalsIgnoreCase("referred") && ref_id != 0 && experience == 0) {
            p_id = pt.getId();
            f_id = ft.getFacilityId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PatientCheckout pc = PatientCheckout.builder()
                    .patientId(p_id)
                    .dischargeStatus(discharge_status)
                    .treatmentGiven(treatment)
                    .referralId(ref_id)
                    .facilityId(f_id)
                    .dischargeDate(timestamp)
                    .build();
            try {
                patientCheckoutRepository.save(pc);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save patient checkout in DB " + e.getMessage());
            }

        }
        else
        {
            p_id = pt.getId();
            f_id = ft.getFacilityId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PatientCheckout pc = PatientCheckout.builder()
                    .patientId(p_id)
                    .dischargeStatus(discharge_status)
                    .treatmentGiven(treatment)
                    .experienceId(experience)
                    .referralId(ref_id)
                    .facilityId(f_id)
                    .dischargeDate(timestamp)
                    .build();
            try {
                patientCheckoutRepository.save(pc);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save patient checkout in DB " + e.getMessage());
            }

        }
            discharge_status = null;
            treatment = null;
            p_id = f_id = ref_id = 0;
        return;
        }

        private void report( Patient pt, MedicalFacility ft) throws Exception {

            System.out.println("See Below details of Patient " + pt.getFirstName() + " " + pt.getLastName() + " at" +
                    " Facility " + ft.getFacilityName());

            System.out.println("Patient Checkout Report");
            System.out.println();
            System.out.println("Discharge Status = " + discharge_status);

            if (discharge_status.equalsIgnoreCase("referred")) {
                referralService.referralDetailsWithoutSave();
            } else {
                System.out.println("Referral = None");
            }
            System.out.println("Treatment = " + treatment);
            if(experience != 0) {
            System.out.println("Negative Experience = " + negativeExperienceRepository.findByExperienceId(experience).getDescription());
            }
            else{
                System.out.println("Negative Experience = No negative experience mentioned");
            }
            System.out.println();
            System.out.println("Staff-Patient Report Confirmation\n" +
                    "1. Yes\n" +
                    "2. Go Back");

            int in = Integer.parseInt(input.readLine());

                if (in == 1) {
                    if (discharge_status.equalsIgnoreCase("referred")) {
                        ref_id = referralService.referral_save();
                        referralReasonService.referralReasonSave(ref_id);
                    }
                    pc_save(pt, ft, ref_id);
                    return;
                }
                if (in == 2) {
                    checkout(pt, ft);
                }
            }

            public void patientCheckoutAcknowledgment(Patient pt, MedicalFacility ft) throws IOException {
             pc3 = patientCheckoutRepository.findByPatientIdAndFacilityId(pt.getId(), ft.getFacilityId());
                System.out.println();
                System.out.println("Patient Report");
                System.out.println();
                System.out.println("Discharge Status = " + pc3.getDischargeStatus());
                if (pc3.getDischargeStatus().equalsIgnoreCase("referred")) {
                    referralService.referralDetailsAfterSave(pt);
                } else {
                    System.out.println("Referral = None");
                }
                System.out.println("Treatment = " + pc3.getTreatmentGiven());
                if(experience != 0)
                System.out.println("Negative Experience: " + negativeExperienceRepository.findByExperienceId(pc3.getExperienceId()).getDescription());
                else
                    System.out.println("Negative Experience = No negative experience mentioned");

                System.out.println();
                System.out.println("Patient Checkout Acknowledgment");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.println("3. Go Back");

                int in = Integer.parseInt(input.readLine());

                if(in == 1 || in == 3 ) { return ;}
                if(in == 2 ) {
                    System.out.println("Please give reason");
                    String reason = input.readLine();
                    pc3.setAcknowledgement(reason);
                    patientCheckoutRepository.save(pc3);
                    System.out.println("Acknowledgment is saved");
                    return;
                }
        }

        }

