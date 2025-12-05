package com.example.IGORPROYECTO.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.repository.DocumentacionRepository;


@Controller
public class DocumentacionController {

    @Autowired
    private DocumentacionRepository repo;

      // Mostrar el formulario en /Documentacion
    @GetMapping("/documentacion")
    public String mostrarDocumentacion(Model model) {
        model.addAttribute("documentacion", new Documentacion());
        // Aquí Thymeleaf buscará templates/Documentacion/Documentacion.html
        return "Documentacion/Documentacion";
    }

    @GetMapping("NuevoDocumento")
    public String mostrarFormulario(Model model) {
        model.addAttribute("documentacion", new Documentacion());
        // Aquí Thymeleaf buscará templates/Documentacion/Documentacion.html
        return "Documentacion/NuevoDocumento";
    }

    // Guardar el documento en /Documentacion/guardar
    @PostMapping("/Documentacion/guardar")
    public String guardarDocumento(@ModelAttribute Documentacion documentacion) {
        documentacion.setFechaCreacion(new Date());
        repo.save(documentacion);
         // Redirige a /Documentacion para mostrar formulario limpio después de guardar
        return "redirect:/Documentacion";
    }
    //Apartado EditarDocumentacion html
        // Mostrar la vista de edición en /editarDocumentacion
        @GetMapping("/Documentacion/editar")
        public String mostrarHistorial(Model model) {
        List<Documentacion> documentacion = repo.findAll(); // Obtener todos los proyectos
        model.addAttribute("documentacion", documentacion);
        return "Documentacion/EditarDocumento";
    }

    @PostMapping("/Documentacion/editar")
    public String editarDocumento(@ModelAttribute Documentacion documentacion) {
        documentacion.setFechaCreacion(new Date());
        // Puedes validar si existe antes de guardar
        repo.save(documentacion);
        return "redirect:/EditarDocumento";
    }


}
