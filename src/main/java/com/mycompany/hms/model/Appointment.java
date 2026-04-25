package com.mycompany.hms.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record Appointment(Integer id, int patientId, int doctorId, LocalDate date, LocalTime time, String notes) {
    public static Appointment newAppointment(int patientId, int doctorId, LocalDate date, LocalTime time, String notes) {
        return new Appointment(null, patientId, doctorId, date, time, notes);
    }
}
