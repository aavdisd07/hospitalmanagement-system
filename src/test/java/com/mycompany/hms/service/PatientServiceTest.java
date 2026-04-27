package com.mycompany.hms.service;

import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.dao.PatientDao;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.exception.ValidationException;
import com.mycompany.hms.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
PatientDao dao = new PatientDao(); // real DB connection
 PatientService service = new PatientService(dao);
    
    @Mock DoctorDao doctors;

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
        when(doctors.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.add("Ana", 30, 99))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void inserts_when_valid() {
        when(patients.insert(any())).thenAnswer(inv -> {
            Patient p = inv.getArgument(0);
            return new Patient(7, p.name(), p.age(), p.doctorId());
        });
        Patient saved = service.add("Ana", 30, null);
        org.assertj.core.api.Assertions.assertThat(saved.id()).isEqualTo(7);
    }
}
