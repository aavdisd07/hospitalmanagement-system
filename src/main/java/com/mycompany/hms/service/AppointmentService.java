package com.mycompany.hms.service;

import com.mycompany.hms.dao.AppointmentDao;
import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.dao.PatientDao;
import com.mycompany.hms.exception.ConflictException;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.exception.ValidationException;
import com.mycompany.hms.model.Appointment;
import com.mycompany.hms.model.AppointmentDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public static final LocalTime DAY_START = LocalTime.of(9, 0);
    public static final LocalTime DAY_END   = LocalTime.of(17, 0);
    public static final int       SLOT_MIN  = 30;

    private final AppointmentDao appointments;
    private final PatientDao patients;
    private final DoctorDao doctors;

    public AppointmentService() {
        this(new AppointmentDao(), new PatientDao(), new DoctorDao());
    }
    public AppointmentService(AppointmentDao appointments, PatientDao patients, DoctorDao doctors) {
        this.appointments = appointments;
        this.patients = patients;
        this.doctors = doctors;
    }

    public Appointment book(int patientId, int doctorId, LocalDate date, LocalTime time, String notes) {
        if (date == null) throw new ValidationException("Appointment date is required");
        if (time == null) throw new ValidationException("Appointment time is required");
        LocalDateTime when = LocalDateTime.of(date, time);
        if (when.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment cannot be in the past");
        }
        if (time.isBefore(DAY_START) || !time.isBefore(DAY_END)) {
            throw new ValidationException("Slot must be between " + DAY_START + " and " + DAY_END);
        }
        if (patients.findById(patientId).isEmpty()) {
            throw new NotFoundException("Patient " + patientId + " not found");
        }
        if (doctors.findById(doctorId).isEmpty()) {
            throw new NotFoundException("Doctor " + doctorId + " not found");
        }
        if (appointments.doctorBookedAt(doctorId, date, time)) {
            throw new ConflictException("Doctor " + doctorId + " is already booked on " + date + " at " + time);
        }
        Appointment saved = appointments.insert(Appointment.newAppointment(patientId, doctorId, date, time, notes));
        log.info("Appointment booked: id={} patient={} doctor={} when={} {}",
                saved.id(), patientId, doctorId, date, time);
        return saved;
    }

    public List<LocalTime> availableSlots(int doctorId, LocalDate date) {
        List<LocalTime> all = allSlots();
        List<LocalTime> booked = appointments.bookedSlotsOn(doctorId, date);
        List<LocalTime> out = new ArrayList<>(all);
        out.removeAll(booked);
        return out;
    }

    public static List<LocalTime> allSlots() {
        List<LocalTime> out = new ArrayList<>();
        for (LocalTime t = DAY_START; t.isBefore(DAY_END); t = t.plusMinutes(SLOT_MIN)) {
            out.add(t);
        }
        return out;
    }

    public List<AppointmentDetail> historyOf(int patientId) {
        return appointments.findDetailsByPatientId(patientId);
    }
}
