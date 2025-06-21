package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.enumType.CropType;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SpraySession.enumType.CalenderType;

import java.time.LocalDate;

public class FarmerOrderResponseDTO {
    private Long sprayId;
    private double totalCost;
    private OrderStatus orderStatus;
    private CropType cropType;
    private LocalDate date;
    private CalenderType calenderType;

    public FarmerOrderResponseDTO(SprayOrder sprayOrder) {
        this.sprayId = sprayOrder.getSprayId();
        this.totalCost = sprayOrder.getTotalCost();
        this.orderStatus = sprayOrder.getOrderStatus();
        this.cropType = sprayOrder.getCropType();
        if (sprayOrder.getCalenderType() == CalenderType.GREGORIAN){
            this.date = sprayOrder.getSprayingSessions().getDate();
        } else {
            this.date = sprayOrder.getSprayingSessions().getLunarDate();
        }

        this.calenderType = sprayOrder.getCalenderType();
    }

    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
