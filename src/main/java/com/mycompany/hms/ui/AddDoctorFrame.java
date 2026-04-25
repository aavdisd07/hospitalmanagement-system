package com.mycompany.hms.ui;

import com.mycompany.hms.service.DoctorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddDoctorFrame extends JFrame {

    private final JTextField name = new JTextField(20);
    private final JTextField department = new JTextField(20);
    private final DoctorService service = new DoctorService();

    public AddDoctorFrame() {
        setTitle("Add Doctor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Icons.of("doctor", 32).getImage());

        name.putClientProperty("JTextField.placeholderText", "Full name");
        department.putClientProperty("JTextField.placeholderText", "Department");

        JButton save = new JButton("Save", Icons.of("add"));
        save.putClientProperty("JButton.buttonType", "roundRect");
        save.addActionListener(e -> save());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Name"), g);
        g.gridx = 1;             form.add(name, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Department"), g);
        g.gridx = 1;             form.add(department, g);
        g.gridx = 1; g.gridy = 2; g.anchor = GridBagConstraints.LINE_END;
        g.fill = GridBagConstraints.NONE; form.add(save, g);

        setContentPane(form);
        pack();
        setMinimumSize(new Dimension(420, getHeight()));
        setLocationRelativeTo(null);
    }

    private void save() {
        try {
            service.add(name.getText(), department.getText());
            UiErrors.info(this, "Doctor saved");
            dispose();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
        }
    }
}
