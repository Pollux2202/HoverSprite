package com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerRequest;

public class SprayerConfirmRequest {
 private Long sprayId;
 private double receivedAmount;

    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
}
