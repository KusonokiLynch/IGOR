package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.IGORPROYECTO.service.BrevoEmailService;

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

    @PostMapping("/send")
    public String enviarCorreo(String destinatario, String asunto, String mensaje, Model model) {

        try {
            String[] destinatarios = destinatario.split("\\s*,\\s*");

            brevoEmailService.enviarCorreoConPlantilla(destinatarios, asunto, mensaje);

            model.addAttribute("respuesta", "Correo enviado exitosamente con Brevo.");
        } catch (Exception e) {
            model.addAttribute("respuesta", "Error al enviar el correo: " + e.getMessage());
        }

        return "AnalisisYReportes/form";
    }

}