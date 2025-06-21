package com.example.demo.User.Employee.Receptionist.ReceptionistDTO.Response;

import com.example.demo.User.Employee.Receptionist.Receptionists;

public class ReceptionistContactDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public ReceptionistContactDTO(Receptionists receptionists) {
        this.firstName = receptionists.getFirstName();
        this.lastName = receptionists.getLastName();
        this.email = receptionists.getEmail();
        this.phoneNumber = receptionists.getPhoneNumber();
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
}
