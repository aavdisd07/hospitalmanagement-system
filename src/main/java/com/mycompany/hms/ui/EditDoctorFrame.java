package com.mycompany.hms.ui;

import com.mycompany.hms.model.Doctor;
import com.mycompany.hms.service.DoctorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditDoctorFrame extends JFrame {

    private final JComboBox<Doctor> picker = new JComboBox<>();
    private final JTextField name = new JTextField(20);
    private final JTextField department = new JTextField(20);
    private final DoctorService service = new DoctorService();

    public EditDoctorFrame() {
        setTitle("Edit Doctor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Icons.of("doctor", 32).getImage());
        picker.setRenderer(new DoctorRenderer());
        for (Doctor d : service.listAll()) picker.addItem(d);
        picker.addActionListener(e -> populate());

        JButton save = new JButton("Save changes", Icons.of("view"));
        save.putClientProperty("JButton.buttonType", "roundRect");
        save.addActionListener(e -> save());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Doctor"), g);
        g.gridx = 1;             form.add(picker, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Name"), g);
        g.gridx = 1;             form.add(name, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Department"), g);
        g.gridx = 1;             form.add(department, g);
        g.gridx = 1; g.gridy = 3; g.anchor = GridBagConstraints.LINE_END;
        g.fill = GridBagConstraints.NONE; form.add(save, g);

        setContentPane(form);
        pack();
        setMinimumSize(new Dimension(440, getHeight()));
        setLocationRelativeTo(null);
        populate();
    }

    private void populate() {
        Doctor d = (Doctor) picker.getSelectedItem();
        if (d != null) { name.setText(d.name()); department.setText(d.department()); }
    }

    private void save() {
        try {
            Doctor d = (Doctor) picker.getSelectedItem();
            if (d == null) return;
            service.update(d.id(), name.getText(), department.getText());
            UiErrors.info(this, "Updated");
            dispose();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }

    static class DoctorRenderer extends DefaultListCellRenderer {
        @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                boolean isSelected, boolean cellHasFocus) {
            String label = (value instanceof Doctor d) ? "#" + d.id() + " — " + d.name() + " (" + d.department() + ")" : "";
            return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
        }
    }
}
