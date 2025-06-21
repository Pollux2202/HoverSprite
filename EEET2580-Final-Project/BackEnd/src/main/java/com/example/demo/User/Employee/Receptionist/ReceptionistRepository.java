package com.example.demo.User.Employee.Receptionist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionists, String> {
    Optional<Receptionists> findReceptionistByPhoneNumber(String phoneNumber);
    Optional<Receptionists> findReceptionistByEmail(String email);

    @Query("SELECT r FROM Receptionists r WHERE r.rId = :rId")
    Optional<Receptionists> findReceptionistById(@Param("rId") String rId);
    Optional<Receptionists> findByEmail(String email);
}
