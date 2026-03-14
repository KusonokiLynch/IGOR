package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.IGORPROYECTO.service.BrevoEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mail")
public class MailController {

    private final BrevoEmailService brevoEmailService;

    public MailController(BrevoEmailService brevoEmailService) {
        this.brevoEmailService = brevoEmailService;
    }

    @GetMapping("/form")
    public String mostrarFormulario() {
        return "AnalisisYReportes/form";
    }

    // ✅ FIX: Validar datos antes de usarlos, no loguear datos controlados por usuario
    @PostMapping("/send")
    public String enviarCorreo(String destinatario, String asunto, String mensaje, Model model) {

        try {
            // ✅ FIX: Validar entrada del usuario
            if (destinatario == null || destinatario.trim().isEmpty()) {
                model.addAttribute("respuesta", "El correo destinatario es requerido");
                return "AnalisisYReportes/form";
            }
            
            if (asunto == null || asunto.trim().isEmpty()) {
                model.addAttribute("respuesta", "El asunto es requerido");
                return "AnalisisYReportes/form";
            }
            
            if (mensaje == null || mensaje.trim().isEmpty()) {
                model.addAttribute("respuesta", "El mensaje es requerido");
                return "AnalisisYReportes/form";
            }

            String[] destinatarios = destinatario.split("\\s*,\\s*");
            
            // ✅ FIX: No loguear datos del usuario, solo información de seguridad
            log.info("Intento de envío de correo. Cantidad de destinatarios: {}", destinatarios.length);

            brevoEmailService.enviarCorreoConPlantilla(destinatarios, asunto, mensaje);

            model.addAttribute("respuesta", "Correo enviado exitosamente con Brevo.");
            log.info("Correo enviado exitosamente");
            
        } catch (IllegalArgumentException e) {
            // ✅ FIX: Loguear error pero no los datos del usuario
            log.warn("Validación fallida en envío de correo: {}", e.getMessage());
            model.addAttribute("respuesta", "Error: " + e.getMessage());
        } catch (Exception e) {
            // ✅ FIX: Loguear error pero no los datos del usuario
            log.error("Error al enviar correo", e);
            model.addAttribute("respuesta", "Error al enviar el correo. Intente más tarde.");
        }

        return "AnalisisYReportes/form";
    }

}