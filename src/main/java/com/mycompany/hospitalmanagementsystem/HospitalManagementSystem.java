package com.mycompany.hospitalmanagementsystem;

import com.mycompany.hms.db.Database;
import com.mycompany.hms.db.Migrator;
import com.mycompany.hms.ui.LoginFrame;
import com.mycompany.hms.ui.ThemeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class HospitalManagementSystem {

    private static final Logger log = LoggerFactory.getLogger(HospitalManagementSystem.class);

    public static void main(String[] args) {
        log.info("HMS starting");
        try {
            Migrator.migrate();
        } catch (RuntimeException e) {
            log.error("Startup failed", e);
            JOptionPane.showMessageDialog(null,
                    "Could not connect to database:\n" + e.getMessage(),
                    "Startup error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        ThemeManager.install();
        Runtime.getRuntime().addShutdownHook(new Thread(Database::shutdown, "hms-shutdown"));

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
