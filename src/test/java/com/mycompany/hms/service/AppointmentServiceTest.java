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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentDao appointments;
    @Mock PatientDao patients;
    @Mock DoctorDao doctors;
    @InjectMocks AppointmentService service;

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);
    private final LocalTime slot = LocalTime.of(10, 0);

    @Test
    void rejects_past_date() {
        assertThatThrownBy(() -> service.book(1, 1, LocalDate.now().minusDays(1), slot, null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("past");
    }

    @Test
    void rejects_unknown_patient() {
        when(patients.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.book(99, 1, tomorrow, slot, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    void rejects_unknown_doctor() {
        when(patients.findById(1)).thenReturn(Optional.of(new Patient(1, "Ana", 30, null)));
        when(doctors.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.book(1, 99, tomorrow, slot, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Doctor");
    }

    @Test
    void rejects_double_booked_slot() {
        when(patients.findById(1)).thenReturn(Optional.of(new Patient(1, "Ana", 30, null)));
        when(doctors.findById(2)).thenReturn(Optional.of(new Doctor(2, "Dr. Who", "Cardiology")));
        when(appointments.doctorBookedAt(2, tomorrow, slot)).thenReturn(true);
        assertThatThrownBy(() -> service.book(1, 2, tomorrow, slot, null))
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
        when(patients.findById(1)).thenReturn(Optional.of(new Patient(1, "Ana", 30, null)));
        when(doctors.findById(2)).thenReturn(Optional.of(new Doctor(2, "Dr. Who", "Cardiology")));
        when(appointments.doctorBookedAt(2, tomorrow, slot)).thenReturn(false);
        when(appointments.insert(any())).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            return new Appointment(42, a.patientId(), a.doctorId(), a.date(), a.time(), a.notes());
        });
        Appointment saved = service.book(1, 2, tomorrow, slot, "checkup");
        org.assertj.core.api.Assertions.assertThat(saved.id()).isEqualTo(42);
    }
}
