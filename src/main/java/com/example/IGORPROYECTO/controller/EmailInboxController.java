package com.example.IGORPROYECTO.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.IGORPROYECTO.model.EmailMessage;
import com.example.IGORPROYECTO.service.InboxService;

@RestController
@RequestMapping("/api/emails")
public class EmailInboxController {

    @Autowired
    private InboxService inboxService;

    @GetMapping("/inbox/{usuarioId}")
    public List<EmailMessage> listInbox(@PathVariable String usuarioId) {
        return inboxService.listInbox(usuarioId);
    }

    @GetMapping("/{id}")
    public EmailMessage getById(@PathVariable String id) {
        return inboxService.getById(id);
    }

    @PutMapping("/{id}/estado/{estado}")
    public EmailMessage cambiarEstado(@PathVariable String id, @PathVariable String estado) {
        EmailMessage email = inboxService.getById(id);
        if (email == null) return null;

        email.setEstado(estado);
        return inboxService.update(email);
    }

    @PutMapping("/{id}/proyecto/{proyectoId}")
    public EmailMessage asignarProyecto(@PathVariable String id, @PathVariable String proyectoId) {
        EmailMessage email = inboxService.getById(id);
        if (email == null) return null;

        email.setProyectoId(proyectoId);
        return inboxService.update(email);
    }

    @PutMapping("/{id}/tarea/{tareaId}")
    public EmailMessage asignarTarea(@PathVariable String id, @PathVariable String tareaId) {
        EmailMessage email = inboxService.getById(id);
        if (email == null) return null;

        email.setTareaId(tareaId);
        return inboxService.update(email);
    }

    @PutMapping("/{id}/cliente/{clienteId}")
    public EmailMessage asignarCliente(@PathVariable String id, @PathVariable String clienteId) {
        EmailMessage email = inboxService.getById(id);
        if (email == null) return null;

        email.setClienteId(clienteId);
        return inboxService.update(email);
    }
}
