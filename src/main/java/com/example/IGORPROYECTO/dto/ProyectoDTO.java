package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {
    private String id;
    private String nombre;
    private String descripcion;
    private String estado;
    private String programa;
    private Date fechaInicio;
    private String fechaFinal;
    private String responsable;
    private String cliente;
    private String serial;
    private Date fechaCreacion;
}