CREATE TABLE IF NOT EXISTS doctor (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    department  VARCHAR(80)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_doctor_name (name),
    INDEX idx_doctor_department (department)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS patient (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    age         INT          NOT NULL,
    doctor_id   INT          NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_patient_age CHECK (age >= 0 AND age <= 150),
    CONSTRAINT fk_patient_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_patient_name (name),
    INDEX idx_patient_doctor (doctor_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS appointment (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    patient_id        INT  NOT NULL,
    doctor_id         INT  NOT NULL,
    appointment_date  DATE NOT NULL,
    notes             VARCHAR(500) NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_appt_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctor(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY uq_doctor_date (doctor_id, appointment_date),
    INDEX idx_appt_patient (patient_id),
    INDEX idx_appt_date (appointment_date)
) ENGINE=InnoDB;
