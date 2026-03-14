package com.example.IGORPROYECTO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class BrevoEmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private final TemplateEngine templateEngine;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BrevoEmailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void enviarCorreoConPlantilla(String[] destinatarios, String asunto, String mensaje) throws Exception {
        
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

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201 && response.statusCode() != 200) {
            throw new RuntimeException("Error al enviar correo con Brevo: " + response.body());
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