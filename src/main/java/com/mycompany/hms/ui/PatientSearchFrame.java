package com.mycompany.hms.ui;

import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.model.Page;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.model.PatientSearchCriteria;
import com.mycompany.hms.service.DoctorService;
import com.mycompany.hms.service.PatientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientSearchFrame extends JFrame {

    private static final int PAGE_SIZE = 20;

    private final JTextField nameField = new JTextField(14);
    private final JTextField minAge = new JTextField(4);
    private final JTextField maxAge = new JTextField(4);
    private final JComboBox<Object> doctorCombo = new JComboBox<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Age", "Doctor"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final JLabel pageLabel = new JLabel(" ");
    private final JButton prev = new JButton("Prev");
    private final JButton next = new JButton("Next");

    private final PatientService patientService = new PatientService();
    private int currentPage = 0;

    public PatientSearchFrame() {
        setTitle("Search patients");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setIconImage(Icons.of("search", 32).getImage());
        setJMenuBar(buildMenuBar());

        nameField.putClientProperty("JTextField.placeholderText", "name contains…");
        minAge.putClientProperty("JTextField.placeholderText", "min");
        maxAge.putClientProperty("JTextField.placeholderText", "max");

        doctorCombo.addItem("Any doctor");
        for (Doctor d : new DoctorService().listAll()) doctorCombo.addItem(d);
        doctorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int idx,
                                                                    boolean s, boolean f) {
                String label = (value instanceof Doctor d)
                        ? "#" + d.id() + " — " + d.name() : String.valueOf(value);
                return super.getListCellRendererComponent(list, label, idx, s, f);
            }
        });

        JButton find = new JButton("Search", Icons.of("search"));
        find.putClientProperty("JButton.buttonType", "roundRect");
        find.addActionListener(e -> { currentPage = 0; runSearch(); });

        prev.addActionListener(e -> { if (currentPage > 0) { currentPage--; runSearch(); }});
        next.addActionListener(e -> { currentPage++; runSearch(); });

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filters.add(new JLabel("Name:"));    filters.add(nameField);
        filters.add(new JLabel("Age:"));     filters.add(minAge); filters.add(new JLabel("–")); filters.add(maxAge);
        filters.add(new JLabel("Doctor:"));  filters.add(doctorCombo);
        filters.add(find);

        JPanel pager = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pager.add(prev); pager.add(pageLabel); pager.add(next);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.add(filters, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(pager, BorderLayout.SOUTH);

        setContentPane(root);
        runSearch();
    }

    private void runSearch() {
        try {
            PatientSearchCriteria c = PatientSearchCriteria.empty()
                    .withName(nameField.getText())
                    .withAgeRange(parseOpt(minAge.getText()), parseOpt(maxAge.getText()))
                    .withDoctor(doctorCombo.getSelectedItem() instanceof Doctor d ? d.id() : null);
            Page<Patient> p = patientService.search(c, currentPage, PAGE_SIZE);

            tableModel.setRowCount(0);
            for (Patient pt : p.items()) {
                tableModel.addRow(new Object[]{
                        pt.id(), pt.name(), pt.age(),
                        pt.doctorId() == null ? "" : pt.doctorId()
                });
            }
            pageLabel.setText("Page " + (p.page() + 1) + " / " + Math.max(1, p.totalPages())
                    + "   (" + p.total() + " results)");
            prev.setEnabled(p.hasPrev());
            next.setEnabled(p.hasNext());
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu export = new JMenu("Export");
        JMenuItem csv = new JMenuItem("CSV…");
        JMenuItem xls = new JMenuItem("Excel (.xlsx)…");
        JMenuItem pdf = new JMenuItem("PDF…");
        csv.addActionListener(e -> exportTo("csv"));
        xls.addActionListener(e -> exportTo("xlsx"));
        pdf.addActionListener(e -> exportTo("pdf"));
        export.add(csv); export.add(xls); export.add(pdf);
        mb.add(export);
        return mb;
    }

    private void exportTo(String fmt) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("patients." + fmt));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        java.nio.file.Path target = fc.getSelectedFile().toPath();
        try {
            switch (fmt) {
                case "csv"  -> com.mycompany.hms.util.Exporters.toCsv(table, target);
                case "xlsx" -> com.mycompany.hms.util.Exporters.toXlsx(table, "Patients", target);
                case "pdf"  -> com.mycompany.hms.util.Exporters.toPdf(table, "Patients report", target);
            }
            UiErrors.info(this, "Saved: " + target);
        } catch (java.io.IOException e) {
            UiErrors.show(this, e);
        }
    }

    private static Integer parseOpt(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return null; }
    }

    public JTable table() { return table; }
}
