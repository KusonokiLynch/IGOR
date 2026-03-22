package com.example.IGORPROYECTO.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDTO {
    private String id;
    private String nombre;
    private String nombreProyecto;
    private String tipo;
    private String descripcion;
    private Date fechaCreacion;
    private String estado;
    private String propietario;
}