package com.example.IGORPROYECTO.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "peticiones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peticion {

    @Id
    private String id;
    private String titulo;
    private String tipo; // "Reporte", "Dashboard", "Análisis", etc.
    private String descripcion;
    private String clienteId; // ID del cliente que hace la petición
    private String proyectoId; // proyecto relacionado
    private String prioridad; // "Alta", "Media", "Baja"
    private String estado = "Pendiente"; // "Pendiente", "En Proceso", "Completado", "Cancelado"
    
    private Date fechaCreacion = new Date();
    
    // Campos adicionales
    private String solicitante;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaSolicitud;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEstimada;

    private Integer progreso; // 0-100
    private String comentarios;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaActualizacion;
}