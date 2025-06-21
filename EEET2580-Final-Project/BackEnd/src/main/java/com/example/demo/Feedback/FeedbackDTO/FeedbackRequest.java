package com.example.demo.Feedback.FeedbackDTO;

public class FeedbackRequest {
    private Long orderId;
    private String description;
    private int attentiveRating;
    private int friendlyRating;
    private int professionalRating;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
