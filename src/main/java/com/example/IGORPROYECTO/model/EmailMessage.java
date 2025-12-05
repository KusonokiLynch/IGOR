package com.example.IGORPROYECTO.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "emails")
public class EmailMessage {

    @Id
    private String id;

    private String usuarioId;
    private String remitente;
    private String asunto;
    private String mensaje;
    private String estado = "NO_LEIDO";

    private String proyectoId;
    private String tareaId;
    private String clienteId;

    private String fechaEnvio;

    // Getters y Setters
    public String getId() { return id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }

    public String getTareaId() { return tareaId; }
    public void setTareaId(String tareaId) { this.tareaId = tareaId; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
