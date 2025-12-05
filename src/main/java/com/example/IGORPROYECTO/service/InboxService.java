package com.example.IGORPROYECTO.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.IGORPROYECTO.model.EmailMessage;
import com.example.IGORPROYECTO.repository.EmailMessageRepository;

@Service
public class InboxService {

    @Autowired
    private EmailMessageRepository emailRepo;

    public List<EmailMessage> listInbox(String usuarioId) {
        return emailRepo.findByUsuarioId(usuarioId);
    }

    public EmailMessage getById(String id) {
        return emailRepo.findById(id).orElse(null);
    }

    public EmailMessage update(EmailMessage email) {
        return emailRepo.save(email);
    }

    // Para guardar un correo en la bandeja
    public void guardarEnBandeja(EmailMessage msg) {
        emailRepo.save(msg);
    }
}
