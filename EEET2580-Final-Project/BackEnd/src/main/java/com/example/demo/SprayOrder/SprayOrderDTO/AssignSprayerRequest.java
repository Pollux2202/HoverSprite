package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.User.Employee.Sprayer.Sprayer;

import java.util.List;

public class AssignSprayerRequest {
    private Long sprayId;
    private List<String> sprayerEmails;



    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }


    public List<String> getSprayerEmails() {
        return sprayerEmails;
    }

    public void setSprayerEmails(List<String> sprayerEmails) {
        this.sprayerEmails = sprayerEmails;
    }
}
