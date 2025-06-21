package com.example.demo.SpraySession;


import com.example.demo.SpraySession.Annotations.StartTimeException;
import com.example.demo.SpraySession.enumType.SubSessionType;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;



@Entity
@Table(name="time_slots")
public class SprayingSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weekday")

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    //date is Gregorian Date by default
    @Column(name = "gregorian_date")
    //date is Gregorian Date by default
    private LocalDate date;

    @Column(name = "lunar_date")
    private LocalDate lunarDate;



    @StartTimeException
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")

    private LocalTime endTime;

    @Column(name = "sub_session_1_booked")

    private boolean subSession1Booked;

    @Column(name = "sub_session_2_booked")

    private boolean subSession2Booked;

    public SprayingSessions() {}


    public SprayingSessions(Long id, DayOfWeek dayOfWeek, LocalDate date, LocalDate lunarDate, LocalTime startTime, LocalTime endTime, boolean subSession1Booked, boolean subSession2Booked) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.lunarDate=lunarDate;
        this.lunarDate=lunarDate;
        this.startTime = startTime;
        this.endTime = endTime;

        this.subSession1Booked = subSession1Booked;
        this.subSession2Booked = subSession2Booked;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
        return subSession1Booked;
    }

    public boolean isSubSession2Booked() {
        return subSession2Booked;
    }

    //Helper method
    public void bookSubSession1() {
        if (!subSession1Booked) {
            subSession1Booked = true;
        } else {
            throw new IllegalStateException("Sub-session 1 is already booked");
        }
    }

    public void bookSubSession2() {
        if (!subSession2Booked) {
            subSession2Booked = true;
        } else {
            throw new IllegalStateException("Sub-session 2 is already booked");
        }
    }

    // Method to check if the session is fully booked
//    public boolean isFullyBooked() {
//        return subSession1Booked && subSession2Booked;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubSession1Booked(boolean subSession1Booked) {
        this.subSession1Booked = subSession1Booked;
    }

    public void setSubSession2Booked(boolean subSession2Booked) {
        this.subSession2Booked = subSession2Booked;
    }





    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalDate getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(LocalDate lunarDate) {
        this.lunarDate = lunarDate;
    }
}



