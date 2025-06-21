package com.example.demo.SpraySession.SpraySessionDTO;


import com.example.demo.SpraySession.SprayingSessions;
import com.example.demo.SpraySession.enumType.SubSessionType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class SpraySessionResponse {
    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private LocalDate lunarDate;
    private LocalTime startTime;
    private LocalTime endTime;


    public SpraySessionResponse(SprayingSessions sprayingSessions) {
        this.dayOfWeek = sprayingSessions.getDayOfWeek();
        this.date = sprayingSessions.getDate();
        this.lunarDate = sprayingSessions.getLunarDate();
        this.date = sprayingSessions.getDate();
        this.startTime = sprayingSessions.getStartTime();
        this.endTime = sprayingSessions.getEndTime();

    }


    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public LocalDate getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(LocalDate lunarDate) {
        this.lunarDate = lunarDate;
    }
}


