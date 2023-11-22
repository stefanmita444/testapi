package com.example.testapi.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MultiDateFormatter {
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "dd-MM-yyyy",
            "yyyy-MM-dd",
            "MM-dd-yyyy",
            "yyyy/MM/dd",
            "dd/MM/yyyy",
            "MM/dd/yyyy"
            // Add more formats as needed
    );

    public static Date parseDate(String dateAsString) {
        for (String pattern : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(pattern).parse(dateAsString);
            } catch (ParseException ignored) {
                // If parsing fails, try the next format
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateAsString);
    }
}
