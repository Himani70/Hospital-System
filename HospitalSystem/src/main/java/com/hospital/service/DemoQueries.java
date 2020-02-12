package com.hospital.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DemoQueries {


    @PersistenceContext
    private EntityManager em;

    private ObjectMapper mapper = new ObjectMapper();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void demoQueryMenu() throws IOException {

        System.out.println("1. Demo Query 1 : Find all patients that were discharged but had negative experiences at any facility,list their names" +
                "facility, check-in date, discharge date and negative experiences\n" +
                "2. Demo Query 2 : Find facilities that did not have a negative experience for a specific period (to be given \n" +
                "3. Demo Query 3 : For each facility, find the facility that is sends the most referrals to\n" +
                "4. Demo Query 4 : Find facilities that had no negative experience for patients with cardiac symptoms\n" +
                "5. Demo Query 5 : Find the facility with the most number of negative experiences (overall i.e. of either kind\n"+
                "6. Demo Query 6 : Find each facility, list the patient encounters with the top five longest check-in phases (i.e. time from" +
                "begin check-in to when treatment phase begins (list the name of patient, date, facility, duration and" +
                "list of symptoms\n" +
                "7. Return" );

        int in = Integer.valueOf(input.readLine());
        while(in != 7) {
                switch (in) {
                    case 1:
                        demo1();
                        break;
                    case 2:
                        demo2();
                        break;
                    case 3:
                        demo3();
                        break;
                    case 4:
                        demo4();
                        break;
                    case 5:
                        demo5();
                        break;
                    case 6:
                        demo6();
                        break;
                }
            System.out.println("1. Demo Query 1 : Find all patients that were discharged but had negative experiences at any facility,list their names" +
                    "facility, check-in date, discharge date and negative experiences\n" +
                    "2. Demo Query 2 : Find facilities that did not have a negative experience for a specific period (to be given \n" +
                    "3. Demo Query 3 : For each facility, find the facility that is sends the most referrals to\n" +
                    "4. Demo Query 4 : Find facilities that had no negative experience for patients with cardiac symptoms\n" +
                    "5. Demo Query 5 : Find the facility with the most number of negative experiences (overall i.e. of either kind\n"+
                    "6. Demo Query 6 : Find each facility, list the patient encounters with the top five longest check-in phases (i.e. time from" +
                    "begin check-in to when treatment phase begins (list the name of patient, date, facility, duration and" +
                    "list of symptoms\n" +
                    "7. Return" );

            in = Integer.valueOf(input.readLine());
        }
    }

    private void demo6() throws JsonProcessingException {

        List<Object[]> objectList = em.createNativeQuery("select mf.facility_id, mf.facility_name, concat(p.first_name,' ',p.last_name) as patient_full_name, date(ps.start_time) as checkedin_date, hour(timediff(ps.end_time,ps.start_time)) as checkin_duration, sy.sym_name from medical_facility mf,patient p,patient_status ps,symptom_detail sd,symptom sy where mf.facility_id = ps.facility_id and p.patient_id = ps.patient_id and ps.patient_status_id = sd.patient_status_id and sd.sym_id = sy.sym_id;").getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }

    private void demo5() throws JsonProcessingException {
        List<Object[]> objectList = em.createNativeQuery("select temp2.facility_id as " +
                "facility_id_with_max_number_of_negative_experiences from (select temp1.facility_id, max(temp1.mycount) " +
                "from(select temp.facility_id ,count(temp.facility_id) as mycount from (select facility_id from " +
                "patient_checkout where experience_id is not null) temp group by temp.facility_id)temp1)temp2")
                .getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }

    private void demo4() throws JsonProcessingException {
        List<Object[]> objectList = em.createNativeQuery("select distinct mf.facility_id,mf.facility_name " +
                "from medical_facility mf,patient_status ps, patient_checkout pc, symptom sy, " +
                "symptom_detail sd,body_part bp, symptom_body_part sbp where pc.patient_id =ps.patient_id " +
                "and pc.facility_id = ps.facility_id and ps.patient_status_id = sd.patient_status_id " +
                "and sd.sym_id = sy.sym_id and mf.facility_id = ps.facility_id and mf.facility_id = pc.facility_id " +
                "and sy.sym_id and sd.sym_id = sbp.sym_id and sbp.part_id = bp.part_id and pc.experience_id is not null " +
                "and bp.body_part_name = 'heart'").getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }

    private void demo3() throws JsonProcessingException {
        List<Object[]> objectList = em.createNativeQuery("select temp.checkedinfacilityid,temp.max_referredfacilityid" +
                " from(select pc.facility_id as checkedinfacilityid,rf.facility_id as  max_referredfacilityid, " +
                "count(rf.facility_id) as mycount from patient_checkout pc,referral rf where " +
                "rf.patient_id = pc.patient_id and pc.referral_id = rf.referral_id group by " +
                "pc.facility_id,rf.facility_id)temp group by temp.checkedinfacilityid having max(mycount)")
                .getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }

    private void demo2() throws IOException {
        System.out.println("Enter start date in format dd-MM-yyyy");
        Date startDate = getDate();
        System.out.println("Enter end date in format dd-MM-yyyy");
        Date endDate = getDate();

        List<Object[]> objectList = em.createNativeQuery("select distinct pc.facility_id " +
                "from patient_checkout pc where pc.experience_id is not null and " +
                "discharge_date is not null and date(pc.discharge_date) " +
                "between date(:start_date) and date(:end_date)")
                .setParameter("start_date", startDate).setParameter("end_date", endDate).getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }
    private Date getDate() throws IOException {
        String dob = input.readLine();
        dob = dob.isEmpty() ? null : dob;
        Date date = null;
        try{
            DateFormat simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(dob);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error in parsing date" + e.getMessage());
            return null;
        }
    }
    private void demo1() throws JsonProcessingException {
        List<Object[]> objectList = em.createNativeQuery("select distinct concat(p.first_name,' ',p.last_name) " +
                "as patient_full_name, mf.facility_name as facility_name, date(ps.start_time) as checkedIn_date, " +
                "date(pc.discharge_date) as discharge_date, ne.description as negative_experince " +
                "from patient p,medical_facility mf,patient_status ps, patient_checkout pc, negative_experience ne " +
                "where p.patient_id = ps.patient_id and ps.patient_id = pc.patient_id and mf.facility_id = ps.facility_id " +
                "and ps.facility_id = pc.facility_id and pc.experience_id = ne.experience_id and pc.experience_id is not null " +
                "and ps.is_checkout=1").getResultList();
        System.out.println("\n");
        for(Object o : objectList){
            System.out.println(mapper.writeValueAsString(o));
        }
        System.out.println("\n");
    }
}
