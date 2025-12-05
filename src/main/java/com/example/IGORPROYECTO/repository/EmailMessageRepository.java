package com.example.IGORPROYECTO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.IGORPROYECTO.model.EmailMessage;

public interface EmailMessageRepository extends MongoRepository<EmailMessage, String> {

    List<EmailMessage> findByUsuarioId(String usuarioId);

}
