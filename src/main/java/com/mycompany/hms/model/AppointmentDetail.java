package com.mycompany.hms.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDetail(
        int patientId,
        String patientName,
        int patientAge,
        String doctorName,
        LocalDate date,
        LocalTime time) {
}
