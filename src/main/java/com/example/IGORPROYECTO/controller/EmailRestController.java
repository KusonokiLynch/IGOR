package com.example.IGORPROYECTO.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.IGORPROYECTO.model.EmailDocument;
import com.example.IGORPROYECTO.service.EmailService;

@RestController
@RequestMapping("/api/correos")
public class EmailRestController {
    private final EmailService service;

    public EmailRestController(EmailService service) {
        this.service = service;
    }

    @GetMapping("/recibidos")
    public ResponseEntity<List<EmailDocument>> inbox(Authentication authentication) {
        String usuarioId = authentication.getName();
        return ResponseEntity.ok(service.listInbox(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailDocument> getById(@PathVariable String id, Authentication auth) {
        var opt = service.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        EmailDocument e = opt.get();
        if (!e.getUsuarioId().equals(auth.getName())) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(e);
    }

    @PutMapping("/{id}/marcar")
    public ResponseEntity<EmailDocument> marcar(@PathVariable String id,
                                                @RequestBody Map<String,String> body,
                                                Authentication auth) {
        var opt = service.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        EmailDocument e = opt.get();
        if (!e.getUsuarioId().equals(auth.getName())) return ResponseEntity.status(403).build();
        e.setEstado(body.getOrDefault("estado", "leido"));
        return ResponseEntity.ok(service.update(e));
    }

    @PutMapping("/{id}/asociar")
    public ResponseEntity<EmailDocument> asociar(@PathVariable String id,
                                                 @RequestBody Map<String,String> body,
                                                 Authentication auth) {
        var opt = service.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        EmailDocument e = opt.get();
        if (!e.getUsuarioId().equals(auth.getName())) return ResponseEntity.status(403).build();
        e.setProyectoId(body.get("proyectoId"));
        e.setTareaId(body.get("tareaId"));
        e.setClienteId(body.get("clienteId"));
        return ResponseEntity.ok(service.update(e));
    }
}
