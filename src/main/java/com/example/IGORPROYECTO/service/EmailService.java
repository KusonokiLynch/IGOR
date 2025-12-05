package com.example.IGORPROYECTO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.EmailDocument;
import com.example.IGORPROYECTO.repository.EmailRepository;

@Service
public class EmailService {

    private final EmailRepository repo;

    public EmailService(EmailRepository repo) {
        this.repo = repo;
    }

    // Bandeja del usuario (filtra por destinatario = correo)
    public List<EmailDocument> listInbox(String usuarioCorreo) {
        return repo.findByDestinatarioOrderByFechaDesc(usuarioCorreo);
    }

    // Buscar correo por ID
    public Optional<EmailDocument> getById(String id) {
        return repo.findById(id);
    }

    // Actualizar correo
    public EmailDocument update(EmailDocument email) {
        return repo.save(email);
    }

    // Guardar correo (webhook) — si falta usuarioId, lo rellenamos con destinatario si existe
    public EmailDocument save(EmailDocument email) {
        if ((email.getUsuarioId() == null || email.getUsuarioId().isBlank()) && email.getDestinatario() != null) {
            email.setUsuarioId(email.getDestinatario());
        }
        return repo.save(email);
    }

    // Enviar correo interno IGOR (usa correo del destinatario)
    public void enviarInterno(String remitente, String correoDestinatario, String asunto, String mensaje) {
        EmailDocument e = new EmailDocument();
        e.setRemitente(remitente);
        e.setDestinatario(correoDestinatario);
        e.setAsunto(asunto);
        e.setMensaje(mensaje);
        e.setEstado("ENVIADO");
        e.setFecha(String.valueOf(System.currentTimeMillis()));
        e.setUsuarioId(correoDestinatario); // importante: usar correo como clave de bandeja
        repo.save(e);
    }
}
