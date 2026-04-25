package com.mycompany.hms.ui;

import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.service.DoctorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DeleteDoctorFrame extends JFrame {

    private final JComboBox<Doctor> picker = new JComboBox<>();
    private final DoctorService service = new DoctorService();

    public DeleteDoctorFrame() {
        setTitle("Delete Doctor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Icons.of("delete", 32).getImage());
        picker.setRenderer(new EditDoctorFrame.DoctorRenderer());
        for (Doctor d : service.listAll()) picker.addItem(d);

        JButton del = new JButton("Delete", Icons.of("delete"));
        del.putClientProperty("JButton.buttonType", "roundRect");
        del.setForeground(new Color(0xC0392B));
        del.addActionListener(e -> doDelete());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Doctor"), g);
        g.gridx = 1;             form.add(picker, g);
        g.gridx = 1; g.gridy = 1; g.anchor = GridBagConstraints.LINE_END;
        g.fill = GridBagConstraints.NONE; form.add(del, g);

        setContentPane(form);
        pack();
        setMinimumSize(new Dimension(420, getHeight()));
        setLocationRelativeTo(null);
    }

    private void doDelete() {
        try {
            Doctor d = (Doctor) picker.getSelectedItem();
            if (d == null) return;
            int ok = JOptionPane.showConfirmDialog(this,
                    "Delete " + d.name() + "?",
                    "Confirm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return;
            service.delete(d.id());
            UiErrors.info(this, "Deleted");
            dispose();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }
}
