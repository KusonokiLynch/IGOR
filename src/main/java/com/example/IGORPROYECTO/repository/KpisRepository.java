package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Kpis;

@Repository
public interface KpisRepository extends MongoRepository<Kpis, String> {
    // Buscar por nombre de proyecto
    List<Kpis> findByNombreProyecto(String nombreProyecto);
    
    // Buscar por tipo
    List<Kpis> findByTipo(String tipo);
    
    // Buscar por estado
    List<Kpis> findByEstado(String estado);
    
    // Buscar por propietario
    List<Kpis> findByPropietario(String propietario);
    
    // Ordenar por fecha de creación descendente
    List<Kpis> findAllByOrderByFechaCreacionDesc();
}