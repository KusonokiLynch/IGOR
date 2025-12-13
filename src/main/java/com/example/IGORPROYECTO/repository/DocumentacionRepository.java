package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Documentacion;

@Repository
public interface DocumentacionRepository extends MongoRepository<Documentacion, String> {
    List<Documentacion> findByNombreProyecto(String nombreProyecto);

     // Buscar documentos por estado (ACTIVO o INACTIVO)
    List<Documentacion> findByEstado(String estado);
    
    // Buscar por múltiples estados (útil si necesitas filtrar por varios estados)
    List<Documentacion> findByEstadoIn(List<String> estados);
}
