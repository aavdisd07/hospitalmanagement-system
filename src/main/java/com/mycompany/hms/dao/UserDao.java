package com.mycompany.hms.dao;

import com.mycompany.hms.db.Database;
import com.mycompany.hms.model.Role;
import com.mycompany.hms.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class UserDao {

    private final DataSource ds;

    public UserDao() { this(Database.dataSource()); }
    public UserDao(DataSource ds) { this.ds = ds; }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, role, doctor_id, enabled FROM users WHERE username = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByUsername failed", e);
        }
    }

    public User insert(User u) {
        String sql = "INSERT INTO users(username, password_hash, role, doctor_id, enabled) VALUES (?,?,?,?,?)";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.username());
            ps.setString(2, u.passwordHash());
            ps.setString(3, u.role().name());
            if (u.doctorId() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, u.doctorId());
            ps.setBoolean(5, u.enabled());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new User(keys.getInt(1), u.username(), u.passwordHash(), u.role(), u.doctorId(), u.enabled());
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert user failed", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        int docId = rs.getInt("doctor_id");
        Integer doctorId = rs.wasNull() ? null : docId;
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                Role.valueOf(rs.getString("role")),
                doctorId,
                rs.getBoolean("enabled")
        );
    }
}
