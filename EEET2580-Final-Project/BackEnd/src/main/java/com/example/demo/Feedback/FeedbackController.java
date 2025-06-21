package com.example.demo.Feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Feedback.FeedbackDTO.FeedbackRequest;
import com.example.demo.Feedback.FeedbackDTO.FeedbackResponse;
import com.example.demo.Security.Filter.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private  FeedbackService feedbackService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping("/all")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks() {
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/submit")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @RequestBody FeedbackRequest feedbackRequest, HttpServletRequest request) {

        String farmerEmail = jwtAuthenticationFilter.extractEmailFromToken(request);
        FeedbackResponse feedbackResponse = feedbackService.submitFeedback(farmerEmail, feedbackRequest);
        return ResponseEntity.ok(feedbackResponse);
    }



}

