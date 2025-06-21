package com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse;

import com.example.demo.User.Employee.Sprayer.Sprayer;
import com.example.demo.User.Employee.Sprayer.enumType.SprayerExpertise;


public class SprayerResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private SprayerExpertise sprayerExpertise;

    public SprayerResponse(Sprayer sprayer) {
        this.firstName = sprayer.getFirstName();
        this.lastName = sprayer.getLastName();
        this.phoneNumber = sprayer.getPhoneNumber();
        this.email = sprayer.getEmail();
        this.address = sprayer.getAddress();
        this.sprayerExpertise = sprayer.getSprayerExpertise();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SprayerExpertise getSprayerExpertise() {
        return sprayerExpertise;
    }

    public void setSprayerExpertise(SprayerExpertise sprayerExpertise) {
        this.sprayerExpertise = sprayerExpertise;
    }
}
