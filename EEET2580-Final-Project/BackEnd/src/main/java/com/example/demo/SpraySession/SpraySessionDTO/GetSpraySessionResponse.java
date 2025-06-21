package com.example.demo.SpraySession.SpraySessionDTO;

import com.example.demo.SpraySession.SprayingSessions;

import java.time.LocalTime;

public class GetSpraySessionResponse {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isSubSession1Booked;
    private boolean isSubSession2Booked;

    public GetSpraySessionResponse(SprayingSessions sprayingSessions) {
        this.startTime = sprayingSessions.getStartTime();
        this.endTime = sprayingSessions.getEndTime();
        this.isSubSession1Booked = sprayingSessions.isSubSession1Booked();
        this.isSubSession2Booked = sprayingSessions.isSubSession2Booked();
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

    public boolean isSubSession1Booked() {
        return isSubSession1Booked;
    }

    public void setSubSession1Booked(boolean subSession1Booked) {
        isSubSession1Booked = subSession1Booked;
    }

    public boolean isSubSession2Booked() {
        return isSubSession2Booked;
    }

    public void setSubSession2Booked(boolean subSession2Booked) {
        isSubSession2Booked = subSession2Booked;
    }
}
