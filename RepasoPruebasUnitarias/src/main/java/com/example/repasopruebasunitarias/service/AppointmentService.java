package com.example.repasopruebasunitarias.service;
import com.example.repasopruebasunitarias.model.Appointment;
import com.example.repasopruebasunitarias.dto.AppointmentResponse;
import com.example.repasopruebasunitarias.repository.AppointmentRepository;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class AppointmentService {
    //Dependencias que se van a usar
    private final AppointmentRepository appointmentRepository;
    private final BlackListClient blackListClient;
    private final ConfirmationService confirmationService;

    public AppointmentService(AppointmentRepository appointmentRepository, BlackListClient blackListClient, ConfirmationService confirmationService) {
        this.appointmentRepository = appointmentRepository;
        this.blackListClient = blackListClient;
        this.confirmationService = confirmationService;
    }

    //método para crear una cita médica para responder
    public AppointmentResponse createAppointment(String patientEmail, LocalDate date, String status) {
        //lógica de negocio

        //El email no debe ser nulo y debe contener @
        if (patientEmail == null || !patientEmail.contains("@")) {
            throw new IllegalArgumentException("Patient email invalido");
        }

        //La fecha no puede ser anterior a la fecha actual
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date invalido");
        }

        //Si el paciente está en la lista negra → lanzar excepción
        if (blackListClient.isInBlackList(patientEmail)) {
            throw new IllegalStateException("Patient email invalido");
        }

        //Guardar la cita en el repositorio con estado "CREATED"
        Appointment appointment = new Appointment(patientEmail, date, status);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //Generar un código de confirmación
        String code = confirmationService.generateConfirmationCode();

        //Retornar un DTO de confirmación
        return new AppointmentResponse(savedAppointment.getId(), code);

    }





}
