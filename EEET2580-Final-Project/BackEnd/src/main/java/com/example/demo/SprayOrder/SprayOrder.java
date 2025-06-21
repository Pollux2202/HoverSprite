package com.example.demo.SprayOrder;

import com.example.demo.SprayOrder.enumType.CropType;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.SprayOrder.enumType.PaymentType;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.SubSessionType;
import com.example.demo.User.Employee.Sprayer.Sprayer;
import com.example.demo.User.Customer.Farmer.Farmer;
import com.example.demo.User.Employee.Receptionist.Receptionists;
import com.example.demo.SpraySession.SprayingSessions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "spray_orders")
public class SprayOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprayId;

    @ManyToOne
    @JoinColumn(name="receptionist_id")
    private Receptionists receptionist;

    @ManyToOne
    @JoinColumn(name="farmer_id")
    private Farmer farmer;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assigned_sprayer(s)",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "sprayer_id")
    )

    private List<Sprayer> sprayers;

    @Enumerated(EnumType.STRING)
    @Column(name="crop_type")
    private CropType cropType;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private OrderStatus orderStatus;

    @Column(name="farm_area")
    private double farmArea;

    @Column(name="created_date_time")
    private LocalDateTime createdDateTime;

    private String stripeId;

    @ManyToOne
    @JoinColumn(name="time_slot_id")
    private SprayingSessions sprayingSessions;

    @Column(name = "calender_type")
    private CalenderType calenderType;

    @Column
    private SubSessionType subSessionType;

    @Column (name="payment_type")
    private PaymentType paymentType;

    @Column(name="total")
    private double totalCost;

    @Column(name="location")
    private String location;

    @Column(name = "farmer_confirmation")
    private boolean farmerConfirmation;

    @Column(name = "sprayer_confirmation")
    private boolean sprayerConfirmation;

    @Column(name = "received_amount")
    private double receivedAmount;

    @Column(name = "change_amount")
    private double change;




    public SprayOrder(){

    }




    public SprayOrder(Long sprayId, Receptionists receptionist, Farmer farmer, List<Sprayer> sprayers, CropType cropType, OrderStatus orderStatus, double farmArea, LocalDateTime createdDateTime, SprayingSessions sprayingSessions, PaymentType paymentType, double totalCost, SubSessionType subSessionType, String stripeId, String location) {}

    public SprayOrder(Long sprayId, Receptionists receptionist, Farmer farmer,
                      List<Sprayer> sprayers, CropType cropType, OrderStatus orderStatus,
                      double farmArea, LocalDateTime createdDateTime, String stripeId,
                      SprayingSessions sprayingSessions,CalenderType calenderType, SubSessionType subSessionType,
                      PaymentType paymentType, double totalCost, boolean farmerConfirmation,
                      boolean sprayerConfirmation, double receivedAmount, double change, String location) {


        this.sprayId = sprayId;
        this.receptionist = receptionist;
        this.farmer = farmer;
        this.sprayers = sprayers;
        this.cropType = cropType;
        this.orderStatus = orderStatus;
        this.farmArea = farmArea;
        this.createdDateTime = createdDateTime;
        this.stripeId = stripeId;
        this.sprayingSessions = sprayingSessions;
        this.calenderType = calenderType;
        this.subSessionType = subSessionType;
        this.paymentType = paymentType;
        this.totalCost = totalCost;

        this.subSessionType = subSessionType;
        this.stripeId = stripeId;
        this.location = location;

        this.farmerConfirmation = farmerConfirmation;
        this.sprayerConfirmation = sprayerConfirmation;
        this.receivedAmount = receivedAmount;
        this.change = change;

    }


    // Method to get the week of the year for the order creation date
    public int getWeek() {
        // Use ISO standard or specific locale (e.g., Locale.getDefault())
        WeekFields weekFields = WeekFields.ISO; // You can use Locale-specific if needed: WeekFields.of(Locale.getDefault());
        return createdDateTime.get(weekFields.weekOfYear());
    }


    public Long getSprayId() {
        return sprayId;
    }

    public void setSprayId(Long sprayId) {
        this.sprayId = sprayId;
    }

    public Receptionists getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Receptionists receptionist) {
        this.receptionist = receptionist;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public List<Sprayer> getSprayers() {
        return sprayers;
    }

    public void setSprayers(List<Sprayer> sprayers) {
        this.sprayers = sprayers;
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

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public SprayingSessions getSprayingSessions() {
        return sprayingSessions;
    }

    public void setSprayingSessions(SprayingSessions sprayingSessions) {
        this.sprayingSessions = sprayingSessions;
    }

    public CalenderType getCalenderType() {
        return calenderType;
    }

    public void setCalenderType(CalenderType calenderType) {
        this.calenderType = calenderType;
    }

    public SubSessionType getSubSessionType() {
        return subSessionType;
    }

    public void setSubSessionType(SubSessionType subSessionType) {
        this.subSessionType = subSessionType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFarmerConfirmation() {
        return farmerConfirmation;
    }

    public void setFarmerConfirmation(boolean farmerConfirmation) {
        this.farmerConfirmation = farmerConfirmation;
    }

    public boolean isSprayerConfirmation() {
        return sprayerConfirmation;
    }

    public void setSprayerConfirmation(boolean sprayerConfirmation) {
        this.sprayerConfirmation = sprayerConfirmation;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
