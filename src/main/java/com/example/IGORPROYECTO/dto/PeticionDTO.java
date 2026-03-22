package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeticionDTO {
    private String id;
    private String titulo;
    private String tipo; // ✅ AGREGADO
    private String descripcion;
    private String clienteId; // ✅ AGREGADO
    private String proyectoId; // ✅ AGREGADO
    private String prioridad;
    private String estado;
    private Integer progreso;
    
    private Date fechaCreacion; // ✅ CAMBIADO: Date en lugar de LocalDateTime
    private Date fechaSolicitud; // ✅ AGREGADO
    private Date fechaEstimada; // ✅ AGREGADO
    private Date fechaActualizacion; // ✅ CAMBIADO: Date en lugar de LocalDateTime
    
    private String solicitante; // ✅ AGREGADO
    private String comentarios; // ✅ AGREGADO
}