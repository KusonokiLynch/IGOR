package com.example.IGORPROYECTO.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "documentacion")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Documentacion {

    @Id
    private String id;
    private String nombre;
    private String nombreProyecto;
    private String tipo;
    private String descripcion;
    private Date FechaCreacion;
    private String estado;
    private String propietario;


    
}
