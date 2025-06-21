package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.enumType.CropType;
import com.example.demo.SprayOrder.enumType.PaymentType;
import com.example.demo.SpraySession.SpraySessionDTO.SpraySessionRequest;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.SubSessionType;
import com.example.demo.User.Customer.Farmer.Farmer;

import jakarta.validation.constraints.NotNull;

public class SprayOrderRequest {

    private Farmer farmer;

    @NotNull
    private CropType cropType;

    @NotNull
    private double farmArea;

    @NotNull
    private String location;

    @NotNull
    private SpraySessionRequest timeSlot;

    private CalenderType calenderType;

    private SubSessionType subSessionType;

    private PaymentType paymentType;




    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(@NotNull String location) {
        this.location = location;
    }

    public double getFarmArea() {
        return farmArea;
    }

    public void setFarmArea(double farmArea) {
        this.farmArea = farmArea;
    }


    public @NotNull SpraySessionRequest getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(@NotNull SpraySessionRequest timeSlot) {
        this.timeSlot = timeSlot;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public SubSessionType getSubSessionType() {
        return subSessionType;
    }

    public void setSubSessionType(SubSessionType subSessionType) {
        this.subSessionType = subSessionType;
    }

    public CalenderType getCalenderType() {
        return calenderType;
    }

    public void setCalenderType(CalenderType calenderType) {
        this.calenderType = calenderType;
    }
}
