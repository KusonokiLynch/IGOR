package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeticionDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private String estado;
    private Integer progreso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String prioridad;
}