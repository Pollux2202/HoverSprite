package com.example.demo.User.Customer.Farmer;


import com.example.demo.User.Customer.Farmer.Annotations.IdValidation.FarmerIdValidation;
import com.example.demo.User.User;
import com.example.demo.User.enumType.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "farmer")
public class Farmer extends User {

    @FarmerIdValidation
    @Id
    @Column(name = "fid")
    private String fId;


    public Farmer() {

    }

    public Farmer( String fId, String firstName,String lastName, String password, String phoneNumber, String email, String address, UserType userType) {

        super(firstName, lastName, password, phoneNumber, email, address, userType);
        this.fId = fId;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }
}
