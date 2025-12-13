package com.example.IGORPROYECTO.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailSenderService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarCorreoConPlantilla(String[] destinatarios, String asunto, String mensaje) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(destinatarios);
        helper.setSubject(asunto);

        Context context = new Context();
        context.setVariable("mensaje", mensaje);
        

        String html = templateEngine.process("Correos/Plantilla", context);

        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
    
}
