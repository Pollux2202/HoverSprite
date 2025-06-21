package com.example.demo.User;

import com.example.demo.User.enumType.UserType;
import jakarta.persistence.*;


@MappedSuperclass
public class User {
    @Column (name = "first_name")
    private String firstName;

    @Column (name = "last_name")
    private String lastName;

    @Column (name = "password")
    private String password;

    @Column (name = "phone_number")
    private String phoneNumber;

    @Column (name = "email")
    private String email;

    @Column (name = "address")
    private String address;

    @Column (name = "user_type")
    private UserType userType;

    public User(){

    }

    public User(String firstName, String lastName, String password, String phoneNumber, String email, String address, UserType userType) {
        this.firstName = firstName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.userType = userType;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getUserFullName(){
        return lastName + " " + firstName;
    }
    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


}
