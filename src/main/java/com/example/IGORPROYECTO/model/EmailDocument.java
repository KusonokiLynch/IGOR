package com.example.IGORPROYECTO.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email")
public class EmailDocument {

    @Id
    private String id;

    private String remitente;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private String cuerpoHtml;

    private String estado;  // NUEVO, LEIDO, ENVIADO, etc.
    private String fecha;   // ISO string o timestamp

    // clave usada para filtrar la bandeja: el correo del receptor
    private String usuarioId;

    private String proyectoId;
    private String tareaId;
    private String clienteId;

    // getters y setters (generar o pegar)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getCuerpoHtml() { return cuerpoHtml; }
    public void setCuerpoHtml(String cuerpoHtml) { this.cuerpoHtml = cuerpoHtml; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getProyectoId() { return proyectoId; }
    public void setProyectoId(String proyectoId) { this.proyectoId = proyectoId; }
    public String getTareaId() { return tareaId; }
    public void setTareaId(String tareaId) { this.tareaId = tareaId; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
}
