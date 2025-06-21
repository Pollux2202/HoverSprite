package com.example.demo.Feedback;

import com.example.demo.Feedback.FeedbackDTO.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f WHERE f.sprayOrder.id = :orderId")
    List<FeedbackResponse> findBySprayOrderId(@Param("orderId") Long orderId);
}
