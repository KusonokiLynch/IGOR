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
    private String tipo;
    private String descripcion;
    private String clienteId;
    private String proyectoId;
    private String prioridad;
    private String estado;
    private Integer progreso;
    private String solicitante;
    private String comentarios;

    private Date fechaCreacion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaSolicitud;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEstimada;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // ✅ AGREGADO
    private Date fechaActualizacion;
}