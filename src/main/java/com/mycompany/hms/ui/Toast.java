package com.mycompany.hms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class Toast {

    public enum Kind { SUCCESS, INFO, WARN }

    private Toast() {}

    public static void success(Component anchor, String message) { show(anchor, message, Kind.SUCCESS, 2400); }
    public static void info(Component anchor, String message)    { show(anchor, message, Kind.INFO,    2200); }
    public static void warn(Component anchor, String message)    { show(anchor, message, Kind.WARN,    3200); }

    public static void show(Component anchor, String message, Kind kind, int millis) {
        Window owner = anchor == null ? null : SwingUtilities.getWindowAncestor(anchor);
        JWindow w = new JWindow(owner);
        w.setBackground(new Color(0, 0, 0, 0));
        w.setFocusableWindowState(false);

        Color accent = switch (kind) {
            case SUCCESS -> UiStyle.ACCENT_GREEN;
            case WARN    -> UiStyle.WARNING;
            case INFO    -> UiStyle.PRIMARY;
        };

        JPanel body = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 28));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 18, 18);
                g2.setColor(ThemeManager.isDark() ? new Color(0x1F2937) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 8, 18, 18);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 4, getHeight() - 8, 18, 18);
                g2.dispose();
            }
        };
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(12, 18, 18, 22));

        JLabel msg = new JLabel(message);
        msg.setFont(msg.getFont().deriveFont(Font.PLAIN, 13f));
        msg.setForeground(ThemeManager.isDark() ? new Color(0xE5E7EB) : new Color(0x111827));
        body.add(msg, BorderLayout.CENTER);

        w.add(body);
        w.pack();

        if (owner != null) {
            Point p = owner.getLocationOnScreen();
            int x = p.x + owner.getWidth() - w.getWidth() - 24;
            int y = p.y + owner.getHeight() - w.getHeight() - 36;
            w.setLocation(x, y);
        } else {
            Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
            w.setLocation(scr.width - w.getWidth() - 32, scr.height - w.getHeight() - 80);
        }
        w.setVisible(true);

        Timer t = new Timer(millis, e -> w.dispose());
        t.setRepeats(false);
        t.start();
    }
}
