package com.example.demo.Security.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ALTERNATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parseDate(String dateString) {
        try {
            // Try parsing in the standard format first (yyyy-MM-dd)
            return LocalDate.parse(dateString, STANDARD_FORMATTER);
        } catch (DateTimeParseException e) {
            // If parsing in standard format fails, try the alternate format (dd/MM/yyyy)
            try {
                return LocalDate.parse(dateString, ALTERNATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd or dd/MM/yyyy.");
            }
        }
    }
}
