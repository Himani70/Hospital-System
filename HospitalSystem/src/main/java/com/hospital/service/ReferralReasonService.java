package com.hospital.service;

import com.hospital.model.*;
import com.hospital.repository.ReasonRepository;
import com.hospital.repository.ReferralReasonRepository;
import com.hospital.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
class ReferralReasonService {
    @Autowired
    private
    ServiceRepository serviceRepository;
    @Autowired
    private
    ReferralReasonRepository referralReasonRepository;
    @Autowired
    private ReasonRepository reasonRepository;

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private List<Integer> reasons = new ArrayList<Integer>();
    private List<String> services = new ArrayList<String>();
    private List<String> description = new ArrayList<String>();
    int read = 0;
    int count = 0;
    String serv = "No service was given!";

    void reasons() throws IOException {
        read = 0;
        count = 0;
        reasons = new ArrayList<Integer>();
        services = new ArrayList<String>();
        description = new ArrayList<String>();
        serv = "No service was given!";
        while(count != 4 ) {
            if(count <= 3 ) {
                System.out.println();
                Reason r = null;
                System.out.println("Add Reason");
                List<Reason> reasonsList = reasonRepository.findAll();
                for (Reason reasonDetail : reasonsList) {
                    System.out.println( reasonDetail.getReasonCode()
                            + ". " + reasonDetail.getReasonName());
                }
                System.out.println("Please select any reason code from above or enter 0 to exit");
                read = Integer.parseInt(input.readLine());
                if (read == 0) return;
                r = reasonRepository.findByReasonCode(read);
                while(r == null) {
                    System.out.println("Reason entered is not correct. Please enter correct reason from list above");
                    read = Integer.parseInt(input.readLine());
                    r = reasonRepository.findByReasonCode(read);
                }
                reasons.add(read);
                count++;
                com.hospital.model.Service serviceTest;
                System.out.println("1. Please give service name from below");
                List<com.hospital.model.Service> serviceList = serviceRepository.findAll();
                for (com.hospital.model.Service s : serviceList) {
                    System.out.println(s.getServiceName());
                }
                System.out.println("2. Write your own ");
                System.out.println("Choose input 1 or 2");
                int in = Integer.parseInt(input.readLine());
                if (in == 1) {
                    System.out.println("Please write the exact service name as mentioned in list");
                    serv = input.readLine();
                    serviceTest = serviceRepository.findByServiceName(serv);
                    while(serviceTest == null ) {
                        System.out.println("Please write name exactly mentioned in list");
                        serv = input.readLine();
                        serviceTest = serviceRepository.findByServiceName(serv);
                    }
                }
                if (in == 2 ) {
                    System.out.println("Please give more details about service");
                    serv = input.readLine();
                }
                services.add(serv);
                System.out.println();
                System.out.println("Please give more details for referral: ");
                description.add(input.readLine());
                System.out.println();
            }
            else
            {
                System.out.println("You have already gave 4 reasons, You cannot add more than 4");
                return;
            }
        }
    }

    void referralReasonSave(int referral_id) {
        Integer[] rcode = reasons.toArray(new Integer[reasons.size()]);
        String[] snums = services.toArray(new String[services.size()]);
        String[] det = description.toArray(new String[description.size()]);
        ReferralReason[] rr = new ReferralReason[rcode.length];
        for(int i=0; i<rcode.length ; i++) {
             rr[i] = ReferralReason.builder()
                    .reasonCode(rcode[i])
                    .referralId(referral_id)
                    .description(det[i])
                     .serviceName(snums[i])
                    .build();
            try {
               referralReasonRepository.save(rr[i]);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save Reasons in DB " + e.getMessage());
            }

        }

        read = 0;
        count = 0;
        rcode = null;
        snums = null;
        det = null;
    }


    void reasonsDisplayWithoutSave() {
        Integer[] rcode = reasons.toArray(new Integer[reasons.size()]);
        String[] snums = services.toArray(new String[services.size()]);
        String[] det = description.toArray(new String[description.size()]);
        System.out.println("Referral Reasons: ");
        for(int i=0 ; i<rcode.length; i++) {
            System.out.println(i + 1 + ". " + reasonRepository.findByReasonCode(rcode[i]).getReasonName() +
                        ", Service " + snums[i] +
                        " and description is " + det[i]);
            }
        }

    public void reasonsDisplayAfterSave(Referral ref) {
        List<ReferralReason> refList = referralReasonRepository.findByReferralId(ref.getId());
        Reason res;
        System.out.println("Reasons");
        for(ReferralReason rr : refList) {
                System.out.println(rr.getReasonCode() + " " +
                        reasonRepository.findByReasonCode(rr.getReasonCode()).getReasonName() + "," +
                        "Service: " + rr.getServiceName() +
                        " description was: " + rr.getDescription());

            }
        }
}
