package com.mycompany.hms.dao;

import com.mycompany.hms.db.Database;
import com.mycompany.hms.model.Appointment;
import com.mycompany.hms.model.AppointmentDetail;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {

    private final DataSource ds;

    public AppointmentDao() { this(Database.dataSource()); }
    public AppointmentDao(DataSource ds) { this.ds = ds; }

    public Appointment insert(Appointment a) {
        String sql = "INSERT INTO appointment(patient_id, doctor_id, appointment_date, appointment_time, notes) VALUES (?,?,?,?,?)";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.patientId());
            ps.setInt(2, a.doctorId());
            ps.setDate(3, Date.valueOf(a.date()));
            ps.setTime(4, Time.valueOf(a.time()));
            ps.setString(5, a.notes());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Appointment(keys.getInt(1), a.patientId(), a.doctorId(), a.date(), a.time(), a.notes());
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert appointment failed", e);
        }
    }

    public boolean doctorBookedAt(int doctorId, LocalDate date, LocalTime time) {
        String sql = "SELECT 1 FROM appointment WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? LIMIT 1";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("doctorBookedAt check failed", e);
        }
    }

    public List<LocalTime> bookedSlotsOn(int doctorId, LocalDate date) {
        String sql = "SELECT appointment_time FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                List<LocalTime> out = new ArrayList<>();
                while (rs.next()) out.add(rs.getTime("appointment_time").toLocalTime());
                return out;
            }
        } catch (SQLException e) {
            throw new DataAccessException("bookedSlotsOn failed", e);
        }
    }

    public List<AppointmentDetail> findDetailsByPatientId(int patientId) {
        String sql = """
                SELECT p.id AS pid, p.name AS pname, p.age AS page,
                       d.name AS dname, a.appointment_date AS adate, a.appointment_time AS atime
                FROM   patient p
                JOIN   appointment a ON a.patient_id = p.id
                JOIN   doctor d      ON d.id = a.doctor_id
                WHERE  p.id = ?
                ORDER  BY a.appointment_date DESC, a.appointment_time DESC
                """;
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                List<AppointmentDetail> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new AppointmentDetail(
                            rs.getInt("pid"),
                            rs.getString("pname"),
                            rs.getInt("page"),
                            rs.getString("dname"),
                            rs.getDate("adate").toLocalDate(),
                            rs.getTime("atime").toLocalTime()
                    ));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findDetailsByPatientId failed", e);
        }
    }
}
