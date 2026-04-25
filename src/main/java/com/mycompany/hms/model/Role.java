package com.mycompany.hms.model;

public enum Role {
    ADMIN, DOCTOR, RECEPTIONIST;

    public boolean canManageDoctors()       { return this == ADMIN; }
    public boolean canManagePatients()      { return this == ADMIN || this == RECEPTIONIST; }
    public boolean canBookAppointments()    { return this == ADMIN || this == RECEPTIONIST; }
    public boolean canViewDashboard()       { return true; }
}
