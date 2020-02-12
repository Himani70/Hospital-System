package com.hospital;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.service.DemoQueries;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hospital.model.MedicalFacility;
import com.hospital.service.MedicalFacilityService;


@SpringBootApplication
@ComponentScan("com.hospital.service")
@EnableJpaRepositories(basePackages = "com.hospital.repository")
public class Hospital implements CommandLineRunner {

    private static String[] args;
    @Autowired
    private MedicalFacilityService medicalFacilityService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DemoQueries demoQueries;

    public static void main(String[] args) {
        System.out.println("Starting hospital Service....");

        Hospital.args = args;
        SpringApplication app = new SpringApplication(Hospital.class);

        app.run(Hospital.class, args);

//        SpringApplication.run(com.hospital.Hospital.class, args);
//        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
//        factory.autowireBean( hospital );
//        factory.initializeBean( hospital, "hospital" );

    }

    @Override
    public void run(String... args) throws Exception, SQLException {

        System.out.println("Home Page\n");

        System.out.println("1. Sign In\n" +
                "2. Sign Up Patient\n" +
                "3. Demo Queries\n" +
                "4. Exit");
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        while(input != 4 ) {
            try {
                switch (input) {
                    case 1:
                        patientService.signIn();
                        break;
                    case 2:
                        patientService.signUp();
                        break;
                    case 3:
                        demoQueries.demoQueryMenu();
                        break;
                    default:
                        System.out.println(" Invalid input try again...");
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Error occured due to " + e.getMessage());
            }
            System.out.println("Home Page\n");
            System.out.println("1. Sign In\n" +
                    "2. Sign Up Patient\n" +
                    "3. Demo Queries\n" +
                    "4. Exit");
            in = new Scanner(System.in);
            input = in.nextInt();
        }
    }
}
