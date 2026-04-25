package com.mycompany.hms.dao;

import com.mycompany.hms.db.Database;
import com.mycompany.hms.model.Doctor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDao {

    private final DataSource ds;

    public DoctorDao() { this(Database.dataSource()); }
    public DoctorDao(DataSource ds) { this.ds = ds; }

    public Doctor insert(Doctor d) {
        String sql = "INSERT INTO doctor(name, department) VALUES (?, ?)";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.name());
            ps.setString(2, d.department());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Doctor(keys.getInt(1), d.name(), d.department());
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert doctor failed", e);
        }
    }

    public Optional<Doctor> findById(int id) {
        String sql = "SELECT id, name, department FROM doctor WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById doctor failed", e);
        }
    }

    public List<Doctor> findByName(String name) {
        String sql = "SELECT id, name, department FROM doctor WHERE name LIKE ? ORDER BY name";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            return collect(ps);
        } catch (SQLException e) {
            throw new DataAccessException("findByName doctor failed", e);
        }
    }

    public List<Doctor> findAll() {
        String sql = "SELECT id, name, department FROM doctor ORDER BY id";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return collect(ps);
        } catch (SQLException e) {
            throw new DataAccessException("findAll doctors failed", e);
        }
    }

    public boolean update(Doctor d) {
        String sql = "UPDATE doctor SET name = ?, department = ? WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.name());
            ps.setString(2, d.department());
            ps.setInt(3, d.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("update doctor failed", e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM doctor WHERE id = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("delete doctor failed", e);
        }
    }

    private List<Doctor> collect(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            List<Doctor> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private Doctor map(ResultSet rs) throws SQLException {
        return new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("department"));
    }
}
