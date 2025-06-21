package com.example.demo.SpraySession;

import com.example.demo.SpraySession.SpraySessionDTO.GetSpraySessionResponse;
import com.example.demo.SpraySession.enumType.CalenderType;
import com.example.demo.SpraySession.enumType.SubSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpraySessionsService {

    @Autowired
    private SpraySessionsRepository spraySessionsRepository;

    private static final Set<LocalTime> VALID_TIMES = new HashSet<>();

    static {
        VALID_TIMES.add(LocalTime.of(4, 0));
        VALID_TIMES.add(LocalTime.of(5, 0));
        VALID_TIMES.add(LocalTime.of(6, 0));
        VALID_TIMES.add(LocalTime.of(7, 0));
        VALID_TIMES.add(LocalTime.of(16, 0));
        VALID_TIMES.add(LocalTime.of(17, 0));
    }

    public SprayingSessions save(SprayingSessions sprayingSessions) {
        return spraySessionsRepository.save(sprayingSessions);
    }

    // Method to get a spraying session by ID
    public Optional<SprayingSessions> getSpraySession(Long id) {
        return spraySessionsRepository.findById(id);
    }

//    public Optional<SprayingSessions> getSpraySessionByDateAndTime(LocalDate sessionDate, LocalTime startTime, LocalTime endTime ) {
//        return spraySessionsRepository.findByDateAndStartTimeAndEndTime(sessionDate, startTime, endTime);
//    }

    public List<GetSpraySessionResponse> getSessionsByDateAndCalendarType(LocalDate date, CalenderType calendarType) {
        List<SprayingSessions> sessions;

        if (calendarType == CalenderType.GREGORIAN) {
            sessions = spraySessionsRepository.findByDate(date);
        } else if (calendarType == CalenderType.LUNAR) {
            sessions = spraySessionsRepository.findByLunarDate(date);
        } else {
            throw new IllegalArgumentException("Invalid calendar type: " + calendarType);
        }

        return sessions.stream()
                .map(GetSpraySessionResponse::new)
                .collect(Collectors.toList());
    }

}
