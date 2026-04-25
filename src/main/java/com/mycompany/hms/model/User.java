package com.mycompany.hms.model;

public record User(Integer id, String username, String passwordHash, Role role, Integer doctorId, boolean enabled) {
    public static User newUser(String username, String passwordHash, Role role, Integer doctorId) {
        return new User(null, username, passwordHash, role, doctorId, true);
    }
}
