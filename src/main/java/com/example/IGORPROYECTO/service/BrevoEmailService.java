package com.example.IGORPROYECTO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

@Slf4j
@Service
public class BrevoEmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private final TemplateEngine templateEngine;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    // ✅ FIX: Expresión regular para validar emails
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // ✅ FIX: Expresión regular para validar asunto (sin caracteres sospechosos)
    private static final Pattern ASUNTO_PATTERN = 
        Pattern.compile("^[\\w\\s\\-.,ñáéíóú]{1,100}$", Pattern.CASE_INSENSITIVE);

    public BrevoEmailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // ✅ FIX: Validar datos antes de usarlos
    public void enviarCorreoConPlantilla(String[] destinatarios, String asunto, String mensaje) throws Exception {
        
        // Validar destinatarios
        validarDestinatarios(destinatarios);
        
        // Validar asunto
        validarAsunto(asunto);
        
        // Validar mensaje (no vacío)
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        
        log.info("Iniciando envío de correo. Número de destinatarios: {}", destinatarios.length);
        
        // Generar HTML desde la plantilla
        Context context = new Context();
        context.setVariable("mensaje", mensaje);
        String html = templateEngine.process("Correos/Plantilla", context);

        // Construir el JSON para Brevo
        String jsonBody = construirJsonBrevo(destinatarios, asunto, html);

        // Hacer la solicitud HTTP POST a Brevo
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("api-key", brevoApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201 && response.statusCode() != 200) {
                log.error("Error de respuesta de Brevo. Status code: {}", response.statusCode());
                throw new RuntimeException("Error al enviar correo con Brevo");
            }
            
            log.info("Correo enviado exitosamente con Brevo");
        } catch (IOException | InterruptedException e) {
            log.error("Error en la comunicación con Brevo", e);
            throw new RuntimeException("Error al conectar con el servicio de correo", e);
        }
    }

    // ✅ FIX: Validar destinatarios
    private void validarDestinatarios(String[] destinatarios) throws IllegalArgumentException {
        if (destinatarios == null || destinatarios.length == 0) {
            throw new IllegalArgumentException("Debe proporcionar al menos un destinatario");
        }
        
        for (String email : destinatarios) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Correo vacío detectado");
            }
            
            String emailTrimmed = email.trim();
            if (!EMAIL_PATTERN.matcher(emailTrimmed).matches()) {
                throw new IllegalArgumentException("Formato de correo inválido");
            }
        }
    }

    // ✅ FIX: Validar asunto
    private void validarAsunto(String asunto) throws IllegalArgumentException {
        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto no puede estar vacío");
        }
        
        if (asunto.length() > 100) {
            throw new IllegalArgumentException("El asunto es muy largo (máximo 100 caracteres)");
        }
        
        if (!ASUNTO_PATTERN.matcher(asunto).matches()) {
            throw new IllegalArgumentException("El asunto contiene caracteres no permitidos");
        }
    }

    private String construirJsonBrevo(String[] destinatarios, String asunto, String html) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sender\": {\"name\": \"IGOR\", \"email\": \"igorsoftwar3@gmail.com\"},");
        sb.append("\"to\": [");

        for (int i = 0; i < destinatarios.length; i++) {
            sb.append("{\"email\": \"").append(destinatarios[i].trim()).append("\"}");
            if (i < destinatarios.length - 1) {
                sb.append(",");
            }
        }

        sb.append("],");
        sb.append("\"subject\": \"").append(asunto.replace("\"", "\\\"")).append("\",");
        sb.append("\"htmlContent\": \"").append(html.replace("\"", "\\\"").replace("\n", "\\n")).append("\"");
        sb.append("}");

        return sb.toString();
    }
}