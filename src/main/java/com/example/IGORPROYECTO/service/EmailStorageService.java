package com.example.IGORPROYECTO.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmailStorageService {

    // Lista donde guardaremos los correos enviados
    private final List<EmailRecord> historialCorreos = new ArrayList<>();

    // Método para guardar el correo enviado
    public void guardarCorreo(String[] destinatarios, String asunto, String mensaje) {
        EmailRecord registro = new EmailRecord(
                destinatarios,
                asunto,
                mensaje,
                LocalDateTime.now()
        );
        historialCorreos.add(registro);
    }

    // Obtener todos los correos almacenados
    public List<EmailRecord> obtenerHistorial() {
        return historialCorreos;
    }

    // Clase interna para representar el correo
    public static class EmailRecord {
        private final String[] destinatarios;
        private final String asunto;
        private final String mensaje;
        private final LocalDateTime fechaEnvio;

        public EmailRecord(String[] destinatarios, String asunto, String mensaje, LocalDateTime fechaEnvio) {
            this.destinatarios = destinatarios;
            this.asunto = asunto;
            this.mensaje = mensaje;
            this.fechaEnvio = fechaEnvio;
        }

        public String[] getDestinatarios() {
            return destinatarios;
        }

        public String getAsunto() {
            return asunto;
        }

        public String getMensaje() {
            return mensaje;
        }

        public LocalDateTime getFechaEnvio() {
            return fechaEnvio;
        }
    }
}
