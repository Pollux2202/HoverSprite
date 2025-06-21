package com.example.demo.Feedback.FeedbackDTO;

import com.example.demo.Feedback.Feedback;

public class FeedbackResponse {
    private Long orderId;
    private String farmerEmail;
    private String description;
    private int attentiveRating;
    private int friendlyRating;
    private int professionalRating;


    public FeedbackResponse(Feedback feedback) {
        this.orderId = feedback.getSprayOrder().getSprayId();
        this.farmerEmail = feedback.getFarmer().getEmail();
        this.description = feedback.getFeedbackText();
        this.attentiveRating = feedback.getAttentiveRating();
        this.friendlyRating = feedback.getFriendlyRating();
        this.professionalRating = feedback.getProfessionalRating();
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttentiveRating() {
        return attentiveRating;
    }

    public void setAttentiveRating(int attentiveRating) {
        this.attentiveRating = attentiveRating;
    }

    public int getFriendlyRating() {
        return friendlyRating;
    }

    public void setFriendlyRating(int friendlyRating) {
        this.friendlyRating = friendlyRating;
    }

    public int getProfessionalRating() {
        return professionalRating;
    }

    public void setProfessionalRating(int professionalRating) {
        this.professionalRating = professionalRating;
    }
}
