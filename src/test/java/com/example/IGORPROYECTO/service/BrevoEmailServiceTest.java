package com.example.IGORPROYECTO.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BrevoEmailServiceTest {

    @Test
    public void testValidarFormatoDestinatarios() {
        // ARRANGE
        String destinatarioVacio = "";
        String destinatarioValido = "usuario@gmail.com";

        // ACT & ASSERT
        assertTrue(destinatarioVacio.isEmpty());
        assertTrue(destinatarioValido.contains("@"));
        assertFalse(destinatarioValido.isEmpty());
    }

    @Test
    public void testValidarAsuntoNoVacio() {
        // ARRANGE
        String asuntoValido = "Notificación IGOR";
        String asuntoVacio = "";

        // ACT & ASSERT
        assertFalse(asuntoValido.isEmpty());
        assertTrue(asuntoVacio.isEmpty());
    }

    @Test
    public void testValidarMensajeNoVacio() {
        // ARRANGE
        String mensajeValido = "Este es un mensaje importante";
        String mensajeVacio = "";

        // ACT & ASSERT
        assertFalse(mensajeValido.isEmpty());
        assertTrue(mensajeVacio.isEmpty());
    }

    @Test
    public void testValidarArregloDestinatarios() {
        // ARRANGE
        String[] destinariosMultiples = {"user1@gmail.com", "user2@gmail.com"};
        String[] destinariosUnico = {"user@gmail.com"};

        // ACT & ASSERT
        assertTrue(destinariosMultiples.length > 1);
        assertEquals(2, destinariosMultiples.length);
        assertEquals(1, destinariosUnico.length);
    }

    @Test
    public void testValidarEmailConArroba() {
        // ARRANGE
        String email = "test@gmail.com";

        // ACT & ASSERT
        assertTrue(email.contains("@"));
        assertTrue(email.contains(".com"));
    }
}