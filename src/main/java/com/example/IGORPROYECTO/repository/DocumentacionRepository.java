package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Documentacion;

@Repository
public interface DocumentacionRepository extends MongoRepository<Documentacion, String> {
    List<Documentacion> findByNombreProyecto(String nombreProyecto);
}
