package com.example.demo.SpraySession;


import com.example.demo.SpraySession.SpraySessionDTO.GetSpraySessionResponse;

import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.SubSessionType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/spray-sessions")
public class SpraySessionController {

    @Autowired
    private SpraySessionsService spraySessionsService;

    @Autowired
    private SpraySessionsRepository spraySessionsRepository;

    @PostMapping("/create")
    public ResponseEntity<SprayingSessions> createSpraySession(@RequestBody @Valid SprayingSessions sprayingSessions) {
        SprayingSessions savedSession = spraySessionsService.save(sprayingSessions);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SprayingSessions> getSpraySession(@PathVariable Long id) {
        Optional<SprayingSessions> sprayingSession = spraySessionsService.getSpraySession(id);
        return sprayingSession.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping("/getDate")
    public ResponseEntity<List<GetSpraySessionResponse>> getSessionsByDateAndCalendarType(
            @RequestParam LocalDate date,
            @RequestParam CalenderType calendarType) {
        List<GetSpraySessionResponse> sessions = spraySessionsService.getSessionsByDateAndCalendarType(date, calendarType);
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}