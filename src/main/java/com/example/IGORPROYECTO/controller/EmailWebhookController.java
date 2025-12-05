package com.example.IGORPROYECTO.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.IGORPROYECTO.model.EmailDocument;
import com.example.IGORPROYECTO.service.EmailService;

/**
 * Controlador que recibe el webhook del proveedor de email (Mailgun/SendGrid/etc.)
 * Guarda destinatario y usuarioId usando el correo (ej: "usuario@dominio.com")
 */
@RestController
public class EmailWebhookController {

    private final EmailService service;

    public EmailWebhookController(EmailService service) {
        this.service = service;
    }

    private String extraerEmail(String raw) {
        if (raw == null) return null;
        // intenta extraer entre < >, si no, intenta extraer la primera cadena con @
        Pattern p = Pattern.compile("([a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,})");
        Matcher m = p.matcher(raw);
        if (m.find()) return m.group(1).toLowerCase().trim();
        return raw.trim().toLowerCase();
    }

    @PostMapping("/api/correos/webhook")
    public ResponseEntity<String> inboundWebhook(@RequestBody Map<String, Object> payload) {
        try {
            EmailDocument e = new EmailDocument();

            // Remitente y asunto
            Object from = payload.get("from");
            Object subject = payload.get("subject");
            if (from != null) e.setRemitente(extraerEmail(from.toString()));
            if (subject != null) e.setAsunto(subject.toString());

            // Cuerpo de texto plano (campo típico: body-plain o text)
            Object bodyPlain = payload.get("body-plain");
            if (bodyPlain == null) bodyPlain = payload.get("text");
            if (bodyPlain != null) e.setMensaje(bodyPlain.toString());

            // HTML fallback
            Object bodyHtml = payload.get("body-html");
            if (bodyHtml != null && (e.getMensaje() == null || e.getMensaje().isEmpty())) {
                e.setMensaje(bodyHtml.toString());
            }

            // Extraer destinatario (campo "recipient", "to" o "to[]")
            String correoDest = null;
            Object recipient = payload.get("recipient");
            if (recipient == null) recipient = payload.get("to");
            if (recipient == null) recipient = payload.get("To");
            if (recipient == null) recipient = payload.get("to[]");
            if (recipient != null) {
                correoDest = extraerEmail(recipient.toString());
            } else {
                // intenta extraer desde "envelope" si el proveedor lo envia
                Object envelope = payload.get("envelope");
                if (envelope != null && envelope instanceof Map) {
                    Object to = ((Map<?,?>) envelope).get("to");
                    if (to != null) correoDest = extraerEmail(to.toString());
                }
            }

            // asignar destinatario y usuarioId por correo si lo encontramos
            if (correoDest != null) {
                e.setDestinatario(correoDest);
                e.setUsuarioId(correoDest); // clave para la bandeja: usar el correo
            }

            // Extraer token de proyecto en el asunto: [PROJ-123]
            if (e.getAsunto() != null) {
                Pattern p = Pattern.compile("\\[PROJ-(.*?)\\]");
                Matcher m = p.matcher(e.getAsunto());
                if (m.find()) {
                    String proyectoToken = m.group(1);
                    e.setProyectoId(proyectoToken);
                }
            }

            // Estado y fecha
            e.setEstado("NUEVO");
            e.setFecha(LocalDateTime.now().toString());

            // Guardar en Mongo usando EmailService (save/update)
            service.save(e);

            return ResponseEntity.ok("received");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("error: " + ex.getMessage());
        }
    }
}
