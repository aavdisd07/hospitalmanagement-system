package com.mycompany.hms.model;

public record Patient(Integer id, String name, int age, Integer doctorId) {
    public static Patient newPatient(String name, int age, Integer doctorId) {
        return new Patient(null, name, age, doctorId);
    }
}
