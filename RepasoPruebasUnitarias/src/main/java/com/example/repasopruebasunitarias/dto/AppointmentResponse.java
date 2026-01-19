package com.example.repasopruebasunitarias.dto;

public class AppointmentResponse {
    private String appointmentId;
    private String confirmationCode;

    public AppointmentResponse(String appointmentId, String confirmationCode) {
        this.appointmentId = appointmentId;
        this.confirmationCode = confirmationCode;
    }

    // getter y setter


    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
