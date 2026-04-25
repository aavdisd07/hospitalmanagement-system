-- 1) Users / authentication
CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(60)  NOT NULL,
    password_hash VARCHAR(120) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    doctor_id     INT          NULL,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_users_username (username),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'DOCTOR', 'RECEPTIONIST')),
    CONSTRAINT fk_users_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- bcrypt hash of "admin123" — change after first login
INSERT IGNORE INTO users (username, password_hash, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');

-- 2) Slot-based appointments
ALTER TABLE appointment
    ADD COLUMN appointment_time TIME NOT NULL DEFAULT '09:00:00' AFTER appointment_date;

ALTER TABLE appointment DROP INDEX uq_doctor_date;
ALTER TABLE appointment ADD UNIQUE KEY uq_doctor_slot (doctor_id, appointment_date, appointment_time);
