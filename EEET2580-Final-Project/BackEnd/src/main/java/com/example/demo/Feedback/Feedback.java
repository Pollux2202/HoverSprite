package com.example.demo.Feedback;


import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.User.Customer.Farmer.Farmer;
import jakarta.persistence.*;
import lombok.Setter;


import java.time.LocalDateTime;



@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private SprayOrder sprayOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Farmer farmer;


    @Column(name = "attentive_rating",nullable = false)
    private int attentiveRating;

    @Column(name = "friendly_rating",nullable = false)
    private int friendlyRating;

    @Column(name = "professional_rating",nullable = false)
    private int professionalRating;


    @Column(columnDefinition = "TEXT")
    private String feedbackText;

    @Column( nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();





    public Feedback() {

    }

    public Feedback(Long id, SprayOrder sprayOrder, Farmer farmer, int attentiveRating, int friendlyRating,
                    int professionalRating, String feedbackText, LocalDateTime createdAt) {
        this.id = id;
        this.sprayOrder = sprayOrder;
        this.farmer = farmer;
        this.attentiveRating = attentiveRating;
        this.friendlyRating = friendlyRating;
        this.professionalRating = professionalRating;
        this.attentiveRating = attentiveRating;
        this.friendlyRating = friendlyRating;
        this.professionalRating = professionalRating;
        this.feedbackText = feedbackText;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SprayOrder getSprayOrder() {
        return sprayOrder;
    }

    public void setSprayOrder(SprayOrder sprayOrder) {
        this.sprayOrder = sprayOrder;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }



    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getAttentiveRating() {
        return attentiveRating;
    }

    public void setAttentiveRating(int attentiveRating) {
        if (attentiveRating < 1 || attentiveRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.attentiveRating = attentiveRating;
    }

    public int getFriendlyRating() {
        return friendlyRating;
    }

    public void setFriendlyRating(int friendlyRating) {
        if (friendlyRating < 1 || friendlyRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.friendlyRating = friendlyRating;
    }

    public int getProfessionalRating() {
        return professionalRating;
    }

    public void setProfessionalRating(int professionalRating) {
        if (professionalRating < 1 || professionalRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.professionalRating = professionalRating;
    }


}
