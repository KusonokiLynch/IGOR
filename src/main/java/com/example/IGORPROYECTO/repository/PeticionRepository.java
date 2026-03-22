package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.IGORPROYECTO.model.Peticion;

@Repository
public interface PeticionRepository extends MongoRepository<Peticion, String> {
    List<Peticion> findByEstado(String estado);
    List<Peticion> findBySolicitante(String solicitante);
    List<Peticion> findByClienteId(String clienteId);
    List<Peticion> findByTipo(String tipo);
    List<Peticion> findByProyectoId(String proyectoId);
    List<Peticion> findAllByOrderByFechaCreacionDesc();
}