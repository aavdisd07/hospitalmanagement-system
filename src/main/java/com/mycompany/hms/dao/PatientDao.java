package com.mycompany.hms.dao;

import com.mycompany.hms.db.Database;
import com.mycompany.hms.model.Page;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.model.PatientSearchCriteria;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDao {

    private final DataSource ds;

    public PatientDao() { this(Database.dataSource()); }
    public PatientDao(DataSource ds) { this.ds = ds; }

    public Patient insert(Patient p) {
        String sql = "INSERT INTO patient(name, age, doctor_id) VALUES (?, ?, ?)";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.name());
            ps.setInt(2, p.age());
            if (p.doctorId() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, p.doctorId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Patient(keys.getInt(1), p.name(), p.age(), p.doctorId());
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert patient failed", e);
        }
    }

    public Optional<Patient> findById(int id) {
        String sql = "SELECT id, name, age, doctor_id FROM patient WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById patient failed", e);
        }
    }

    public List<Patient> findAll() {
        String sql = "SELECT id, name, age, doctor_id FROM patient ORDER BY id";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Patient> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        } catch (SQLException e) {
            throw new DataAccessException("findAll patients failed", e);
        }
    }

    public Page<Patient> search(PatientSearchCriteria c, int page, int size) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (c.nameLike() != null && !c.nameLike().isBlank()) {
            where.append(" AND name LIKE ?"); args.add("%" + c.nameLike().trim() + "%");
        }
        if (c.minAge() != null) { where.append(" AND age >= ?"); args.add(c.minAge()); }
        if (c.maxAge() != null) { where.append(" AND age <= ?"); args.add(c.maxAge()); }
        if (c.doctorId() != null) { where.append(" AND doctor_id = ?"); args.add(c.doctorId()); }

        long total;
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM patient" + where)) {
            for (int i = 0; i < args.size(); i++) ps.setObject(i + 1, args.get(i));
            try (ResultSet rs = ps.executeQuery()) { rs.next(); total = rs.getLong(1); }
        } catch (SQLException e) {
            throw new DataAccessException("count search failed", e);
        }

        List<Patient> items = new ArrayList<>();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, name, age, doctor_id FROM patient" + where +
                     " ORDER BY id LIMIT ? OFFSET ?")) {
            int idx = 1;
            for (Object a : args) ps.setObject(idx++, a);
            ps.setInt(idx++, size);
            ps.setInt(idx, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("search failed", e);
        }
        return new Page<>(items, page, size, total);
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM patient WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete patient failed", e);
        }
    }

    private Patient map(ResultSet rs) throws SQLException {
        int docId = rs.getInt("doctor_id");
        Integer doctorId = rs.wasNull() ? null : docId;
        return new Patient(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), doctorId);
    }
}
