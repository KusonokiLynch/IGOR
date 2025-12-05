package com.example.IGORPROYECTO.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "recursos")
public class Recurso {

    @Id
    private String id;
    private String nombre;         
    private String tipo;           
    private int cantidad;          
    private boolean disponibilidad; 
    private List<String> asignadoA = new ArrayList<>();
}