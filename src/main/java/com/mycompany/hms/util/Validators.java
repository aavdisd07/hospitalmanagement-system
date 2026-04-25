package com.mycompany.hms.util;

import com.mycompany.hms.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Validators {

    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Validators() {}

    public static String requireNonBlank(String v, String field) {
        if (v == null || v.isBlank()) throw new ValidationException(field + " is required");
        return v.trim();
    }

    public static int requireInRange(int v, int min, int max, String field) {
        if (v < min || v > max) throw new ValidationException(field + " must be between " + min + " and " + max);
        return v;
    }

    public static int requireInt(String v, String field) {
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(field + " must be a whole number");
        }
    }

    public static LocalDate requireDate(String v, String field) {
        try {
            return LocalDate.parse(v.trim(), DATE_FMT);
        } catch (DateTimeParseException e) {
            throw new ValidationException(field + " must be in yyyy-MM-dd format");
        }
    }
}
