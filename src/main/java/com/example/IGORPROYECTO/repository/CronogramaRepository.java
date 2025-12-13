package com.example.IGORPROYECTO.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.EventoCronograma;
import com.example.IGORPROYECTO.model.EventoCronograma.TipoEvento;

@Repository
public interface CronogramaRepository extends MongoRepository<EventoCronograma, String> {
    
    // Buscar todos los eventos de un proyecto ordenados por fecha
    List<EventoCronograma> findByProyectoIdOrderByFechaDesc(String proyectoId);
    
    // Buscar eventos de un proyecto por nombre
    List<EventoCronograma> findByNombreProyectoContainingIgnoreCaseOrderByFechaDesc(String nombreProyecto);
    
    // Buscar eventos por tipo (ej: solo reuniones)
    List<EventoCronograma> findByProyectoIdAndTipoOrderByFechaDesc(String proyectoId, TipoEvento tipo);
    
    // Buscar eventos en un rango de fechas
    List<EventoCronograma> findByProyectoIdAndFechaBetweenOrderByFechaDesc(
        String proyectoId, 
        LocalDateTime fechaInicio, 
        LocalDateTime fechaFin
    );
    
    // Contar eventos por proyecto
    Long countByProyectoId(String proyectoId);
}