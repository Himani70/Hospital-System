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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SymptomSevice {


    @Autowired
    private SymptomRepository symptomRepository;
    @Autowired
    private BodyPartRepository bodyPartRepository;
    @Autowired
    private SymptomBodyPartRepository symptomBodyPartRepository;
    @Autowired
    private SeverityRepository severityRepository;
    @Autowired
    private SeverityScaleRepository severityScaleRepository;
    @Autowired
    private PatientSymptomDetailRepository patientSymptomDetailRepository;
    @Autowired
    private OtherSymptomRepository otherSymptomRepository;

    @PersistenceContext
    private EntityManager em;

    private ObjectMapper mapper = new ObjectMapper();
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void getSymptomMetaDataFromUser(PatientStatus patientStatus) throws IOException {
        int idx = showSymtpomsList();
        System.out.println("Enter id for symptom");
        int in = Integer.parseInt(input.readLine());

        while (in != idx+2) {
            if(in == idx + 1){
                //code to get other symptom
                System.out.println("Enter details for Other symptom");
                String otherSymptomDetail = input.readLine();
                OtherSymptom otherSymptom = OtherSymptom.builder()
                        .symptomDetail(otherSymptomDetail)
                        .patientStatusId(patientStatus.getId())
                        .build();
                otherSymptomRepository.save(otherSymptom);
            } else {
                Symptom symptom = em.find(Symptom.class, in);
                PatientSymptomDetail symptomDetail = getSymptomDataInput(symptom, patientStatus);
                try {
                    patientSymptomDetailRepository.save(symptomDetail);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to persist patient in DB " + e.getMessage());
                    return;
                }
            }
            idx = showSymtpomsList();
            System.out.println("Enter another input from the above list");
            in = Integer.parseInt(input.readLine());
        }
        return;
    }

    private PatientSymptomDetail getSymptomDataInput(Symptom symptom, PatientStatus patientStatus) throws IOException {
        System.out.println("Enter Symptom Related detail for the select symptom " + symptom.getSymtomName() +"\n");
        List<SymptomBodyPart> symptomBodyPartList = symptomBodyPartRepository.findBySymptomId(symptom.getId());
        Integer bodyPartId = null;
        if(symptomBodyPartList != null && !symptomBodyPartList.isEmpty()){
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
            } else {
                System.out.println("Enter body part id for this symptom for above list");
                bodyPartId = Integer.valueOf(input.readLine());
                BodyPart bp = em.find(BodyPart.class, bodyPartId);
                while(bp == null) {
                    System.out.println("Invalid body part entered. Enter again...");
                    bodyPartId = Integer.valueOf(input.readLine());
                    bp = em.find(BodyPart.class, bodyPartId);
                }
            }
        } else {
            List<BodyPart> bodyPartList = bodyPartRepository.findAll();
            System.out.println("\n\n");
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
        }

        System.out.println("(Duration) Enter the number of days for symptom is observed");
        Integer duration = Integer.valueOf(input.readLine());

        Boolean isReoccuring = false;
        System.out.println("Is symptom reoccuring... Enter yes or No");
        String reoccur = input.readLine();
        if(reoccur.equalsIgnoreCase("yes")){
            isReoccuring = true;
        }

        List<SeverityScale> severityScaleList = severityScaleRepository.findBySymptomId(symptom.getId());
        List<Severity> severityList = new ArrayList<>();
        if(!severityScaleList.isEmpty()) {
            List<Integer> severityIdList = new ArrayList<>();
            for (SeverityScale severitydetail : severityScaleList) {
                severityIdList.add(severitydetail.getSeverityId());
            }
            severityList = severityRepository.findByIdIn(severityIdList);
        } else {
            System.out.println("Symptom does not have associated severity. Select new severity for this symptom");
            severityList = severityRepository.findAll();
        }
        for (Severity severitydetail : severityList) {
            System.out.println(severitydetail.getId() + ". " + severitydetail.getSeverityValue());
        }
        System.out.println("Enter severity Id for the symptom");
        Integer severityId = Integer.valueOf(input.readLine());
        Severity severity = em.find(Severity.class, severityId);

        SeverityScale severityScale = severityScaleRepository.findBySeverityIdAndSymptomId(severity.getId(), symptom.getId());
        if(severityScale == null) {
            severityScale = SeverityScale.builder()
                    .symptomId(symptom.getId())
                    .severityId(severityId)
                    .build();
            try{
                severityScaleRepository.save(severityScale);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Enter cause for the symptom");
        String cause = input.readLine();

        PatientSymptomDetail symptomDetail = PatientSymptomDetail.builder()
                                        .bodyPartId(bodyPartId)
                                        .symptomId(symptom.getId())
                                        .duration(duration)
                                        .patientStatusId(patientStatus.getId())
                                        .isOccuring(isReoccuring)
                                        .severityScaleId(severityScale.getId())
                                        .cause(cause)
                                        .build();

        return symptomDetail;

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
        System.out.println(otherIdx + "." + " Other");
        int doneIdx  = idx + 2;
        System.out.println(doneIdx + "." + " Done");
        return idx;
    }

    public void addNewSymptom() throws IOException {

        System.out.println("Adding new symptom details\n");
        System.out.println("Enter Symptom name");
        String symptomName = input.readLine();
        String symtomCode = "SYM-"+symptomName;

        BodyPart bodyPart = getBodyPartForNewSymptom();
        List<String> severityIdList = getSeverityForNewSymptom();

        System.out.println("Add Symptom Menu\n" +
                "1. Record\n" +
                "2. Go Back");
        int in = Integer.parseInt(input.readLine());
        if(in == 2){
            return;
        }
        Symptom symptom = saveNewSymptom(symptomName,symtomCode);
        if(bodyPart != null) {
            SymptomBodyPart symptomBodyPart = SymptomBodyPart.builder()
                    .partId(bodyPart.getId())
                    .symptomId(symptom.getId())
                    .build();
            try{
                symptomBodyPartRepository.save(symptomBodyPart);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while saving body part for symptom" + e.getMessage());
                return;
            }
        }

        if(!severityIdList.isEmpty()) {
            for(String severityId : severityIdList) {
                saveSeverityForSymptom(Integer.valueOf(severityId),symptom.getId());
            }
        }
        System.out.println("Successfully added new symptom in system\n");
    }

    private void saveSeverityForSymptom(Integer severityId, Integer symptomId) {
        SeverityScale severityScale = SeverityScale.builder()
                .severityId(severityId)
                .symptomId(symptomId)
                .build();
        try{
            severityScaleRepository.save(severityScale);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while saving severity for symptom " + e.getMessage());
            return;
        }
    }

    private List<String> getSeverityForNewSymptom() throws IOException {
        System.out.println("List of severity");
        List<Severity> list = severityRepository.findAll();
        int idx = 0;
        for(Severity severity : list){
            idx = severity.getId();
            System.out.println(severity.getId() + ". " + severity.getSeverityValue());
        }
        System.out.println(idx+1 + ". None");
        System.out.println("Enter comma seperated id for all the severity associated with symptom." +
                " Select None if no associated value of severity.");
        List<String> idList = Arrays.asList(input.readLine().split("\\s*,\\s*"));
        if(Integer.parseInt(idList.get(0)) == idx+1){
            return new ArrayList<String>();
        }
        return idList;
    }

    private Symptom saveNewSymptom(String symptomName, String symtomCode) {
        Symptom symptom = Symptom.builder()
                .symptomCode(symtomCode)
                .symtomName(symptomName)
                .build();
        try{
            symptomRepository.save(symptom);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while saving new symptom in database" + e.getMessage());
            return null;
        }
        return symptom;
    }

    private BodyPart getBodyPartForNewSymptom() throws IOException {
        List<BodyPart> bodyPartList = bodyPartRepository.findAll();
        System.out.println("List of all body part");
        int idx = 0;
        for(BodyPart bodyPart : bodyPartList){
            idx = bodyPart.getId();
            System.out.println(bodyPart.getId() +". "+ bodyPart.getBodyPartName());
        }
        System.out.println(idx+1 +". None");
        System.out.println("Select body part Id associated with symptom. Select none for no specific body part");
        int bodyPartId = Integer.parseInt(input.readLine());
        if(bodyPartId == idx+1) {
            return null;
        }
        BodyPart bodyPart = em.find(BodyPart.class,bodyPartId);
        return bodyPart;
    }
}
