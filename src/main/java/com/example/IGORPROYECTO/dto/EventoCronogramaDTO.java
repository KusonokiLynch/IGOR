package com.example.IGORPROYECTO.dto;

import com.example.IGORPROYECTO.model.EventoCronograma.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoCronogramaDTO {
    private String id;
    private String proyectoId;
    private String nombreProyecto;
    private LocalDateTime fecha;
    private TipoEvento tipo;
    private String descripcion;
    private String detalles;
    private String creadoPor;
    private LocalDateTime fechaCreacion;
}