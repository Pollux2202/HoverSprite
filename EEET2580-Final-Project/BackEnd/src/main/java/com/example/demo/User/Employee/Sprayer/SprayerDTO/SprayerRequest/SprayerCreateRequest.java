package com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerRequest;

import com.example.demo.User.Annotation.FieldConstraints.PasswordValidate;
import com.example.demo.User.Annotation.FieldConstraints.PhoneNumberConstraint;
import com.example.demo.User.Employee.Annotations.FieldConstraints.EmployeeEmailConstraints;
import com.example.demo.User.Employee.Sprayer.enumType.SprayerExpertise;

public class SprayerCreateRequest {
    private String firstName;
    private String lastName;
    @PasswordValidate
    private String password;
    @PhoneNumberConstraint
    private String phoneNumber;
    @EmployeeEmailConstraints
    private String email;
    private String address;
    private SprayerExpertise sprayerExpertise;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
