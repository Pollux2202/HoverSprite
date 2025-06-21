package com.example.demo.SprayOrder;

import com.example.demo.User.Employee.Sprayer.Sprayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprayOrderRepository extends JpaRepository<SprayOrder, Long> {
   // Find SprayOrder by stripeId
   Optional<SprayOrder> findByStripeId(String stripeId);
    // Additional query methods (if needed) can be defined here
    @Query("SELECT so FROM SprayOrder so " +
            "WHERE :sprayer MEMBER OF so.sprayers " +
            "AND FUNCTION('WEEK', so.createdDateTime) = :week " +
            "AND so.sprayerConfirmation = true")
    List<SprayOrder> findAcceptedOrdersBySprayerAndWeek(@Param("sprayer") Sprayer sprayer, @Param("week") int week);
    Page<SprayOrder> findByFarmerEmail(String farmerEmail, Pageable pageable);
    Page<SprayOrder> findBySprayersEmail(String email, Pageable pageable);

}
