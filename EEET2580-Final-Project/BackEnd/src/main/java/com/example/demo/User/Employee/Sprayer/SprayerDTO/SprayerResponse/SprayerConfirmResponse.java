package com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse;

import com.example.demo.SprayOrder.SprayOrder;

public class SprayerConfirmResponse {
    private Long sprayId;
    private double receivedAmount;
    private double change;

    public SprayerConfirmResponse(SprayOrder sprayOrder) {
        this.sprayId = sprayOrder.getSprayId();
        this.receivedAmount = sprayOrder.getReceivedAmount();
        this.change = sprayOrder.getChange();
    }

    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
}
