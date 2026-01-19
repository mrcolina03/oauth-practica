package com.example.repasopruebasunitarias.model;

import java.time.LocalDate;
import java.util.UUID;

public class Appointment {
    //Atributos Cita médica
    private String id;
    private String patientEmail;
    private LocalDate date;
    private String status;

    //Constructuor
    public Appointment(String patientEmail, LocalDate date, String status) {
        this.id = UUID.randomUUID().toString();
        this.patientEmail = patientEmail;
        this.date = date;
        this.status = status;
    }

    //getter y setters para status y patientEmail


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatienEmail() {
        return patientEmail;
    }

    public void setPatienEmail(String patienEmail) {
        this.patientEmail = patienEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter agregado para la fecha (necesario por los tests)
    public LocalDate getDate() {
        return date;
    }
}
