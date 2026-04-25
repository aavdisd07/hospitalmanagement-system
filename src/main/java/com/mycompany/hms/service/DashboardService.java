package com.mycompany.hms.service;

import com.mycompany.hms.dao.DashboardDao;

import java.time.LocalDate;
import java.util.Map;

public class DashboardService {

    private final DashboardDao dao;

    public DashboardService() { this(new DashboardDao()); }
    public DashboardService(DashboardDao dao) { this.dao = dao; }

    public record Kpis(int patients, int doctors, int appointments, int upcoming) {}

    public Kpis kpis() {
        return new Kpis(
                dao.totalPatients(),
                dao.totalDoctors(),
                dao.totalAppointments(),
                dao.upcomingAppointments()
        );
    }

    public Map<LocalDate, Integer> appointmentsPerDay(int days) { return dao.appointmentsPerDay(days); }
    public Map<String, Integer>    doctorsByDepartment()         { return dao.doctorsByDepartment(); }
    public Map<String, Integer>    topDoctors(int limit)         { return dao.topDoctorsByAppointments(limit); }
    public Map<String, Integer>    patientsByAgeGroup()          { return dao.patientsByAgeGroup(); }
}
