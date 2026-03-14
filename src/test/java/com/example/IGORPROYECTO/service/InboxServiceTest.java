package com.example.IGORPROYECTO.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.IGORPROYECTO.model.EmailDocument;
import com.example.IGORPROYECTO.repository.EmailRepository;

@ExtendWith(MockitoExtension.class)
public class InboxServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    private EmailDocument emailTest;

    @BeforeEach
    public void setUp() {
        emailTest = new EmailDocument();
        emailTest.setId("1");
        emailTest.setRemitente("admin@igor.com");
        emailTest.setDestinatario("usuario@gmail.com");
        emailTest.setAsunto("Notificación IGOR");
        emailTest.setMensaje("Has recibido una notificación");
        emailTest.setEstado("ENVIADO");
    }

    @Test
    public void testGuardarCorreoEnBandeja() {
        // ARRANGE
        when(emailRepository.save(emailTest)).thenReturn(emailTest);

        // ACT
        EmailDocument emailGuardado = emailService.save(emailTest);

        // ASSERT
        assertNotNull(emailGuardado);
        assertEquals("usuario@gmail.com", emailGuardado.getDestinatario());
        assertEquals("ENVIADO", emailGuardado.getEstado());
        verify(emailRepository, times(1)).save(emailTest);
    }

    @Test
    public void testObtenerBandejaDelUsuario() {
        // ARRANGE
        EmailDocument email2 = new EmailDocument();
        email2.setId("2");
        email2.setDestinatario("usuario@gmail.com");
        
        List<EmailDocument> emails = Arrays.asList(emailTest, email2);
        when(emailRepository.findByDestinatarioOrderByFechaDesc("usuario@gmail.com"))
            .thenReturn(emails);

        // ACT
        List<EmailDocument> bandeja = emailService.listInbox("usuario@gmail.com");

        // ASSERT
        assertNotNull(bandeja);
        assertEquals(2, bandeja.size());
        verify(emailRepository, times(1)).findByDestinatarioOrderByFechaDesc("usuario@gmail.com");
    }
}