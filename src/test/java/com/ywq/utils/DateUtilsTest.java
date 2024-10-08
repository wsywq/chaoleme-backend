package com.ywq.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtilsTest {

    @Test
    public void testGetTodayDate() {
        String todayDate = DateUtils.getTodayDate();
        assertNotNull(todayDate);
        // Verify that the date is in the correct format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD);
        LocalDate parsedDate = LocalDate.parse(todayDate, formatter);
        assertEquals(LocalDate.now(), parsedDate);
    }

    @Test
    public void testGetLocalTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        String localTime = DateUtils.getLocalTime(dateTime);
        assertNotNull(localTime);

        // Verify that the date is in the correct format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.HH_MM_SS);
        String expectedTime = dateTime.toLocalTime().format(formatter);

        assertEquals(expectedTime, localTime);
    }
}
