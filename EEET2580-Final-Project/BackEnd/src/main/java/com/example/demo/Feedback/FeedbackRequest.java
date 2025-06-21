package com.example.demo.Feedback;

public class FeedbackRequest {
    private Long orderId;
    private String farmerId;  // Updated to use Farmer's fId (String)
    private int attentiveRating;
    private int friendlyRating;
    private int professionalRating;
    private String feedbackText;

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
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

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }
}
