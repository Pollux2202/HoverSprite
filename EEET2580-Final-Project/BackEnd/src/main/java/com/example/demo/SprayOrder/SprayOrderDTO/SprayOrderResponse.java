package com.example.demo.SprayOrder.SprayOrderDTO;

import com.example.demo.SprayOrder.enumType.CropType;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.enumType.PaymentType;

import com.example.demo.SpraySession.SpraySessionDTO.SpraySessionResponse;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.User.Customer.Farmer.FarmerDTO.Response.FarmerResponse;

import com.example.demo.User.Employee.Sprayer.SprayerDTO.SprayerResponse.SprayerResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SprayOrderResponse {

    private Long sprayId;
    private FarmerResponse farmer;
    private CropType cropType;
    private OrderStatus orderStatus;
    private double farmArea;
    private PaymentType paymentType;
    private LocalDateTime createdDateTime;
    private String location;
    private CalenderType calenderType;
    private SpraySessionResponse timeSlot;
    private double totalCost;
    private List<SprayerResponse> sprayers;

    private String stripeId;
    private String checkOutUrl;


    public SprayOrderResponse(SprayOrder sprayOrder) {
        this.sprayId = sprayOrder.getSprayId();

        this.farmer = new FarmerResponse(sprayOrder.getFarmer());
        this.cropType = sprayOrder.getCropType();
        this.orderStatus = sprayOrder.getOrderStatus();
        this.farmArea = sprayOrder.getFarmArea();
        this.paymentType = sprayOrder.getPaymentType();
        this.createdDateTime = sprayOrder.getCreatedDateTime();
        this.timeSlot = new SpraySessionResponse(sprayOrder.getSprayingSessions());
        this.totalCost = sprayOrder.getTotalCost();
        this.location = sprayOrder.getLocation();
        if (sprayOrder.getOrderStatus() == OrderStatus.ASSIGNED || sprayOrder.getOrderStatus() == OrderStatus.IN_PROGRESS || sprayOrder.getOrderStatus() == OrderStatus.COMPLETED) {
            this.sprayers = sprayOrder.getSprayers().stream()
                    .map(SprayerResponse::new)
                    .collect(Collectors.toList());
        } else {
            this.sprayers = null;  // Or you could initialize as an empty list: new ArrayList<>();
        }
        this.stripeId = sprayOrder.getStripeId();
        if (paymentType == PaymentType.CARD) {
            this.checkOutUrl = "https://checkout.stripe.com/c/pay/" + stripeId; // Generate URL if needed
        } else {
            this.checkOutUrl ="NO URL SINCE CARD PAYMENT IS NOT CHOSEN";
        }
    }

    // Getters and Setters

    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public String getLocation(){return this.location;}

    public void setLocation(String location){
        this.location = location;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getFarmArea() {
        return farmArea;
    }

    public void setFarmArea(double farmArea) {
        this.farmArea = farmArea;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }



    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public FarmerResponse getFarmer() {
        return farmer;
    }

    public void setFarmer(FarmerResponse farmer) {
        this.farmer = farmer;
    }



    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public SpraySessionResponse getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(SpraySessionResponse timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<SprayerResponse> getSprayers() {
        return sprayers;
    }

    public void setSprayers(List<SprayerResponse> sprayers) {
        this.sprayers = sprayers;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getCheckOutUrl() {
        return checkOutUrl;
    }

    public void setCheckOutUrl(String checkOutUrl) {
        this.checkOutUrl = checkOutUrl;
    }

    public CalenderType getCalenderType() {
        return calenderType;
    }

    public void setCalenderType(CalenderType calenderType) {
        this.calenderType = calenderType;
    }
}
