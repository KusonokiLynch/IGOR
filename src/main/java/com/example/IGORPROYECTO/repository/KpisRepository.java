package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Kpi;

@Repository
public interface KpisRepository extends MongoRepository<Kpi, String> {
    // Buscar por nombre de proyecto
    List<Kpi> findByNombreProyecto(String nombreProyecto);
    
    // Buscar por tipo
    List<Kpi> findByTipo(String tipo);
    
    // Buscar por estado
    List<Kpi> findByEstado(String estado);
    
    // Buscar por propietario
    List<Kpi> findByPropietario(String propietario);
    
    // Ordenar por fecha de creación descendente
    List<Kpi> findAllByOrderByFechaCreacionDesc();
    long countByEstado(String estado); // ✅ AGREGADO
}