package com.mycompany.hms.ui;

import com.mycompany.hms.model.User;
import com.mycompany.hms.service.AuthService;
import com.mycompany.hms.service.Session;
import com.mycompany.hospitalmanagementsystem.HomePage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final AuthService auth = new AuthService();

    public LoginFrame() {
        setTitle("HMS — Sign in");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Icons.of("dashboard", 32).getImage());

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel("Hospital Management System");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setForeground(new Color(0x888888));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBorder(new EmptyBorder(2, 0, 18, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 0, 6, 0);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0; g.weightx = 1;

        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        usernameField.putClientProperty("JTextField.leadingIcon", Icons.of("patient", 16));
        passwordField.putClientProperty("JTextField.leadingIcon", Icons.of("theme", 16));
        passwordField.putClientProperty("JTextField.showRevealButton", true);

        form.add(usernameField, g); g.gridy++;
        form.add(passwordField, g); g.gridy++;

        JButton signIn = new JButton("Sign in");
        signIn.putClientProperty("JButton.buttonType", "roundRect");
        signIn.setFont(signIn.getFont().deriveFont(Font.BOLD));
        signIn.addActionListener(e -> doLogin());

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);

        g.fill = GridBagConstraints.HORIZONTAL;
        form.add(signIn, g); g.gridy++;

        JLabel hint = new JLabel("default: admin / admin123");
        hint.setForeground(new Color(0x888888));
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setBorder(new EmptyBorder(14, 0, 0, 0));

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        root.add(hint,   BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(380, getHeight()));
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(usernameField::requestFocusInWindow);
    }

    private void doLogin() {
        try {
            User u = auth.login(usernameField.getText(),
                    new String(passwordField.getPassword()));
            Session.set(u);
            new HomePage().setVisible(true);
            dispose();
        } catch (RuntimeException e) {
            UiErrors.show(this, e);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        }
    }
}
