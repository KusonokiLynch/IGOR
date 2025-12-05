package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Peticion;

@Repository
public interface PeticionRepository extends MongoRepository<Peticion, String> {
    // Buscar por estado
    List<Peticion> findByEstado(String estado);
    
    // Buscar por solicitante
    List<Peticion> findBySolicitante(String solicitante);
    
    // Buscar por tipo
    List<Peticion> findByTipo(String tipo);
    
    // Buscar por proyecto
    List<Peticion> findByProyecto(String proyecto);
    
    // Ordenar por fecha de solicitud descendente
    List<Peticion> findAllByOrderByFechaSolicitudDesc();
}