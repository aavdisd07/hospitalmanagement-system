package com.mycompany.hms.dao;

import com.mycompany.hms.db.Database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDao {

    private final DataSource ds;

    public DashboardDao() { this(Database.dataSource()); }
    public DashboardDao(DataSource ds) { this.ds = ds; }

    public int totalPatients()    { return countOf("SELECT COUNT(*) FROM patient"); }
    public int totalDoctors()     { return countOf("SELECT COUNT(*) FROM doctor"); }
    public int totalAppointments(){ return countOf("SELECT COUNT(*) FROM appointment"); }

    public int upcomingAppointments() {
        return countOf("SELECT COUNT(*) FROM appointment WHERE appointment_date >= CURRENT_DATE");
    }

    public Map<LocalDate, Integer> appointmentsPerDay(int lastNDays) {
        String sql = """
                SELECT appointment_date AS d, COUNT(*) AS c
                FROM   appointment
                WHERE  appointment_date >= (CURRENT_DATE - INTERVAL ? DAY)
                GROUP  BY appointment_date
                ORDER  BY appointment_date
                """;
        Map<LocalDate, Integer> out = new LinkedHashMap<>();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lastNDays);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.put(rs.getDate("d").toLocalDate(), rs.getInt("c"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("appointmentsPerDay failed", e);
        }
        return out;
    }

    public Map<String, Integer> doctorsByDepartment() {
        return groupCount("SELECT department AS k, COUNT(*) AS c FROM doctor GROUP BY department ORDER BY c DESC");
    }

    public Map<String, Integer> topDoctorsByAppointments(int limit) {
        String sql = """
                SELECT d.name AS k, COUNT(*) AS c
                FROM   appointment a
                JOIN   doctor d ON d.id = a.doctor_id
                GROUP  BY d.id, d.name
                ORDER  BY c DESC
                LIMIT  ?
                """;
        Map<String, Integer> out = new LinkedHashMap<>();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.put(rs.getString("k"), rs.getInt("c"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("topDoctorsByAppointments failed", e);
        }
        return out;
    }

    public Map<String, Integer> patientsByAgeGroup() {
        String sql = """
                SELECT CASE
                         WHEN age < 13  THEN '0-12'
                         WHEN age < 20  THEN '13-19'
                         WHEN age < 36  THEN '20-35'
                         WHEN age < 56  THEN '36-55'
                         WHEN age < 76  THEN '56-75'
                         ELSE                '76+'
                       END AS k,
                       COUNT(*) AS c
                FROM   patient
                GROUP  BY k
                ORDER  BY MIN(age)
                """;
        return groupCount(sql);
    }

    private int countOf(String sql) {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("count failed: " + sql, e);
        }
    }

    private Map<String, Integer> groupCount(String sql) {
        Map<String, Integer> out = new LinkedHashMap<>();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.put(rs.getString("k"), rs.getInt("c"));
        } catch (SQLException e) {
            throw new DataAccessException("groupCount failed: " + sql, e);
        }
        return out;
    }
}
