package com.example.demo.User.Employee.Sprayer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface SprayerRepository extends JpaRepository<Sprayer, String> {
    Optional<Sprayer> findSprayerByPhoneNumber(String phoneNumber);
    Optional<Sprayer> findSprayerByEmail(String email);
    @Query("SELECT s FROM Sprayer s WHERE s.sId = :sId")
    Optional<Sprayer> findSprayerById(@Param("sId") String sId);

    List<Sprayer> findAllByEmailIn(List<String> emails);



}
