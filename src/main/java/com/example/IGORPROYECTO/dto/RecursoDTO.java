package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoDTO {
    private String id;
    private String nombre;
    private String tipo;
    private int cantidad;
    private boolean disponibilidad;
    private List<String> asignadoA = new ArrayList<>();
}