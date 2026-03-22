package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeticionDTO {
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // ✅ AGREGADO
    private Date fechaCreacion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // ✅ AGREGADO
    private Date fechaSolicitud;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // ✅ AGREGADO - rompía el form
    private Date fechaEstimada;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // ✅ AGREGADO
    private Date fechaActualizacion;
}