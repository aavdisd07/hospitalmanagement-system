package com.mycompany.hms.ui;

import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.model.Patient;
import com.mycompany.hms.service.AppointmentService;
import com.mycompany.hms.service.DoctorService;
import com.mycompany.hms.service.PatientService;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class BookAppointmentFrame extends JFrame {

    private final JComboBox<Patient>   patientCombo = new JComboBox<>();
    private final JComboBox<Doctor>    doctorCombo  = new JComboBox<>();
    private final JDateChooser         dateChooser  = new JDateChooser();
    private final JComboBox<LocalTime> slotCombo    = new JComboBox<>();
    private final JTextArea            notesArea    = new JTextArea(3, 20);

    private final AppointmentService appointmentService = new AppointmentService();
    private final DoctorService doctorService = new DoctorService();
    private final PatientService patientService = new PatientService();

    public BookAppointmentFrame() {
        setTitle("Book appointment");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Icons.of("appointment", 32).getImage());

        for (Patient p : patientService.listAll()) patientCombo.addItem(p);
        patientCombo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                String label = (v instanceof Patient p) ? "#" + p.id() + " — " + p.name() : "";
                return super.getListCellRendererComponent(l, label, i, s, f);
            }
        });
        for (Doctor d : doctorService.listAll()) doctorCombo.addItem(d);
        doctorCombo.setRenderer(new EditDoctorFrame.DoctorRenderer());

        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));

        Runnable refreshSlots = this::refreshSlots;
        doctorCombo.addActionListener(e -> refreshSlots.run());
        dateChooser.getDateEditor().addPropertyChangeListener("date", e -> refreshSlots.run());
        refreshSlots.run();

        JButton book = new JButton("Book", Icons.of("appointment"));
        book.putClientProperty("JButton.buttonType", "roundRect");
        book.addActionListener(e -> book());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        int y = 0;
        addRow(form, g, y++, "Patient",  patientCombo);
        addRow(form, g, y++, "Doctor",   doctorCombo);
        addRow(form, g, y++, "Date",     dateChooser);
        addRow(form, g, y++, "Slot",     slotCombo);
        addRow(form, g, y++, "Notes",    new JScrollPane(notesArea));
        g.gridx = 1; g.gridy = y; g.anchor = GridBagConstraints.LINE_END;
        g.fill = GridBagConstraints.NONE; form.add(book, g);

        setContentPane(form);
        pack();
        setMinimumSize(new Dimension(480, getHeight()));
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel p, GridBagConstraints g, int y, String label, Component comp) {
        g.gridx = 0; g.gridy = y; g.weightx = 0; p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1;             p.add(comp, g);
    }

    private void refreshSlots() {
        slotCombo.removeAllItems();
        Doctor d = (Doctor) doctorCombo.getSelectedItem();
        Date raw = dateChooser.getDate();
        if (d == null || raw == null) return;
        LocalDate date = raw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        try {
            for (LocalTime t : appointmentService.availableSlots(d.id(), date)) {
                slotCombo.addItem(t);
            }
        } catch (RuntimeException ignored) { /* DB not ready, fall through */ }
    }

    private void book() {
        try {
            Patient p = (Patient) patientCombo.getSelectedItem();
            Doctor  d = (Doctor)  doctorCombo.getSelectedItem();
            LocalTime t = (LocalTime) slotCombo.getSelectedItem();
            Date raw = dateChooser.getDate();
            if (p == null || d == null || t == null || raw == null) {
                UiErrors.info(this, "Pick patient, doctor, date, and slot");
                return;
            }
            LocalDate date = raw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            appointmentService.book(p.id(), d.id(), date, t, notesArea.getText());
            UiErrors.info(this, "Appointment booked");
            dispose();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }
}
