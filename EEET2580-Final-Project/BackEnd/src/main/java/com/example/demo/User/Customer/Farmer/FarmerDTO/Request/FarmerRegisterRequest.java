package com.example.demo.User.Customer.Farmer.FarmerDTO.Request;

import com.example.demo.User.Customer.Farmer.Annotations.FieldConstraints.FarmerEmailConstraint;
import com.example.demo.User.Annotation.FieldConstraints.PasswordValidate;
import com.example.demo.User.Annotation.FieldConstraints.PhoneNumberConstraint;

public class FarmerRegisterRequest {

    private String firstName;
    private String lastName;
    @PasswordValidate
    private String password;
    @FarmerEmailConstraint
    private String email;
    @PhoneNumberConstraint
    private String phoneNumber;
    private String address;



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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
