package com.mycompany.hms.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ThemeManager {

    private static final Logger log = LoggerFactory.getLogger(ThemeManager.class);
    private static final Path PREF_FILE = Paths.get(System.getProperty("user.home"), ".hms-theme");

    public enum Mode { LIGHT, DARK }

    private static Mode current = loadPreferred();

    private ThemeManager() {}

    public static void install() {
        applyCurrent();
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
        UIManager.put("Table.rowHeight", 28);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("TitlePane.unifiedBackground", true);
    }

    public static Mode current() { return current; }

    public static boolean isDark() { return current == Mode.DARK; }

    public static void toggleDark() {
        current = (current == Mode.DARK) ? Mode.LIGHT : Mode.DARK;
        applyCurrent();
        savePreferred();
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
        }
        log.info("Theme switched to {}", current);
    }

    private static void applyCurrent() {
        try {
            if (current == Mode.DARK) {
                UIManager.setLookAndFeel(new FlatArcDarkOrangeIJTheme());
            } else {
                UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
            }
        } catch (UnsupportedLookAndFeelException e) {
            log.warn("FlatLaf theme failed, falling back: {}", e.getMessage());
            try {
                UIManager.setLookAndFeel(current == Mode.DARK ? new FlatDarkLaf() : new FlatLightLaf());
            } catch (UnsupportedLookAndFeelException ignored) {}
        }
    }

    private static Mode loadPreferred() {
        try {
            if (Files.exists(PREF_FILE)) {
                String raw = Files.readString(PREF_FILE).trim();
                return Mode.valueOf(raw);
            }
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Could not read theme pref: {}", e.getMessage());
        }
        return Mode.LIGHT;
    }

    private static void savePreferred() {
        try {
            Files.writeString(PREF_FILE, current.name());
        } catch (IOException e) {
            log.warn("Could not save theme pref: {}", e.getMessage());
        }
    }
}
