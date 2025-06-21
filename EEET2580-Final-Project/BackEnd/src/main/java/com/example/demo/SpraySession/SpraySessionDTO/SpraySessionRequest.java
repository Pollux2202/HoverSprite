package com.example.demo.SpraySession.SpraySessionDTO;

import com.example.demo.SpraySession.enumType.SubSessionType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.demo.Security.Utils.DateUtils.parseDate;

public class SpraySessionRequest {

    private String date;

    private LocalTime startTime;

    private LocalTime endTime;



    public SpraySessionRequest() {}



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Method to convert date to LocalDate
    public LocalDate getParsedDate() {
        return parseDate(this.date);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    
}