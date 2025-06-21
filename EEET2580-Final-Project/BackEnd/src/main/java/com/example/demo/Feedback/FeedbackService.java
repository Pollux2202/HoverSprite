package com.example.demo.Feedback;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Feedback.FeedbackDTO.FeedbackRequest;
import com.example.demo.Feedback.FeedbackDTO.FeedbackResponse;
import com.example.demo.SprayOrder.SprayOrder;
import com.example.demo.SprayOrder.SprayOrderRepository;
import com.example.demo.SprayOrder.enumType.OrderStatus;
import com.example.demo.User.Customer.Farmer.Farmer;
import com.example.demo.User.Customer.Farmer.FarmerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SprayOrderRepository sprayOrderRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public List<FeedbackResponse> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();

        return feedbacks.stream()
                .map(this::convertToFeedbackResponse)
                .collect(Collectors.toList());
    }

    private FeedbackResponse convertToFeedbackResponse(Feedback feedback) {
        // Assuming FeedbackResponse has a constructor or setters to populate fields from Feedback
        return new FeedbackResponse(
                feedback
        );
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Feedback with id " + id + " not found."));
    }

    public FeedbackResponse submitFeedback(String farmerEmail, FeedbackRequest feedbackRequest) {
        SprayOrder sprayOrder = sprayOrderRepository.findById(feedbackRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Spray Order not found"));

        Farmer farmer = farmerRepository.findFarmerByEmail(farmerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found"));

        if(!sprayOrder.getOrderStatus().equals(OrderStatus.COMPLETED)){
            throw new IllegalStateException("The Order must be Completed to perform a Feedback");
        }

        if(!farmerEmail.equals(sprayOrder.getFarmer().getEmail())){
            throw new SecurityException("Farmer not match");
        }

        Feedback feedback = new Feedback();
        feedback.setSprayOrder(sprayOrder);
        feedback.setFarmer(farmer);
        feedback.setAttentiveRating(feedbackRequest.getAttentiveRating());
        feedback.setFeedbackText(feedbackRequest.getDescription());
        feedback.setFriendlyRating(feedbackRequest.getFriendlyRating());
        feedback.setProfessionalRating(feedbackRequest.getProfessionalRating());
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Returning a FeedbackResponse object
        return new FeedbackResponse(savedFeedback);
    }


    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Feedback with id " + id + " not found."));
        feedbackRepository.delete(feedback);
    }
}
