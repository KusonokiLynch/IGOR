package com.example.IGORPROYECTO.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Proyecto;

@Repository
public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
    // Aquí puedes definir métodos personalizados si los necesitas más adelante
}
