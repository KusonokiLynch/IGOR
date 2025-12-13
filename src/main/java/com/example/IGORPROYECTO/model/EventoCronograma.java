package com.example.IGORPROYECTO.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "eventos_cronograma")
public class EventoCronograma {
    
    @Id
    private String id;
    
    private String proyectoId;
    
    private String nombreProyecto;
    
    private LocalDateTime fecha;
    
    private TipoEvento tipo;
    
    private String descripcion;
    
    private String detalles; // Información adicional (hora, ubicación, nombre archivo, etc.)
    
    private String creadoPor; // ID del usuario que creó el evento
    
    private LocalDateTime fechaCreacion;
    
    public enum TipoEvento {
        REUNION,
        DOCUMENTO,
        HITO,
        CAMBIO_ESTADO
    }
}