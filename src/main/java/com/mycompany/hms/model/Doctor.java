package com.mycompany.hms.model;

public record Doctor(Integer id, String name, String department) {
    public static Doctor newDoctor(String name, String department) {
        return new Doctor(null, name, department);
    }
}
