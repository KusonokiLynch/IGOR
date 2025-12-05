package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.IGORPROYECTO.model.EmailDocument;

public interface EmailRepository extends MongoRepository<EmailDocument, String> {

    // Correos recibidos
    List<EmailDocument> findByDestinatario(String destinatario);

    // Correos recibidos ordenados por fecha (desc)
    List<EmailDocument> findByDestinatarioOrderByFechaDesc(String destinatario);
}
