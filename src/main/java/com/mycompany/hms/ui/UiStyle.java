package com.mycompany.hms.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UiStyle {

    /* ---------------------------------------------------------------------
     * Brand palette
     * ------------------------------------------------------------------- */
    public static final Color PRIMARY        = new Color(0xF97316);
    public static final Color PRIMARY_HOVER  = new Color(0xEA580C);
    public static final Color PRIMARY_PRESS  = new Color(0xC2410C);
    public static final Color PRIMARY_SOFT   = new Color(0xFEF3E7);
    public static final Color PRIMARY_DEEP   = new Color(0x9A3412);
    public static final Color ACCENT_SKY     = new Color(0x0EA5E9);
    public static final Color ACCENT_GREEN   = new Color(0x10B981);
    public static final Color ACCENT_VIOLET  = new Color(0x8B5CF6);
    public static final Color ACCENT_PINK    = new Color(0xEC4899);
    public static final Color ACCENT_AMBER   = new Color(0xF59E0B);
    public static final Color DANGER         = new Color(0xEF4444);
    public static final Color WARNING        = new Color(0xF59E0B);

    /* ---------------------------------------------------------------------
     * Spacing / radius scale
     * ------------------------------------------------------------------- */
    public static final int PAD_XS = 4;
    public static final int PAD_S  = 8;
    public static final int PAD_M  = 12;
    public static final int PAD_L  = 18;
    public static final int PAD_XL = 24;
    public static final int PAD_2XL = 32;

    public static final int RADIUS_SM = 10;
    public static final int RADIUS_MD = 14;
    public static final int RADIUS_LG = 18;
    public static final int RADIUS_XL = 22;

    private UiStyle() {}

    /* ---------------------------------------------------------------------
     * Theme-aware surface colours
     * ------------------------------------------------------------------- */
    public static Color surface()      { return ThemeManager.isDark() ? new Color(0x1F2937) : Color.WHITE; }
    public static Color surfaceAlt()   { return ThemeManager.isDark() ? new Color(0x111827) : new Color(0xF8FAFC); }
    public static Color surfaceSunk()  { return ThemeManager.isDark() ? new Color(0x0B1220) : new Color(0xF1F5F9); }
    public static Color textPrimary()  { return ThemeManager.isDark() ? new Color(0xF3F4F6) : new Color(0x0F172A); }
    public static Color textMuted()    { return ThemeManager.isDark() ? new Color(0x9CA3AF) : new Color(0x64748B); }
    public static Color textFaint()    { return ThemeManager.isDark() ? new Color(0x6B7280) : new Color(0x94A3B8); }
    public static Color border()       { return ThemeManager.isDark() ? new Color(0x374151) : new Color(0xE2E8F0); }
    public static Color borderStrong() { return ThemeManager.isDark() ? new Color(0x4B5563) : new Color(0xCBD5E1); }
    public static Color sidebarBg()    { return ThemeManager.isDark() ? new Color(0x0B1220) : new Color(0xFAF7F2); }

    /* ---------------------------------------------------------------------
     * Typography
     * ------------------------------------------------------------------- */
    public static JLabel h1(String text)      { return label(text, Font.BOLD,  26f, textPrimary()); }
    public static JLabel h2(String text)      { return label(text, Font.BOLD,  20f, textPrimary()); }
    public static JLabel h3(String text)      { return label(text, Font.BOLD,  16f, textPrimary()); }
    public static JLabel body(String text)    { return label(text, Font.PLAIN, 13f, textPrimary()); }
    public static JLabel muted(String text)   { return label(text, Font.PLAIN, 12f, textMuted());  }
    public static JLabel caption(String text) {
        JLabel l = label(text == null ? "" : text.toUpperCase(), Font.BOLD, 11f, textMuted());
        return l;
    }

    private static JLabel label(String text, int style, float size, Color fg) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, (int) size));
        l.setForeground(fg);
        return l;
    }

    /* ---------------------------------------------------------------------
     * Buttons
     * ------------------------------------------------------------------- */
    public static JButton primary(String text, FlatSVGIcon icon) {
        JButton b = base(text, icon);
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.putClientProperty("FlatLaf.style",
                "background: #F97316; foreground: #FFFFFF; "
              + "borderColor: #F97316; focusedBorderColor: #C2410C; "
              + "hoverBackground: #EA580C; pressedBackground: #C2410C; "
              + "innerFocusWidth: 0; arc: 18;");
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        return b;
    }

    public static JButton secondary(String text, FlatSVGIcon icon) {
        JButton b = base(text, icon);
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.putClientProperty("FlatLaf.style", "arc: 18;");
        return b;
    }

    public static JButton ghost(String text, FlatSVGIcon icon) {
        JButton b = base(text, icon);
        b.putClientProperty("JButton.buttonType", "borderless");
        return b;
    }

    public static JButton danger(String text, FlatSVGIcon icon) {
        JButton b = base(text, icon);
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.putClientProperty("FlatLaf.style",
                "background: #EF4444; foreground: #FFFFFF; "
              + "borderColor: #EF4444; "
              + "hoverBackground: #DC2626; pressedBackground: #B91C1C; "
              + "arc: 18;");
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        return b;
    }

    private static JButton base(String text, FlatSVGIcon icon) {
        JButton b = (icon != null) ? new JButton(text, icon) : new JButton(text);
        b.setIconTextGap(8);
        b.setFocusPainted(false);
        b.setMargin(new Insets(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /* ---------------------------------------------------------------------
     * Page chrome
     * ------------------------------------------------------------------- */
    public static JPanel page(String title, String subtitle, FlatSVGIcon icon, JComponent body) {
        JPanel root = new JPanel(new BorderLayout(0, PAD_M));
        root.setBorder(new EmptyBorder(PAD_L, PAD_L, PAD_L, PAD_L));
        root.add(pageHeader(title, subtitle, icon), BorderLayout.NORTH);
        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.add(card(body), BorderLayout.CENTER);
        root.add(cardWrap, BorderLayout.CENTER);
        return root;
    }

    public static JPanel pageHeader(String title, String subtitle, FlatSVGIcon icon) {
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 22f));
        if (icon != null) { t.setIcon(icon); t.setIconTextGap(10); }

        JLabel s = new JLabel(subtitle == null ? " " : subtitle);
        s.setForeground(textMuted());
        s.setBorder(new EmptyBorder(2, 0, 0, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(t); left.add(s); left.add(Box.createVerticalStrut(6)); left.add(accentBar());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(left, BorderLayout.WEST);
        return header;
    }

    public static JPanel accentBar() {
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.setPaint(new GradientPaint(0, 0, PRIMARY, w, 0, ACCENT_PINK));
                g2.fillRoundRect(0, 0, Math.min(w, 90), Math.max(3, h - 1), 4, 4);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setMaximumSize(new Dimension(90, 4));
        bar.setPreferredSize(new Dimension(90, 4));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        return bar;
    }

    /* ---------------------------------------------------------------------
     * Cards
     * ------------------------------------------------------------------- */
    public static JComponent card(JComponent inner) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(surface());
        card.setBorder(new CompoundBorder(
                new RoundedBorder(RADIUS_LG, border()),
                new EmptyBorder(PAD_L, PAD_L, PAD_L, PAD_L)));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    /** Soft card with painted drop-shadow for elevated surfaces. */
    public static JComponent softCard(JComponent inner) {
        ShadowPanel card = new ShadowPanel(RADIUS_LG);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(PAD_L, PAD_L, PAD_L, PAD_L));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    /** v2 KPI card: caption + big value + subtitle/delta. */
    public static JComponent kpiCard(String label, JLabel valueLabel,
                                     Color accent, FlatSVGIcon icon) {
        return kpiCard(label, valueLabel, null, accent, icon);
    }

    public static JComponent kpiCard(String label, JLabel valueLabel,
                                     String subtitle, Color accent, FlatSVGIcon icon) {
        JLabel cap = new JLabel(label.toUpperCase());
        cap.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cap.setForeground(accent);
        if (icon != null) {
            FlatSVGIcon tinted = icon.derive(16, 16);
            tinted.setColorFilter(new FlatSVGIcon.ColorFilter(c -> accent));
            cap.setIcon(tinted);
            cap.setIconTextGap(8);
        }

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(textPrimary());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        cap.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(cap);
        inner.add(Box.createVerticalStrut(6));
        inner.add(valueLabel);

        if (subtitle != null && !subtitle.isBlank()) {
            JLabel sub = new JLabel(subtitle);
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            sub.setForeground(textMuted());
            sub.setAlignmentX(Component.LEFT_ALIGNMENT);
            inner.add(Box.createVerticalStrut(6));
            inner.add(sub);
        }

        ShadowPanel card = new ShadowPanel(RADIUS_LG);
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new AccentStripBorder(RADIUS_LG, accent, new Color(0, 0, 0, 0)),
                new EmptyBorder(PAD_L, PAD_L + 4, PAD_L, PAD_L)));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    /* ---------------------------------------------------------------------
     * Pills & badges
     * ------------------------------------------------------------------- */
    public static JLabel statusPill(String text, Color accent) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(accent);
        l.setBorder(new EmptyBorder(4, 12, 4, 12));
        l.setOpaque(true);
        l.setBackground(blend(accent, surface(), 0.85f));
        l.putClientProperty("FlatLaf.style", "arc: 999;");
        return l;
    }

    /* ---------------------------------------------------------------------
     * Navigation tiles (modern square-ish action card)
     * ------------------------------------------------------------------- */
    public static JComponent navTile(JButton hostBtn, String title, String hint,
                                     FlatSVGIcon icon, Color accent) {
        // The provided JButton already has the action listener wired up.
        // We hide its label/border and overlay it on a clickable card.
        hostBtn.setText("");
        hostBtn.setIcon(null);
        hostBtn.setBorder(null);
        hostBtn.setBorderPainted(false);
        hostBtn.setContentAreaFilled(false);
        hostBtn.setFocusPainted(false);
        hostBtn.setOpaque(false);
        hostBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        FlatSVGIcon tinted = icon == null ? null : icon.derive(28, 28);
        if (tinted != null) tinted.setColorFilter(new FlatSVGIcon.ColorFilter(c -> accent));

        JLabel iconBadge = new JLabel(tinted);
        iconBadge.setOpaque(true);
        iconBadge.setBackground(blend(accent, surface(), 0.86f));
        iconBadge.setBorder(new EmptyBorder(10, 10, 10, 10));
        iconBadge.putClientProperty("FlatLaf.style", "arc: 14;");
        iconBadge.setHorizontalAlignment(SwingConstants.CENTER);
        iconBadge.setPreferredSize(new Dimension(52, 52));

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ttl.setForeground(textPrimary());

        JLabel sub = new JLabel(hint == null ? " " : hint);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(textMuted());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        ttl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(ttl);
        text.add(Box.createVerticalStrut(2));
        text.add(sub);

        JLabel chevron = new JLabel("›");
        chevron.setFont(new Font("Segoe UI", Font.BOLD, 22));
        chevron.setForeground(textFaint());

        JPanel content = new JPanel(new BorderLayout(14, 0));
        content.setOpaque(false);
        content.add(iconBadge, BorderLayout.WEST);
        content.add(text, BorderLayout.CENTER);
        content.add(chevron, BorderLayout.EAST);

        // Keep the original button alive (so its action listeners stay
        // wired) but don't let it occupy any space in our new layout.
        hostBtn.setVisible(false);
        hostBtn.setPreferredSize(new Dimension(0, 0));

        TileCard card = new TileCard(accent);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(PAD_L, PAD_L, PAD_L, PAD_L));
        card.add(content, BorderLayout.CENTER);

        // Whole tile is clickable: forward to button
        MouseAdapter forward = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setHover(true);
                if (!hostBtn.isEnabled()) card.setHover(false);
            }
            @Override public void mouseExited(MouseEvent e) { card.setHover(false); }
            @Override public void mousePressed(MouseEvent e) { card.setPressed(true); }
            @Override public void mouseReleased(MouseEvent e) {
                card.setPressed(false);
                if (hostBtn.isEnabled()
                        && card.contains(e.getX(), e.getY())) {
                    hostBtn.doClick();
                }
            }
        };
        card.addMouseListener(forward);
        content.addMouseListener(forward);
        text.addMouseListener(forward);
        iconBadge.addMouseListener(forward);
        chevron.addMouseListener(forward);

        if (!hostBtn.isEnabled()) {
            ttl.setForeground(textFaint());
            sub.setText("Restricted by role");
            chevron.setText("⌀");
        }
        return card;
    }

    /* ---------------------------------------------------------------------
     * Sidebar navigation row
     * ------------------------------------------------------------------- */
    public static JComponent sidebarItem(String text, FlatSVGIcon icon, boolean selected, Runnable onClick) {
        Color accent = PRIMARY;
        FlatSVGIcon tinted = icon == null ? null : icon.derive(18, 18);
        if (tinted != null) {
            Color iconColor = selected ? accent : textMuted();
            tinted.setColorFilter(new FlatSVGIcon.ColorFilter(c -> iconColor));
        }
        JLabel l = new JLabel(text, tinted, SwingConstants.LEFT);
        l.setIconTextGap(12);
        l.setFont(new Font("Segoe UI", selected ? Font.BOLD : Font.PLAIN, 13));
        l.setForeground(selected ? accent : textPrimary());
        l.setBorder(new EmptyBorder(10, 14, 10, 14));
        l.setOpaque(true);
        l.setBackground(selected ? blend(accent, surface(), 0.86f) : new Color(0, 0, 0, 0));
        l.putClientProperty("FlatLaf.style", "arc: 12;");
        if (onClick != null) {
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
                @Override public void mouseEntered(MouseEvent e) {
                    if (!selected) l.setBackground(surfaceSunk());
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (!selected) l.setBackground(new Color(0, 0, 0, 0));
                }
            });
        }
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return l;
    }

    /* ---------------------------------------------------------------------
     * Hero banner with gradient backdrop
     * ------------------------------------------------------------------- */
    public static JComponent gradientHero(String eyebrow, String title, String subtitle, FlatSVGIcon icon) {
        JPanel hero = new JPanel(new BorderLayout(PAD_L, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.setPaint(new GradientPaint(0, 0, new Color(0xFB923C), w, h, new Color(0xC2410C)));
                g2.fillRoundRect(0, 0, w, h, RADIUS_XL, RADIUS_XL);
                g2.setColor(new Color(255, 255, 255, 28));
                g2.fillOval(w - 220, -90, 240, 240);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(w - 380, h - 90, 200, 200);
                g2.dispose();
            }
        };
        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(PAD_XL, PAD_XL, PAD_XL, PAD_XL));

        JLabel eye = new JLabel(eyebrow == null ? " " : eyebrow.toUpperCase());
        eye.setFont(new Font("Segoe UI", Font.BOLD, 11));
        eye.setForeground(new Color(255, 255, 255, 220));

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        ttl.setForeground(Color.WHITE);
        if (icon != null) {
            FlatSVGIcon white = icon.derive(28, 28);
            white.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));
            ttl.setIcon(white);
            ttl.setIconTextGap(12);
        }

        JLabel sub = new JLabel("<html><div style='width:560px'>"
                + (subtitle == null ? "" : subtitle) + "</div></html>");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 220));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        eye.setAlignmentX(Component.LEFT_ALIGNMENT);
        ttl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.add(eye);
        txt.add(Box.createVerticalStrut(6));
        txt.add(ttl);
        txt.add(Box.createVerticalStrut(6));
        txt.add(sub);

        hero.add(txt, BorderLayout.CENTER);
        hero.setPreferredSize(new Dimension(0, 150));
        return hero;
    }

    /* ---------------------------------------------------------------------
     * Brand panel — used by LoginFrame
     * ------------------------------------------------------------------- */
    public static JComponent brandPanel(String tagline) {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(); int h = getHeight();
                g2.setPaint(new GradientPaint(0, 0, new Color(0xFB923C), w, h, new Color(0xC2410C)));
                g2.fillRect(0, 0, w, h);
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(-80, h - 220, 280, 280);
                g2.fillOval(w - 200, -120, 260, 260);
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fillOval(w - 320, h - 160, 200, 200);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(400, 540));
        p.setOpaque(true);

        JLabel logo = new JLabel("HMS");
        logo.setForeground(Color.WHITE);
        logo.setFont(logo.getFont().deriveFont(Font.BOLD, 60f));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("<html><div style='text-align:center; width:260px;'>" + tagline + "</div></html>");
        sub.setForeground(new Color(255, 255, 255, 235));
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 14f));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel features = new JPanel();
        features.setOpaque(false);
        features.setLayout(new BoxLayout(features, BoxLayout.Y_AXIS));
        features.setAlignmentX(Component.CENTER_ALIGNMENT);
        features.add(featureRow("Patients & doctors in one workspace"));
        features.add(Box.createVerticalStrut(8));
        features.add(featureRow("Appointment scheduling that just works"));
        features.add(Box.createVerticalStrut(8));
        features.add(featureRow("Live dashboard for the floor"));

        JLabel pill = new JLabel("v1.0  •  built for clinical teams");
        pill.setForeground(Color.WHITE);
        pill.setFont(pill.getFont().deriveFont(Font.BOLD, 11f));
        pill.setBorder(new EmptyBorder(6, 14, 6, 14));
        pill.setOpaque(false);
        pill.setAlignmentX(Component.CENTER_ALIGNMENT);
        pill.putClientProperty("FlatLaf.style", "background: #ffffff44; arc: 999;");

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.add(logo);
        stack.add(Box.createVerticalStrut(8));
        stack.add(sub);
        stack.add(Box.createVerticalStrut(28));
        stack.add(features);
        stack.add(Box.createVerticalStrut(28));
        stack.add(pill);
        p.add(stack);
        return p;
    }

    private static JComponent featureRow(String text) {
        JLabel dot = new JLabel("✓");
        dot.setFont(dot.getFont().deriveFont(Font.BOLD, 13f));
        dot.setForeground(Color.WHITE);
        dot.setOpaque(true);
        dot.setBackground(new Color(255, 255, 255, 60));
        dot.setHorizontalAlignment(SwingConstants.CENTER);
        dot.setPreferredSize(new Dimension(22, 22));
        dot.putClientProperty("FlatLaf.style", "arc: 999;");

        JLabel l = new JLabel(text);
        l.setForeground(new Color(255, 255, 255, 240));
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 12.5f));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.add(dot);
        row.add(l);
        return row;
    }

    /* ---------------------------------------------------------------------
     * Misc
     * ------------------------------------------------------------------- */
    public static Color blend(Color fg, Color bg, float bgWeight) {
        bgWeight = Math.max(0f, Math.min(1f, bgWeight));
        float fw = 1f - bgWeight;
        int r = (int) (fg.getRed()   * fw + bg.getRed()   * bgWeight);
        int g = (int) (fg.getGreen() * fw + bg.getGreen() * bgWeight);
        int b = (int) (fg.getBlue()  * fw + bg.getBlue()  * bgWeight);
        return new Color(r, g, b);
    }

    /* ---------------------------------------------------------------------
     * Borders & components
     * ------------------------------------------------------------------- */
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        public RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(1, 1, 1, 1); }
    }

    public static class AccentStripBorder extends AbstractBorder {
        private final int radius;
        private final Color accent;
        private final Color base;
        public AccentStripBorder(int radius, Color accent, Color base) {
            this.radius = radius; this.accent = accent; this.base = base;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (base.getAlpha() > 0) {
                g2.setColor(base);
                g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            }
            g2.setColor(accent);
            g2.fillRoundRect(x, y, 4, h - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(1, 5, 1, 1); }
    }

    /** Card with painted soft drop-shadow. */
    public static class ShadowPanel extends JPanel {
        private final int radius;
        public ShadowPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            for (int i = 0; i < 6; i++) {
                g2.setColor(new Color(15, 23, 42, 6 + i));
                g2.fillRoundRect(i, i + 1, w - 2 * i - 1, h - 2 * i - 2, radius, radius);
            }
            g2.setColor(surface());
            g2.fillRoundRect(0, 0, w - 6, h - 7, radius, radius);
            g2.setColor(border());
            g2.drawRoundRect(0, 0, w - 7, h - 8, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Insets getInsets() {
            Insets ins = super.getInsets();
            return new Insets(ins.top, ins.left, ins.bottom + 6, ins.right + 6);
        }
    }

    /** Interactive tile card (hover / press states). */
    public static class TileCard extends JPanel {
        private final Color accent;
        private boolean hover;
        private boolean pressed;
        public TileCard(Color accent) {
            this.accent = accent;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        public void setHover(boolean h)   { this.hover = h;   repaint(); }
        public void setPressed(boolean p) { this.pressed = p; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            int r = RADIUS_LG;
            // shadow
            int shadowDepth = hover ? 9 : 5;
            for (int i = 0; i < shadowDepth; i++) {
                g2.setColor(new Color(15, 23, 42, 5 + i));
                g2.fillRoundRect(i, i + 1, w - 2 * i - 1, h - 2 * i - 2, r, r);
            }
            int dx = hover && !pressed ? 0 : 0;
            int dy = pressed ? 1 : 0;
            // surface
            g2.setColor(surface());
            g2.fillRoundRect(dx, dy, w - 6, h - 7, r, r);
            // hover wash
            if (hover) {
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 14));
                g2.fillRoundRect(dx, dy, w - 6, h - 7, r, r);
            }
            // top accent stripe
            g2.setColor(accent);
            g2.fillRoundRect(dx, dy, w - 6, 4, r, r);
            // border
            g2.setColor(hover ? UiStyle.blend(accent, border(), 0.65f) : border());
            g2.drawRoundRect(dx, dy, w - 7, h - 8, r, r);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Insets getInsets() {
            Insets ins = super.getInsets();
            return new Insets(ins.top + 4, ins.left, ins.bottom + 6, ins.right + 6);
        }
    }

    /**
     * Restyles a NetBeans-generated frame in place. Replaces the hard-coded
     * teal panel background with the theme surface, retones titles/buttons,
     * and removes the white matte borders on text fields. Call this from the
     * constructor after initComponents().
     */
    public static void decorateLegacy(JFrame frame, String iconName) {
        try { frame.setIconImage(Icons.of(iconName, 32).getImage()); } catch (RuntimeException ignored) {}
        applyLegacyTo(frame.getContentPane());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private static final Color LEGACY_TEAL_1 = new Color(0, 153, 153);
    private static final Color LEGACY_TEAL_2 = new Color(0, 102, 102);

    private static void applyLegacyTo(Component c) {
        if (c instanceof JPanel p) {
            Color bg = p.getBackground();
            if (bg != null && (bg.equals(LEGACY_TEAL_1) || bg.equals(LEGACY_TEAL_2))) {
                p.setBackground(surfaceAlt());
            }
        }
        if (c instanceof JLabel l) {
            Color fg = l.getForeground();
            if (fg != null && fg.equals(Color.WHITE)) l.setForeground(null);
            Font f = l.getFont();
            if (f != null && f.getSize() >= 14 && f.isItalic()) {
                l.setFont(new Font("Segoe UI", Font.BOLD, 22));
                l.setForeground(PRIMARY);
                l.setIconTextGap(10);
            } else if (f != null && f.isBold() && f.getSize() <= 14) {
                l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
        }
        if (c instanceof JButton b) {
            b.setBackground(null);
            b.setForeground(null);
            b.setBorder(null);
            b.setBorderPainted(true);
            b.setContentAreaFilled(true);
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setMargin(new Insets(6, 14, 6, 14));
            b.setIconTextGap(8);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            String text = b.getText() == null ? "" : b.getText().toLowerCase();
            if (text.contains("delete")) {
                b.putClientProperty("JButton.buttonType", "roundRect");
                b.putClientProperty("FlatLaf.style",
                        "background: #EF4444; foreground: #FFFFFF; "
                      + "borderColor: #EF4444; hoverBackground: #DC2626; "
                      + "pressedBackground: #B91C1C; arc: 18;");
            } else if (text.contains("back")) {
                b.putClientProperty("JButton.buttonType", "borderless");
                b.putClientProperty("FlatLaf.style", "arc: 18;");
            } else if (text.contains("add") || text.contains("save")
                    || text.contains("get") || text.contains("check")) {
                b.putClientProperty("JButton.buttonType", "roundRect");
                b.putClientProperty("FlatLaf.style",
                        "background: #F97316; foreground: #FFFFFF; "
                      + "borderColor: #F97316; hoverBackground: #EA580C; "
                      + "pressedBackground: #C2410C; arc: 18;");
            } else {
                b.putClientProperty("JButton.buttonType", "roundRect");
                b.putClientProperty("FlatLaf.style", "arc: 18;");
            }
        }
        if (c instanceof JTextField tf) {
            tf.setBorder(null);
            tf.putClientProperty("JComponent.roundRect", true);
            tf.putClientProperty("FlatLaf.style", "arc: 12; minimumHeight: 32;");
        }
        if (c instanceof JTable t) {
            t.setRowHeight(32);
            t.setShowGrid(false);
            t.setIntercellSpacing(new Dimension(0, 0));
            t.setSelectionBackground(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 36));
            t.setSelectionForeground(null);
            if (t.getTableHeader() != null) {
                t.getTableHeader().setFont(t.getFont().deriveFont(Font.BOLD));
            }
        }
        if (c instanceof Container con) {
            for (Component child : con.getComponents()) applyLegacyTo(child);
        }
    }
}
