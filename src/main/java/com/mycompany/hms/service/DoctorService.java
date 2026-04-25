package com.mycompany.hms.service;

import com.mycompany.hms.dao.DoctorDao;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.util.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorDao dao;

    public DoctorService() { this(new DoctorDao()); }
    public DoctorService(DoctorDao dao) { this.dao = dao; }

    public Doctor add(String name, String department) {
        String n = Validators.requireNonBlank(name, "Doctor name");
        String d = Validators.requireNonBlank(department, "Department");
        Doctor saved = dao.insert(Doctor.newDoctor(n, d));
        log.info("Doctor created: id={} name={}", saved.id(), saved.name());
        return saved;
    }

    public Doctor getById(int id) {
        return dao.findById(id).orElseThrow(() ->
                new NotFoundException("Doctor " + id + " not found"));
    }

    public List<Doctor> searchByName(String name) {
        return dao.findByName(Validators.requireNonBlank(name, "Doctor name"));
    }

    public List<Doctor> listAll() { return dao.findAll(); }

    public void delete(int id) {
        if (!dao.deleteById(id)) throw new NotFoundException("Doctor " + id + " not found");
        log.info("Doctor deleted: id={}", id);
    }

    public Doctor update(int id, String name, String department) {
        String n = Validators.requireNonBlank(name, "Doctor name");
        String d = Validators.requireNonBlank(department, "Department");
        if (!dao.findById(id).isPresent()) throw new NotFoundException("Doctor " + id + " not found");
        Doctor updated = new Doctor(id, n, d);
        if (!dao.update(updated)) throw new NotFoundException("Doctor " + id + " not found");
        log.info("Doctor updated: id={}", id);
        return updated;
    }
}
