/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.hospitalmanagementsystem;

/**
 *
 * @author Rohi Deshmukh
 */
public class HomePage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomePage.class.getName());

    /**
     * Creates new form HomePage
     */
    public HomePage() {
        initComponents();
        installMenuBar();
        decorateButtons();
        applyRoleGuards();
        setIconImage(com.mycompany.hms.ui.Icons.of("dashboard", 32).getImage());
        setTitle("Hospital Management System");
    }

    private void decorateButtons() {
        jButton8.setIcon(com.mycompany.hms.ui.Icons.of("doctor"));         // View Doctor
        jButton9.setIcon(com.mycompany.hms.ui.Icons.of("appointment"));    // Get Appointment
        jButton10.setIcon(com.mycompany.hms.ui.Icons.of("search"));        // Search Doctor
        jButton11.setIcon(com.mycompany.hms.ui.Icons.of("appointment"));   // Check Appointment
        jButton12.setIcon(com.mycompany.hms.ui.Icons.of("search"));        // Search Patient
        jButton13.setIcon(com.mycompany.hms.ui.Icons.of("view"));          // View Patient
        jButton14.setIcon(com.mycompany.hms.ui.Icons.of("add"));           // Add Patient
        jButton15.setIcon(com.mycompany.hms.ui.Icons.of("delete"));        // Delete Patient
        jButton13.setText("View Patient"); // fix the original "VIew Patient" typo
        for (javax.swing.JButton b : new javax.swing.JButton[]{
                jButton8, jButton9, jButton10, jButton11, jButton12, jButton13, jButton14, jButton15}) {
            b.setIconTextGap(8);
            b.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            b.putClientProperty("JButton.buttonType", "roundRect");
        }
    }

    private void installMenuBar() {
        javax.swing.JMenuBar bar = new javax.swing.JMenuBar();
        com.mycompany.hms.model.Role role = com.mycompany.hms.service.Session.isAuthenticated()
                ? com.mycompany.hms.service.Session.role()
                : com.mycompany.hms.model.Role.ADMIN;

        javax.swing.JMenu viewMenu = new javax.swing.JMenu("View");
        javax.swing.JMenuItem dashboardItem = new javax.swing.JMenuItem(
                "Dashboard", com.mycompany.hms.ui.Icons.of("dashboard"));
        dashboardItem.addActionListener(e -> new com.mycompany.hms.ui.Dashboard().setVisible(true));
        viewMenu.add(dashboardItem);
        bar.add(viewMenu);

        if (role.canManageDoctors()) {
            javax.swing.JMenu doctorsMenu = new javax.swing.JMenu("Doctors");
            javax.swing.JMenuItem add = new javax.swing.JMenuItem("Add",   com.mycompany.hms.ui.Icons.of("add"));
            javax.swing.JMenuItem edit= new javax.swing.JMenuItem("Edit",  com.mycompany.hms.ui.Icons.of("view"));
            javax.swing.JMenuItem del = new javax.swing.JMenuItem("Delete",com.mycompany.hms.ui.Icons.of("delete"));
            add.addActionListener(e -> new com.mycompany.hms.ui.AddDoctorFrame().setVisible(true));
            edit.addActionListener(e -> new com.mycompany.hms.ui.EditDoctorFrame().setVisible(true));
            del.addActionListener(e -> new com.mycompany.hms.ui.DeleteDoctorFrame().setVisible(true));
            doctorsMenu.add(add); doctorsMenu.add(edit); doctorsMenu.add(del);
            bar.add(doctorsMenu);
        }

        javax.swing.JMenu themeMenu = new javax.swing.JMenu("Theme");
        javax.swing.JMenuItem toggleItem = new javax.swing.JMenuItem(
                com.mycompany.hms.ui.ThemeManager.isDark() ? "Switch to Light" : "Switch to Dark",
                com.mycompany.hms.ui.Icons.of("theme"));
        toggleItem.addActionListener(e -> {
            com.mycompany.hms.ui.ThemeManager.toggleDark();
            toggleItem.setText(com.mycompany.hms.ui.ThemeManager.isDark() ? "Switch to Light" : "Switch to Dark");
        });
        themeMenu.add(toggleItem);
        bar.add(themeMenu);

        bar.add(javax.swing.Box.createHorizontalGlue());

        if (com.mycompany.hms.service.Session.isAuthenticated()) {
            javax.swing.JLabel who = new javax.swing.JLabel(
                    "  " + com.mycompany.hms.service.Session.get().username()
                            + " (" + role + ")  ");
            who.setForeground(new java.awt.Color(0xAAAAAA));
            bar.add(who);
            javax.swing.JButton logout = new javax.swing.JButton("Logout");
            logout.putClientProperty("JButton.buttonType", "borderless");
            logout.addActionListener(e -> {
                com.mycompany.hms.service.Session.clear();
                dispose();
                new com.mycompany.hms.ui.LoginFrame().setVisible(true);
            });
            bar.add(logout);
        }

        setJMenuBar(bar);
    }

    private void applyRoleGuards() {
        com.mycompany.hms.model.Role role = com.mycompany.hms.service.Session.isAuthenticated()
                ? com.mycompany.hms.service.Session.role()
                : com.mycompany.hms.model.Role.ADMIN;
        boolean canPatients = role.canManagePatients();
        boolean canBook     = role.canBookAppointments();
        jButton14.setEnabled(canPatients);   // Add Patient
        jButton15.setEnabled(canPatients);   // Delete Patient
        jButton9.setEnabled(canBook);        // Get Appointment
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hospital Management System");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 153, 153));
        jButton8.setText("View Doctor");
        jButton8.setAlignmentY(1);
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.setBorderPainted(false);
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jButton9.setBackground(new java.awt.Color(255, 255, 255));
        jButton9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(0, 153, 153));
        jButton9.setText("Get Appointment");
        jButton9.setAlignmentY(1);
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.setBorderPainted(false);
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton10.setBackground(new java.awt.Color(255, 255, 255));
        jButton10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 153, 153));
        jButton10.setText("Search Doctor");
        jButton10.setAlignmentY(1);
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.setBorderPainted(false);
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jButton11.setBackground(new java.awt.Color(255, 255, 255));
        jButton11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(0, 153, 153));
        jButton11.setText("Check Appointment");
        jButton11.setAlignmentY(1);
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.setBorderPainted(false);
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jButton12.setBackground(new java.awt.Color(255, 255, 255));
        jButton12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(0, 153, 153));
        jButton12.setText("Search Patient");
        jButton12.setAlignmentY(1);
        jButton12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.setBorderPainted(false);
        jButton12.addActionListener(this::jButton12ActionPerformed);

        jButton13.setBackground(new java.awt.Color(255, 255, 255));
        jButton13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(0, 153, 153));
        jButton13.setText("VIew Patient");
        jButton13.setAlignmentY(1);
        jButton13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton13.setBorderPainted(false);
        jButton13.addActionListener(this::jButton13ActionPerformed);

        jButton14.setBackground(new java.awt.Color(255, 255, 255));
        jButton14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(0, 153, 153));
        jButton14.setText("Add Patient");
        jButton14.setAlignmentY(1);
        jButton14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton14.setBorderPainted(false);
        jButton14.addActionListener(this::jButton14ActionPerformed);

        jButton15.setBackground(new java.awt.Color(255, 255, 255));
        jButton15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(0, 153, 153));
        jButton15.setText("Delete Patient");
        jButton15.setAlignmentY(1);
        jButton15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton15.setBorderPainted(false);
        jButton15.addActionListener(this::jButton15ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(137, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(147, 147, 147))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(164, 164, 164))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        ViewDoctor viewdoctor = new ViewDoctor();
        viewdoctor.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        new com.mycompany.hms.ui.BookAppointmentFrame().setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        SearchDoctor searchdoctor = new SearchDoctor();
        searchdoctor.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        CheckAppointment checkappointment= new CheckAppointment();
        checkappointment.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        new com.mycompany.hms.ui.PatientSearchFrame().setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
          ViewPatient viewpatient = new ViewPatient();
          viewpatient.setVisible(true);
          dispose();
          
          
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        AddPatient addPatient= new AddPatient();
        addPatient.setVisible(true);
               
        dispose();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        DeletePatient deletepatient = new DeletePatient();
        deletepatient.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_jButton15ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new HomePage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
