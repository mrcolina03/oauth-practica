package com.example.repasopruebasunitarias.repository;
import com.example.repasopruebasunitarias.model.Appointment;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    boolean existsByPatientEmail(String patientEmail);

}
