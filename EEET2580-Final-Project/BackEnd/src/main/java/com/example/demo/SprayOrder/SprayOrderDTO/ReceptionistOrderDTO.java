package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SpraySession.enumType.CalenderType;

import java.time.LocalDate;

public class ReceptionistOrderDTO {
    private Long sprayId;
    private String farmerEmail;
    private OrderStatus orderStatus;
    private LocalDate date;
    private CalenderType calenderType;
    private double totalCost;

    public ReceptionistOrderDTO(SprayOrder order) {
        this.sprayId = order.getSprayId();
        this.farmerEmail = order.getFarmer().getEmail();
        this.totalCost = order.getTotalCost();
        this.orderStatus = order.getOrderStatus();
        if (order.getCalenderType() == CalenderType.GREGORIAN){
            this.date = order.getSprayingSessions().getDate();
        } else {
            this.date = order.getSprayingSessions().getLunarDate();
        }
        this.calenderType = order.getCalenderType();
    }


    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

}
