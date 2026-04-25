package com.mycompany.hms.dao;

import com.mycompany.hms.model.Appointment;
import com.mycompany.hms.model.AppointmentDetail;
import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.model.Patient;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class AppointmentDaoIT {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("hms_it")
            .withUsername("test")
            .withPassword("test");

    static HikariDataSource ds;

    @BeforeAll
    static void setUp() {
        mysql.start();
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(mysql.getJdbcUrl());
        cfg.setUsername(mysql.getUsername());
        cfg.setPassword(mysql.getPassword());
        ds = new HikariDataSource(cfg);
        Flyway.configure().dataSource(ds).locations("classpath:db/migration").load().migrate();
    }

    @AfterAll
    static void tearDown() {
        if (ds != null) ds.close();
    }

    @Test
    void insert_and_query_history() {
        DoctorDao doctors = new DoctorDao(ds);
        PatientDao patients = new PatientDao(ds);
        AppointmentDao appointments = new AppointmentDao(ds);

        Doctor d = doctors.insert(Doctor.newDoctor("Dr. House", "Diagnostics"));
        Patient p = patients.insert(Patient.newPatient("Ana", 31, d.id()));
        LocalDate when = LocalDate.now().plusDays(1);
        LocalTime slot = LocalTime.of(10, 0);
        Appointment a = appointments.insert(Appointment.newAppointment(p.id(), d.id(), when, slot, "first visit"));

        assertThat(a.id()).isNotNull();
        assertThat(appointments.doctorBookedAt(d.id(), when, slot)).isTrue();

        List<AppointmentDetail> rows = appointments.findDetailsByPatientId(p.id());
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).doctorName()).isEqualTo("Dr. House");
        assertThat(rows.get(0).date()).isEqualTo(when);
    }
}
