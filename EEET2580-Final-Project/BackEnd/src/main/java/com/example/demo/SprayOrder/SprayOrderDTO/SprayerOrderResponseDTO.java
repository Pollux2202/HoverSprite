package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.enumType.CropType;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SprayOrder.enumType.PaymentType;
import com.example.demo.SpraySession.enumType.CalenderType;

import java.time.LocalDate;

public class SprayerOrderResponseDTO {
    private Long sprayId;
    private String farmerEmail;
    private OrderStatus orderStatus;
    private CropType cropType;

    private String location;
    private LocalDate date;
    private CalenderType calenderType;
    private double totalCost;
    private PaymentType paymentType;

    public SprayerOrderResponseDTO(SprayOrder order) {
        this.sprayId = order.getSprayId();
        this.farmerEmail = order.getFarmer().getEmail();
        this.orderStatus = order.getOrderStatus();
        this.cropType = order.getCropType();
        this.paymentType = order.getPaymentType();
        this.location = order.getLocation();
        if(order.getCalenderType() == CalenderType.GREGORIAN){
            this.date = order.getSprayingSessions().getDate();
        } else {
            this.date = order.getSprayingSessions().getLunarDate();
        }
        this.calenderType = order.getCalenderType();
        this.totalCost = order.getTotalCost();
    }

    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CalenderType getCalenderType() {
        return calenderType;
    }

    public void setCalenderType(CalenderType calenderType) {
        this.calenderType = calenderType;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }
}
