package com.example.demo.User.Customer.Farmer.FarmerDTO.Response;

import com.example.demo.User.Customer.Farmer.Farmer;

public class FarmerResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;


    public FarmerResponse(Farmer farmer) {

        this.firstName = farmer.getFirstName();
        this.lastName = farmer.getLastName();
        this.phoneNumber = farmer.getPhoneNumber();
        this.email = farmer.getEmail();
        this.address = farmer.getAddress();
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


}
