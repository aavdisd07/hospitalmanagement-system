package com.mycompany.hms.service;

import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.dao.PatientDao;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.model.Page;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.model.PatientSearchCriteria;
import com.mycompany.hms.util.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientDao patients;
    private final DoctorDao doctors;

    public PatientService() { this(new PatientDao(), new DoctorDao()); }
    public PatientService(PatientDao patients, DoctorDao doctors) {
        this.patients = patients;
        this.doctors = doctors;
    }

    public Patient add(String name, int age, Integer doctorId) {
        String n = Validators.requireNonBlank(name, "Patient name");
        Validators.requireInRange(age, 0, 150, "Age");
        if (doctorId != null && doctors.findById(doctorId).isEmpty()) {
            throw new NotFoundException("Doctor " + doctorId + " does not exist");
        }
        Patient saved = patients.insert(Patient.newPatient(n, age, doctorId));
        log.info("Patient created: id={} name={}", saved.id(), saved.name());
        return saved;
    }

    public Patient getById(int id) {
        return patients.findById(id).orElseThrow(() ->
                new NotFoundException("Patient " + id + " not found"));
    }

    public List<Patient> listAll() { return patients.findAll(); }

    public Page<Patient> search(PatientSearchCriteria c, int page, int size) {
        if (page < 0) page = 0;
        if (size < 1 || size > 200) size = 20;
        return patients.search(c == null ? PatientSearchCriteria.empty() : c, page, size);
    }

    public void delete(int id) {
        if (!patients.deleteById(id)) throw new NotFoundException("Patient " + id + " not found");
        log.info("Patient deleted: id={}", id);
    }
}
