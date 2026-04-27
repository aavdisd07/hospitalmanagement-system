package com.mycompany.hms.service;

import com.mycompany.hms.dao.AppointmentDao;
import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.dao.PatientDao;
import com.mycompany.hms.exception.ConflictException;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.exception.ValidationException;
import com.mycompany.hms.model.Appointment;
import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.testsupport.LocalDbTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentServiceTest extends LocalDbTestBase {

    private AppointmentDao appointments;
    private PatientDao patients;
    private DoctorDao doctors;
    private AppointmentService service;

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);
    private final LocalTime slot = LocalTime.of(10, 0);

    @BeforeEach
    void wire() {
        DataSource ds = dataSource();
        appointments = new AppointmentDao(ds);
        patients = new PatientDao(ds);
        doctors = new DoctorDao(ds);
        service = new AppointmentService(appointments, patients, doctors);
    }

    @Test
    void rejects_past_date() {
        assertThatThrownBy(() -> service.book(1, 1, LocalDate.now().minusDays(1), slot, null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("past");
    }

    @Test
    void rejects_unknown_patient() {
        assertThatThrownBy(() -> service.book(99, 1, tomorrow, slot, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    void rejects_unknown_doctor() {
        Patient p = patients.insert(Patient.newPatient("Ana", 30, null));
        assertThatThrownBy(() -> service.book(p.id(), 99, tomorrow, slot, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Doctor");
    }

    @Test
    void rejects_double_booked_slot() {
        Doctor d = doctors.insert(Doctor.newDoctor("Dr. Who", "Cardiology"));
        Patient p = patients.insert(Patient.newPatient("Ana", 30, d.id()));
        appointments.insert(Appointment.newAppointment(p.id(), d.id(), tomorrow, slot, null));
        assertThatThrownBy(() -> service.book(p.id(), d.id(), tomorrow, slot, null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already booked");
    }

    @Test
    void rejects_slot_outside_day() {
        assertThatThrownBy(() -> service.book(1, 2, tomorrow, LocalTime.of(7, 0), null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Slot");
    }

    @Test
    void books_when_all_valid() {
        Doctor d = doctors.insert(Doctor.newDoctor("Dr. Who", "Cardiology"));
        Patient p = patients.insert(Patient.newPatient("Ana", 30, d.id()));
        Appointment saved = service.book(p.id(), d.id(), tomorrow, slot, "checkup");
        assertThat(saved.id()).isPositive();
        assertThat(appointments.doctorBookedAt(d.id(), tomorrow, slot)).isTrue();
    }
}
