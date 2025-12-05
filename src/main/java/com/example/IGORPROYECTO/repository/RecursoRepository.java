package com.example.IGORPROYECTO.repository;
import com.example.IGORPROYECTO.model.Recurso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RecursoRepository extends MongoRepository<Recurso, String> {
    List<Recurso> findByDisponibilidad(boolean disponibilidad);    
}
