package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Documento;

@Repository
public interface DocumentoRepository extends MongoRepository<Documento, String> {
    
    // Buscar documentos por proyecto
    List<Documento> findByProyectoId(String proyectoId);
    
    // Buscar por nombre de proyecto
    List<Documento> findByNombreProyectoContainingIgnoreCase(String nombreProyecto);
    
    // Buscar por usuario que subió
    List<Documento> findByUsuarioId(String usuarioId);
    
    // Buscar por tipo de archivo
    List<Documento> findByProyectoIdAndTipo(String proyectoId, String tipo);
    
    // Buscar por categoría
    List<Documento> findByProyectoIdAndCategoria(String proyectoId, String categoria);
    
    // Buscar por estado
    List<Documento> findByProyectoIdAndEstado(String proyectoId, String estado);
    
    // Buscar documentos activos de un proyecto
    List<Documento> findByProyectoIdAndEstadoOrderByFechaSubidaDesc(String proyectoId, String estado);
    
    // Contar documentos por proyecto
    Long countByProyectoId(String proyectoId);
}