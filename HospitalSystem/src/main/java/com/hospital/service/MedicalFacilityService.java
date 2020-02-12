package com.hospital.service;

import com.hospital.model.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MedicalFacilityService {

    @Autowired
    MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private SeverityRepository severityRepository;
    @Autowired
    private SymptomRepository symptomRepository;
    @Autowired
    private SymptomBodyPartRepository symptomBodyPartRepository;
    @Autowired
    private BodyPartRepository bodyPartRepository;
    @Autowired
    private SeverityScaleRepository severityScaleRepository;
    @Autowired
    private RuleRepository ruleRepository;

    @PersistenceContext
    private EntityManager em;

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public List<MedicalFacility> getMedicalFacilityList(){
        return medicalFacilityRepository.findAll();
    }

    public void addSeverityMenu() throws IOException {
        System.out.println("Severity Menu\n" +
                "1. Add another level\n" +
                "2. No more levels\n" +
                "3. Go back");
        System.out.println("Enter value from menu");
        int in = Integer.parseInt(input.readLine());
        List<String> levels = new ArrayList<>();
        while(in != 3) {
            if(in == 1) {
                levels.add(addNewLevel());
            }
            else if (in == 2){
                for(String level : levels) {
                    saveSeverityValue(level);
                }
                System.out.println("Severity Level added. Redirecting to Staff Menu...");
                return;
            }
            System.out.println("Severity Menu\n" +
                    "1. Add another level\n" +
                    "2. No more levels\n" +
                    "3. Go back");
            System.out.println("Enter value from menu");
            in = Integer.parseInt(input.readLine());
        }
        System.out.println(" Redirecting to Staff Menu...");
    }

    private void saveSeverityValue(String level) {
        Severity severity = Severity.builder()
                .severityValue(level)
                .build();
        try{
            severityRepository.save(severity);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error occurred while saving severity level " + e.getMessage());
            return;
        }
    }

    private String addNewLevel() throws IOException {
        System.out.println("Enter level value");
        String value = input.readLine();
        return value;
    }

    public void addRule() throws IOException {
        int idx = showSymtpomsList();
        System.out.println("Enter id for symptom");
        int in = Integer.parseInt(input.readLine());
        //default priority id
        int priorityId = 2;
        List<Rule> list  = new ArrayList<>();
        Symptom symptom = null;
        while (in < idx+1) {
            symptom = em.find(Symptom.class, in);
            if(symptom != null) {
                BodyPart bodyPart = symptomBodyPartForNewRule(symptom);
                List<String> severityIdList = getSeverityForNewSymptom(symptom);
                for(String severityId : severityIdList) {
                    list.add(createRuleObject(symptom, bodyPart, Integer.valueOf(severityId)));
                }
            }
            idx = showSymtpomsList();
            System.out.println("Enter id for symptom");
            in = Integer.parseInt(input.readLine());
        }
        if(in == idx+1){
            priorityId = priorityMenu();
            for(Rule rule : list){
                saveRuleObject(rule, priorityId);
            }
            System.out.println("Successfully added rules. Redirecting to Staff Menu...");
        }

    }

    private void saveRuleObject(Rule rule, Integer priiorityId) {
        rule.setPriorityStatus(priiorityId);
        try{
            ruleRepository.save(rule);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while saving new rule " + e.getMessage());
        }
    }

    private int priorityMenu() throws IOException {

        System.out.println("Priority Menu\n" +
                "1. High\n" +
                "2. Normal\n" +
                "3. Quarantine");
        System.out.println("Enter priority value id for this rule");
        return Integer.parseInt(input.readLine());
    }

    private Rule createRuleObject(Symptom symptom, BodyPart bodyPart, Integer severityId) {
        Rule rule = Rule.builder()
                .partId(bodyPart.getId())
                .severityId(severityId)
                .symptomId(symptom.getId())
                .build();
        return rule;
    }

    private int showSymtpomsList() {
        List<Symptom> symptomList = symptomRepository.findAll();
        System.out.println("List of Symptoms");
        int idx = 0;
        for (Symptom symptomDetail : symptomList) {
            idx = symptomDetail.getId();
            System.out.println(symptomDetail.getId()+". " + symptomDetail.getSymtomName());
        }
        int otherIdx = idx + 1;
        System.out.println(otherIdx + "." + " Select Priority");
        return idx;
    }

    private BodyPart symptomBodyPartForNewRule(Symptom symptom) throws IOException {
        List<SymptomBodyPart> symptomBodyPartList = symptomBodyPartRepository.findBySymptomId(symptom.getId());
        Integer bodyPartId = null;
        if(symptomBodyPartList != null && !symptomBodyPartList.isEmpty()) {
            System.out.println("Body Part associated with symptom ");
            for(SymptomBodyPart symptomBodyPart : symptomBodyPartList) {
                BodyPart bodyPart = em.find(BodyPart.class, symptomBodyPart.getPartId());
                bodyPartId = bodyPart.getId();
                System.out.println(bodyPartId + ". " + bodyPart.getBodyPartName());
            }
            if(symptomBodyPartList.size() == 1) {
                BodyPart bodyPart = em.find(BodyPart.class, symptomBodyPartList.get(0).getPartId());
                bodyPartId = bodyPart.getId();
                System.out.println("Body Part associated with symptom " +
                        symptom.getSymtomName() + " is " + bodyPart.getBodyPartName()+"\n");
                return bodyPart;
            } else {
                System.out.println("Enter body part id for this symptom for above list");
                bodyPartId = Integer.valueOf(input.readLine());
                BodyPart bp = em.find(BodyPart.class, bodyPartId);
                while(bp == null) {
                    System.out.println("Invalid body part entered. Enter again...");
                    bodyPartId = Integer.valueOf(input.readLine());
                    bp = em.find(BodyPart.class, bodyPartId);
                }
                return bp;
            }
        } else {
            List<BodyPart> bodyPartList = bodyPartRepository.findAll();
            for(BodyPart bodyPart : bodyPartList){
                System.out.println(bodyPart.getId() + ". " + bodyPart.getBodyPartName());
            }
            System.out.println("Select body part id from the following list of body part");
            bodyPartId = Integer.valueOf(input.readLine());
            BodyPart bp = em.find(BodyPart.class, bodyPartId);
            while(bp == null) {
                System.out.println("Invalid body part entered. Enter again...");
                bodyPartId = Integer.valueOf(input.readLine());
                bp = em.find(BodyPart.class, bodyPartId);
            }
            return bp;
        }
    }

    private List<String> getSeverityForNewSymptom(Symptom symptom) throws IOException {
        System.out.println("List of severity for the symptom");
        List<SeverityScale> severityScaleList = severityScaleRepository.findBySymptomId(symptom.getId());
        List<Severity> severityList = new ArrayList<>();
        if(!severityScaleList.isEmpty()) {
            List<Integer> severityIdList = new ArrayList<>();
            for (SeverityScale severitydetail : severityScaleList) {
                severityIdList.add(severitydetail.getSeverityId());
            }
            severityList = severityRepository.findByIdIn(severityIdList);
        } else {
            severityList = severityRepository.findAll();
        }
        for(Severity severity : severityList){
            System.out.println(severity.getId() + ". " + severity.getSeverityValue());
        }
        System.out.println("Enter comma seperated id for all the severity associated with symptom for this rule.");
        List<String> idList = new ArrayList<>();
        try {
            idList = Arrays.asList(input.readLine().split("\\s*,\\s*"));
        }catch (Exception e){
            System.out.println("Error while entering values " + e.getMessage());
        }
        if(idList == null || idList.isEmpty()){
            getSeverityForNewSymptom(symptom);
        }
        return idList;
    }
}
