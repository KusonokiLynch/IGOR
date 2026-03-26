package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {

    private String id;
    private String nombre;
    private String descripcion;
    private String estado;
    private String programa;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFinal; // ✅ CORREGIDO

    private String responsable;
    private String cliente;
    private String serial;

    private Date fechaCreacion;
}