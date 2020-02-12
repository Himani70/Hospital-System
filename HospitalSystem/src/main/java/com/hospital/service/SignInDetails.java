package com.hospital.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class SignInDetails {

    private Integer facilityId;

    private Date dateOfBirth;

    private String lastName;

    private String city;

    private  Boolean isPatient;
}
