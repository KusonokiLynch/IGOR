package com.example.IGORPROYECTO.controller;

import com.example.IGORPROYECTO.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // Vista HTML con gráficos
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        Map<String, Object> resumen = reporteService.obtenerResumenGeneral();
        model.addAttribute("resumen", resumen);
        return "dashboard";
    }

    // API REST para obtener datos (por si quieres consumirlos desde otro frontend)
    @GetMapping("/api/estadisticas")
    @ResponseBody
    public Map<String, Object> obtenerEstadisticas() {
        return reporteService.obtenerResumenGeneral();
    }

    @GetMapping("/api/por-estado")
    @ResponseBody
    public Map<String, Long> obtenerPorEstado() {
        return reporteService.obtenerProyectosPorEstado();
    }

    @GetMapping("/api/por-responsable")
    @ResponseBody
    public Map<String, Long> obtenerPorResponsable() {
        return reporteService.obtenerProyectosPorResponsable();
    }
}