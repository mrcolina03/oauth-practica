package com.example.repasopruebasunitarias.service;

import com.example.repasopruebasunitarias.*;
import com.example.repasopruebasunitarias.dto.AppointmentResponse;
import com.example.repasopruebasunitarias.model.Appointment;
import com.example.repasopruebasunitarias.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;

import  static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//clase para probar Appointment Service
public class AppointmentServiceTest {
    //Dependencias a simular
    private AppointmentRepository appointmentRepository;
    private BlackListClient blackListClient;
    private ConfirmationService confirmationService;

    //clase que vamos a probar
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        //Inicializar los mocks
        //Arrange común para todos los tests
        appointmentRepository = mock(AppointmentRepository.class);
        confirmationService = mock(ConfirmationService.class);
        appointmentService = mock(AppointmentService.class);
        blackListClient = mock(BlackListClient.class);

        //Creamos el servicio de prueba con dependencias mockeadas
        appointmentService = new  AppointmentService(appointmentRepository, blackListClient, confirmationService);
    }

    //TESTS
    // T1: Flujo válido: Validar que el flujo ocurre
    @Test
    void createAppointment_validData_shouldSaveAndReturnConfirmation() {
        //ARRANGE
        //datos válidos
        String email = "valido@mail.com";
        LocalDate date =  LocalDate.parse("2026-02-10"); //fecha válida

        String status = "CREADO";

        //Simular que el paciente no está en lista negra
        when(blackListClient.isInBlackList(email)).thenReturn(false);

        //Simular que el servicio de confirmación genera el código correctamente
        when(confirmationService.generateConfirmationCode()).thenReturn("confirmado");

        //Simular el comportamiento del repository
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        //ACT
        //Guardar la cita
        AppointmentResponse appointmentResponse = appointmentService.createAppointment(email, date, status);

        //ASSERTS
        //Verificar resultados
        //id no debería estar vacío
        assertNotNull(appointmentResponse.getAppointmentId());
        //codigo debe ser igual al generado
        assertEquals("confirmado", appointmentResponse.getConfirmationCode());

        //Verificar las interacciones
        //debió usar la verificación de lista negra
        verify(blackListClient).isInBlackList(email);
        //debió usar el repo pa guardar
        verify(appointmentRepository).save(any(Appointment.class));
        //debió usar la confirmación y generar el código
        verify(confirmationService).generateConfirmationCode();


    }

    //t5: uso de argument captor: verifica los datos del objeto
    @Test
    void createAppointment_validData_shouldSaveAndReturnConfirmation_usingArgumentCaptor(){
        //ARRANGE
        String email = "valido@mail.com";
        LocalDate date =  LocalDate.parse("2026-02-10");
        String status = "CREADO";

        //simular comportamientos
        when(blackListClient.isInBlackList(email)).thenReturn(false);
        when(confirmationService.generateConfirmationCode()).thenReturn("confirmado");
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);


        //Crear el captor
        ArgumentCaptor<Appointment> appointmentCaptor = ArgumentCaptor.forClass(Appointment.class);


        //ACT
        // Guardar
        AppointmentResponse appointmentResponse = appointmentService.createAppointment(email, date, status);


        //ASSERTS
        //Validar resultados
        assertNotNull(appointmentResponse.getAppointmentId());
        assertEquals("confirmado", appointmentResponse.getConfirmationCode());

        //Validar interacciones
        verify(blackListClient).isInBlackList(email);
        //validar con qué argumento se guardó la cita
        verify(appointmentRepository).save(appointmentCaptor.capture());

        verify(confirmationService).generateConfirmationCode();




        //obtener la cita capturada
        Appointment capturedAppointment = appointmentCaptor.getValue();

        // validar el contenido del objeto
        assertEquals(email, capturedAppointment.getPatienEmail());
        assertEquals(date, capturedAppointment.getDate());
        assertNotNull(capturedAppointment.getId());
        assertEquals(status, capturedAppointment.getStatus());
    }


    //t6: Orden de ejecución: validar en qué secuencia se ejecuta
    @Test
    void createOrder_ValidData_shouldExecuteDependenciesInCorrectOrder() {
        //ARRANGE
        // Flujo normal
        String email = "correcto@mail.com";
        LocalDate date =  LocalDate.parse("2026-02-10");
        String status = "CREADO";

        when(blackListClient.isInBlackList(email)).thenReturn(false);
        when(confirmationService.generateConfirmationCode()).thenReturn("confirmado");
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        //ACT
        //guardar
        appointmentService.createAppointment(email, date, status);

        //ASSERT de orden de ejecución
        InOrder inOrder = Mockito.inOrder(blackListClient, appointmentRepository, confirmationService);

        inOrder.verify(blackListClient).isInBlackList(email);
        inOrder.verify(appointmentRepository).save(any(Appointment.class));
        inOrder.verify(confirmationService).generateConfirmationCode();

    }


    //T2: Email inválido
    //t3: Fecha inválida
    //t4: paciente en lista negra



}
