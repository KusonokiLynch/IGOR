package com.example.IGORPROYECTO.service;

import com.example.IGORPROYECTO.model.Recurso;
import com.example.IGORPROYECTO.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoService(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    public List<Recurso> menuRecurso() {
        return recursoRepository.findAll();
    }

    public List<Recurso> consultarRecursos() {
        return recursoRepository.findAll();
    }

    public List<Recurso> recursoDisponible() {
        return recursoRepository.findByDisponibilidad(true);
    }

    public Recurso guardar(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    public Optional<Recurso> buscarPorId(String id) {
        return recursoRepository.findById(id);
    }

    public void eliminar(String id) {
        recursoRepository.deleteById(id);
    }

    public Recurso asignar(String recursoId, String usuarioId) {
        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + recursoId));

        if (!recurso.getAsignadoA().contains(usuarioId)) {
            recurso.getAsignadoA().add(usuarioId);
        }

        return recursoRepository.save(recurso);
    }

    public Recurso desasignar(String recursoId, String usuarioId) {
        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + recursoId));

        recurso.getAsignadoA().remove(usuarioId);
        return recursoRepository.save(recurso);
    }
}