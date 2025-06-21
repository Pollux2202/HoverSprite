package com.example.demo.User.Customer.Farmer;

import com.example.demo.User.Customer.Farmer.FarmerDTO.Response.FarmerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, String> {
    Optional<Farmer> findFarmerByEmail(String email);

    @Query("SELECT f FROM Farmer f WHERE f.fId = :fId")
    FarmerResponse findFarmerByFId(@Param("fId") String fId);

    Optional<Farmer> findFarmerByPhoneNumber(String phoneNumber);

}
