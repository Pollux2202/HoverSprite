package com.example.demo.SpraySession;

import com.example.demo.SpraySession.SpraySessionDTO.SpraySessionResponse;
import com.example.demo.SpraySession.enumType.SubSessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SpraySessionsRepository extends JpaRepository<SprayingSessions, Long> {

    Optional<SprayingSessions> findByDateAndStartTimeAndEndTime(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );

    Optional<SprayingSessions> findByLunarDateAndStartTimeAndEndTime(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );

    List<SprayingSessions> findByDate(LocalDate date);

    List<SprayingSessions> findByLunarDate(LocalDate date);
}
