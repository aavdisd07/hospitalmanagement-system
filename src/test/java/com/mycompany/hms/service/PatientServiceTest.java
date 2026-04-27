package com.mycompany.hms.service;

import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.dao.PatientDao;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.exception.ValidationException;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.testsupport.LocalDbTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatientServiceTest extends LocalDbTestBase {

    private PatientDao patients;
    private DoctorDao doctors;
    private PatientService service;

    @BeforeEach
    void wire() {
        DataSource ds = dataSource();
        patients = new PatientDao(ds);
        doctors = new DoctorDao(ds);
        service = new PatientService(patients, doctors);
    }

    @Test
    void rejects_blank_name() {
        assertThatThrownBy(() -> service.add("  ", 30, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void rejects_negative_age() {
        assertThatThrownBy(() -> service.add("Ana", -1, null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void rejects_unknown_doctor_reference() {
        assertThatThrownBy(() -> service.add("Ana", 30, 99))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void inserts_when_valid() {
        Patient saved = service.add("Ana", 30, null);
        assertThat(saved.id()).isPositive();
        assertThat(patients.findById(saved.id())).isPresent();
    }
}
