package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpisDTO {
    private String id;
    private String nombre;
    private String descripcion;
    private Double valor;
    private String unidad;
    private String estado;
}