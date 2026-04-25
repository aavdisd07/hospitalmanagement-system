package com.mycompany.hms.ui;

import com.mycompany.hms.exception.ConflictException;
import com.mycompany.hms.exception.NotFoundException;
import com.mycompany.hms.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public final class UiErrors {

    private static final Logger log = LoggerFactory.getLogger(UiErrors.class);

    private UiErrors() {}

    public static void show(Component parent, Throwable t) {
        String title;
        int type;
        if (t instanceof ValidationException) { title = "Invalid input";   type = JOptionPane.WARNING_MESSAGE; }
        else if (t instanceof NotFoundException) { title = "Not found";    type = JOptionPane.WARNING_MESSAGE; }
        else if (t instanceof ConflictException) { title = "Conflict";     type = JOptionPane.WARNING_MESSAGE; }
        else { title = "Unexpected error";                                  type = JOptionPane.ERROR_MESSAGE; }

        log.warn("UI error surfaced: {}", t.getMessage(), t);
        JOptionPane.showMessageDialog(parent, t.getMessage(), title, type);
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
