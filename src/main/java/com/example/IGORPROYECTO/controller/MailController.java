package com.example.IGORPROYECTO.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.exceptions.TemplateProcessingException;

import com.example.IGORPROYECTO.service.EmailSenderService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/mail")
public class MailController {

    private final EmailSenderService emailSenderService;

    public MailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/form")
    public String mostrarFormulario() {
        return "AnalisisYReportes/form";
    }

    @PostMapping("/send")
        public String enviarCorreo(String destinatario, String asunto, String mensaje, String nombre, Model model) {

        try {
        String[] destinatarios = destinatario.split("\\s*,\\s*");

        emailSenderService.enviarCorreoConPlantilla(destinatarios, asunto, mensaje);

        model.addAttribute("respuesta", "Correo enviado con plantilla HTML exitosamente.");
    }   catch (MessagingException e) {
        model.addAttribute("respuesta", "Error al enviar el correo: " + e.getMessage());
    }   catch (TemplateProcessingException e) {
        model.addAttribute("respuesta", "Error en la plantilla HTML: " + e.getMessage());
    }   catch (Exception e) {
        model.addAttribute("respuesta", "Error desconocido: " + e.getMessage());
    }

    return "AnalisisYReportes/form";
    }
    

}
