package com.example.demo.User.Employee.Sprayer;



import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderRepository;
import com.example.demo.User.Employee.Sprayer.Annotations.IdValidation.SprayerIdValidation;
import com.example.demo.User.Employee.Sprayer.enumType.SprayerExpertise;
import com.example.demo.User.User;

import com.example.demo.User.enumType.UserType;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "sprayer")
public class Sprayer extends User {

    @Id
    @SprayerIdValidation
    @Column(name = "sid")
    private String sId;

    @Column(name = "expertise")
    private SprayerExpertise sprayerExpertise;

    public Sprayer(){

    }


    public Sprayer(String firstName, String lastName, String password, String phoneNumber, String email, String address, UserType userType, String sId, SprayerExpertise sprayerExpertise) {
        super(firstName, lastName, password, phoneNumber, email, address, userType);
        this.sId = sId;
        this.sprayerExpertise = sprayerExpertise;
    }


    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public SprayerExpertise getSprayerExpertise() {
        return sprayerExpertise;
    }

    public void setSprayerExpertise(SprayerExpertise sprayerExpertise) {
        this.sprayerExpertise = sprayerExpertise;
    }

    public String getSprayerFullName(){ return getUserFullName();}


}
