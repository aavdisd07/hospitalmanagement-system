package com.mycompany.hms.ui;

import com.mycompany.hms.service.DashboardService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Dashboard extends JFrame {

    private static final Color ACCENT = new Color(0xE67E22);
    private static final Color ACCENT_2 = new Color(0x3498DB);
    private static final Color ACCENT_3 = new Color(0x27AE60);
    private static final Color ACCENT_4 = new Color(0x9B59B6);

    private final DashboardService service = new DashboardService();

    private final JLabel kpiPatients     = bigNumber();
    private final JLabel kpiDoctors      = bigNumber();
    private final JLabel kpiAppointments = bigNumber();
    private final JLabel kpiUpcoming     = bigNumber();

    private final JPanel chartsGrid = new JPanel(new GridLayout(2, 2, 12, 12));

    public Dashboard() {
        setTitle("HMS — Dashboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setIconImage(Icons.of("dashboard", 32).getImage());

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildKpiRow(), BorderLayout.PAGE_START.equals(BorderLayout.PAGE_START) ? BorderLayout.CENTER : BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(buildKpiRow(), BorderLayout.NORTH);
        chartsGrid.setOpaque(false);
        center.add(chartsGrid, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
        refresh();
    }

    private JPanel buildHeader() {
        JLabel title = new JLabel("Hospital Dashboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setIcon(Icons.of("dashboard", 26));
        title.setIconTextGap(10);

        JButton refresh = new JButton("Refresh", Icons.of("view"));
        refresh.addActionListener(e -> refresh());

        JButton themeToggle = new JButton(ThemeManager.isDark() ? "Light mode" : "Dark mode", Icons.of("theme"));
        themeToggle.addActionListener(e -> {
            ThemeManager.toggleDark();
            themeToggle.setText(ThemeManager.isDark() ? "Light mode" : "Dark mode");
            refresh();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(themeToggle);
        right.add(refresh);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));
        return header;
    }

    private JPanel buildKpiRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.add(kpiCard("Patients",     kpiPatients,     ACCENT,   "patient"));
        row.add(kpiCard("Doctors",      kpiDoctors,      ACCENT_2, "doctor"));
        row.add(kpiCard("Appointments", kpiAppointments, ACCENT_3, "appointment"));
        row.add(kpiCard("Upcoming",     kpiUpcoming,     ACCENT_4, "appointment"));
        row.setBorder(new EmptyBorder(0, 0, 12, 0));
        return row;
    }

    private JPanel kpiCard(String label, JLabel value, Color tint, String iconName) {
        JLabel caption = new JLabel(label.toUpperCase());
        caption.setFont(caption.getFont().deriveFont(Font.BOLD, 11f));
        caption.setForeground(tint);
        caption.setIcon(Icons.of(iconName, 16));
        caption.setIconTextGap(8);

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(tint.getRed(), tint.getGreen(), tint.getBlue(), 80), 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.add(caption, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.putClientProperty("FlatLaf.style", "arc: 12");
        return card;
    }

    private static JLabel bigNumber() {
        JLabel l = new JLabel("0");
        l.setFont(l.getFont().deriveFont(Font.BOLD, 28f));
        return l;
    }

    private void refresh() {
        try {
            DashboardService.Kpis k = service.kpis();
            kpiPatients.setText(Integer.toString(k.patients()));
            kpiDoctors.setText(Integer.toString(k.doctors()));
            kpiAppointments.setText(Integer.toString(k.appointments()));
            kpiUpcoming.setText(Integer.toString(k.upcoming()));

            chartsGrid.removeAll();
            chartsGrid.add(panel(buildLineChart()));
            chartsGrid.add(panel(buildTopDoctorsBar()));
            chartsGrid.add(panel(buildDepartmentPie()));
            chartsGrid.add(panel(buildAgePie()));
            chartsGrid.revalidate();
            chartsGrid.repaint();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }

    private static JComponent panel(JFreeChart chart) {
        themeChart(chart);
        ChartPanel cp = new ChartPanel(chart);
        cp.setMouseWheelEnabled(true);
        cp.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return cp;
    }

    private JFreeChart buildLineChart() {
        TimeSeries series = new TimeSeries("Appointments");
        Map<LocalDate, Integer> data = service.appointmentsPerDay(30);
        DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE;
        for (Map.Entry<LocalDate, Integer> e : data.entrySet()) {
            series.addOrUpdate(new Day(
                    e.getKey().getDayOfMonth(),
                    e.getKey().getMonthValue(),
                    e.getKey().getYear()),
                    e.getValue());
        }
        TimeSeriesCollection ds = new TimeSeriesCollection(series);
        JFreeChart c = ChartFactory.createTimeSeriesChart(
                "Appointments — last 30 days", "Date", "Count", ds, false, true, false);
        XYPlot plot = c.getXYPlot();
        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(true, true);
        r.setSeriesPaint(0, ACCENT);
        r.setSeriesStroke(0, new BasicStroke(2.5f));
        plot.setRenderer(r);
        return c;
    }

    private JFreeChart buildTopDoctorsBar() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Map<String, Integer> data = service.topDoctors(5);
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            ds.addValue(e.getValue(), "Appointments", e.getKey());
        }
        JFreeChart c = ChartFactory.createBarChart(
                "Top doctors by appointments", "Doctor", "Count", ds,
                PlotOrientation.HORIZONTAL, false, true, false);
        CategoryPlot plot = c.getCategoryPlot();
        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setSeriesPaint(0, ACCENT_2);
        r.setShadowVisible(false);
        r.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        return c;
    }

    private JFreeChart buildDepartmentPie() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        Map<String, Integer> data = service.doctorsByDepartment();
        data.forEach(ds::setValue);
        JFreeChart c = ChartFactory.createPieChart("Doctors by department", ds, true, true, false);
        styleSlices((PiePlot<?>) c.getPlot());
        return c;
    }

    private JFreeChart buildAgePie() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        Map<String, Integer> data = service.patientsByAgeGroup();
        data.forEach(ds::setValue);
        JFreeChart c = ChartFactory.createPieChart("Patients by age group", ds, true, true, false);
        styleSlices((PiePlot<?>) c.getPlot());
        return c;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void styleSlices(PiePlot plot) {
        Color[] palette = {ACCENT, ACCENT_2, ACCENT_3, ACCENT_4,
                new Color(0xF1C40F), new Color(0xE74C3C), new Color(0x1ABC9C)};
        var keys = plot.getDataset().getKeys();
        int i = 0;
        for (Object key : keys) {
            plot.setSectionPaint((Comparable) key, palette[i % palette.length]);
            i++;
        }
        plot.setLabelGap(0.02);
        plot.setSimpleLabels(true);
        plot.setShadowPaint(null);
        plot.setOutlineVisible(false);
    }

    private static void themeChart(JFreeChart c) {
        boolean dark = ThemeManager.isDark();
        Color bg = dark ? new Color(0x2B2B2B) : Color.WHITE;
        Color fg = dark ? new Color(0xDDDDDD) : new Color(0x222222);
        Color grid = dark ? new Color(0x444444) : new Color(0xDDDDDD);

        c.setBackgroundPaint(bg);
        if (c.getTitle() != null) c.getTitle().setPaint(fg);
        if (c.getLegend() != null) {
            c.getLegend().setBackgroundPaint(bg);
            c.getLegend().setItemPaint(fg);
        }

        if (c.getPlot() instanceof XYPlot xy) {
            xy.setBackgroundPaint(bg);
            xy.setDomainGridlinePaint(grid);
            xy.setRangeGridlinePaint(grid);
            xy.getDomainAxis().setLabelPaint(fg);
            xy.getDomainAxis().setTickLabelPaint(fg);
            xy.getRangeAxis().setLabelPaint(fg);
            xy.getRangeAxis().setTickLabelPaint(fg);
        } else if (c.getPlot() instanceof CategoryPlot cp) {
            cp.setBackgroundPaint(bg);
            cp.setDomainGridlinePaint(grid);
            cp.setRangeGridlinePaint(grid);
            cp.getDomainAxis().setLabelPaint(fg);
            cp.getDomainAxis().setTickLabelPaint(fg);
            cp.getRangeAxis().setLabelPaint(fg);
            cp.getRangeAxis().setTickLabelPaint(fg);
        } else if (c.getPlot() instanceof PiePlot<?> pp) {
            pp.setBackgroundPaint(bg);
            pp.setLabelBackgroundPaint(bg);
            pp.setLabelOutlinePaint(grid);
            pp.setLabelShadowPaint(null);
            pp.setLabelPaint(fg);
        }
    }
}
