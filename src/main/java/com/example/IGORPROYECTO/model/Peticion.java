package com.example.IGORPROYECTO.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Peticiones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peticion {

    @Id
    private String id;
    private String titulo;
    private String tipo; // "Reporte", "Análisis", "Dashboard", etc.
    private String descripcion;
    private String solicitante;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaSolicitud;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEstimada;

    private String estado; // "Pendiente", "En Proceso", "Completado", "Cancelado"
    private Integer progreso; // 0-100
    private String prioridad; // "Alta", "Media", "Baja"
    private String proyecto; // Proyecto relacionado
    private String comentarios;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaActualizacion;
}
