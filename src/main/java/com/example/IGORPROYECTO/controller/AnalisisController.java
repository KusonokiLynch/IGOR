package com.example.IGORPROYECTO.controller;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.IGORPROYECTO.model.Documentacion;
import com.example.IGORPROYECTO.model.Kpi;
import com.example.IGORPROYECTO.model.Peticion;
import com.example.IGORPROYECTO.model.Proyecto;

import com.example.IGORPROYECTO.dto.KpiDTO;
import com.example.IGORPROYECTO.dto.PeticionDTO;

import com.example.IGORPROYECTO.repository.DocumentacionRepository;
import com.example.IGORPROYECTO.repository.ProyectoRepository;

import com.example.IGORPROYECTO.service.AnalisisService;
import com.example.IGORPROYECTO.service.PdfService;
import com.example.IGORPROYECTO.service.ProyectoService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/analisis")
@RequiredArgsConstructor
public class AnalisisController {

    private final AnalisisService analisisService;
    private final ProyectoService proyectoService;
    private final PdfService pdfService;
    private final ProyectoRepository proyectoRepository;
    private final DocumentacionRepository documentacionRepository;


    // ==================== REPORTES / GENERAR PDF ====================
    @GetMapping("/reporte")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proyectos", proyectoRepository.findAll());
        return "AnalisisYReportes/generarReporte";
    }

    // Generar PDF
    @GetMapping("/generar-pdf")
    public void generarReporte(@RequestParam("idProyecto") String idProyecto,
                               HttpServletResponse response) {
        try {
            Optional<Proyecto> proyectoOpt = proyectoRepository.findById(idProyecto);

            if (proyectoOpt.isPresent()) {
                Proyecto proyecto = proyectoOpt.get();

                // Buscar documentos relacionados
                List<Documentacion> documentos = documentacionRepository.findByNombreProyecto(proyecto.getNombre());

                // Llamada al servicio que construye el PDF
                pdfService.exportProjectPdfWithDocs(response, proyecto, documentos);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Proyecto no encontrado");
            }

        } catch (Exception e) {
            log.error("Error generando PDF para proyecto: {}", idProyecto, e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error al generar PDF");
            } catch (Exception ignored) {}
        }
    }

    // ==================== MENÚ PRINCIPAL ====================
    @GetMapping("")
    public String menuAnalisis(Model model) {
        AnalisisService.EstadisticasDTO stats = analisisService.obtenerEstadisticas();
        model.addAttribute("estadisticas", stats);
        return "AnalisisYReportes/analisis";
    }

    // ==================== KPIs ====================
    @GetMapping("/kpi")
    public String registrarKPI(Model model) {
        model.addAttribute("kpi", new KpiDTO());
        model.addAttribute("kpis", analisisService.obtenerTodosKPIs());
        return "AnalisisYReportes/registrarKPI";
    }

    @PostMapping("/kpi/guardar")
    public String guardarKPI(@ModelAttribute KpiDTO kpiDTO, RedirectAttributes redirectAttributes) {
        try {
            Kpi kpi = convertirDTOaKpi(kpiDTO);
            analisisService.crearKPI(kpi);
            redirectAttributes.addFlashAttribute("mensaje", "KPI registrado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al registrar KPI", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar KPI: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/analisis/kpi";
    }

    @GetMapping("/kpi/editar/{id}")
    public String editarKPI(@PathVariable String id, Model model) {
        try {
            Optional<Kpi> kpiOpt = analisisService.obtenerKPIPorId(id);
            Kpi kpi = kpiOpt.orElseThrow(() -> new RuntimeException("KPI no encontrado"));

            KpiDTO kpiDTO = convertirKpiaDTO(kpi);
            model.addAttribute("kpi", kpiDTO);
            model.addAttribute("kpis", analisisService.obtenerTodosKPIs());
        } catch (Exception e) {
            log.error("Error al editar KPI: {}", id, e);
            model.addAttribute("error", "KPI no encontrado");
        }
        return "AnalisisYReportes/registrarKPI";
    }

    @PostMapping("/kpi/actualizar")
    public String actualizarKPI(@ModelAttribute KpiDTO kpiDTO, RedirectAttributes redirectAttributes) {
        try {
            Kpi kpi = convertirDTOaKpi(kpiDTO);
            analisisService.actualizarKPI(kpi);
            redirectAttributes.addFlashAttribute("mensaje", "KPI actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al actualizar KPI", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar KPI: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/analisis/kpi";
    }

    @GetMapping("/kpi/eliminar/{id}")
    public String eliminarKPI(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            analisisService.eliminarKPI(id);
            redirectAttributes.addFlashAttribute("mensaje", "KPI eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar KPI: {}", id, e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar KPI: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/analisis/kpi";
    }

    // ==================== PETICIONES ====================
    @GetMapping("/peticiones")
    public String hacerPeticiones(Model model) {
        log.info("GET /peticiones - Cargando formulario");
        try {
            model.addAttribute("peticion", new PeticionDTO());

            List<Proyecto> proyectos = proyectoRepository.findAll();
            log.info("Proyectos cargados: {}", proyectos.size());

            model.addAttribute("proyectos", proyectos);
            return "AnalisisYReportes/hacerPeticion";

        } catch (Exception e) {
            log.error("Error cargando formulario de peticiones", e);
            model.addAttribute("error", "Error cargando el formulario");
            return "error";
        }
    }

    @PostMapping("/peticiones/guardar")
    public String guardarPeticion(@ModelAttribute PeticionDTO peticionDTO, RedirectAttributes redirectAttributes) {
        log.info("========== POST /peticiones/guardar INICIADO ==========");

        try {
            log.info("STEP 1 - Datos recibidos del formulario:");
            log.info("  - Título: {}", peticionDTO.getTitulo());
            log.info("  - Tipo: {}", peticionDTO.getTipo());
            log.info("  - Prioridad: {}", peticionDTO.getPrioridad());
            log.info("  - Solicitante: {}", peticionDTO.getSolicitante());
            log.info("  - Descripción: {}", peticionDTO.getDescripcion() != null ? "✅ Presente" : "❌ NULL");

            // Validaciones básicas
            if (peticionDTO.getTitulo() == null || peticionDTO.getTitulo().trim().isEmpty()) {
                throw new IllegalArgumentException("El título es obligatorio");
            }
            if (peticionDTO.getDescripcion() == null || peticionDTO.getDescripcion().trim().isEmpty()) {
                throw new IllegalArgumentException("La descripción es obligatoria");
            }

            log.info("STEP 2 - Convirtiendo DTO a Petición...");
            Peticion peticion = convertirDTOaPeticion(peticionDTO);
            log.info("  ✅ Petición creada");
            log.info("  - ID: {}", peticion.getId());
            log.info("  - Estado: {}", peticion.getEstado());
            log.info("  - Progreso: {}", peticion.getProgreso());

            log.info("STEP 3 - Guardando en la BD...");
            analisisService.crearPeticion(peticion);
            log.info("  ✅ Petición guardada exitosamente");
            log.info("  - ID generado: {}", peticion.getId());

            redirectAttributes.addFlashAttribute("mensaje", "✅ Petición creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            log.info("========== GUARDADO COMPLETADO ==========");
            return "redirect:/analisis/solicitud";

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Error de validación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/analisis/peticiones";

        } catch (Exception e) {
            log.error("❌ Error al guardar petición", e);
            log.error("Tipo de error: {}", e.getClass().getName());
            log.error("Mensaje: {}", e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("mensaje", "Error al crear petición: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/analisis/peticiones";
        }
    }

    // ==================== SOLICITUDES/ESTADO ====================
    @GetMapping("/solicitud")
    public String solicitudPeticion(Model model, @RequestParam(required = false) String estado) {
        if (estado != null && !estado.isEmpty()) {
            model.addAttribute("peticiones", analisisService.obtenerPeticionesPorEstado(estado));
            model.addAttribute("filtroActivo", estado);
        } else {
            model.addAttribute("peticiones", analisisService.obtenerTodasPeticiones());
        }
        return "AnalisisYReportes/solicitudPeticion";
    }

    @GetMapping("/solicitud/detalle/{id}")
    public String detallePeticion(@PathVariable String id, Model model) {
        try {
            Optional<Peticion> peticionOpt = analisisService.obtenerPeticionPorId(id);
            Peticion peticion = peticionOpt.orElseThrow(() -> new RuntimeException("Petición no encontrada"));
            model.addAttribute("peticion", peticion);
        } catch (Exception e) {
            log.error("Error al obtener detalle de petición: {}", id, e);
            model.addAttribute("error", "Petición no encontrada");
        }
        return "AnalisisYReportes/detallePeticion";
    }

    @PostMapping("/solicitud/actualizar-estado")
    public String actualizarEstadoPeticion(
            @RequestParam String id,
            @RequestParam String estado,
            @RequestParam(required = false) Integer progreso,
            RedirectAttributes redirectAttributes) {
        try {
            analisisService.actualizarEstadoPeticion(id, estado, progreso);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al actualizar estado de petición: {}", id, e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/analisis/solicitud";
    }

    @GetMapping("/solicitud/eliminar/{id}")
    public String eliminarPeticion(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            analisisService.eliminarPeticion(id);
            redirectAttributes.addFlashAttribute("mensaje", "Petición eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            log.error("Error al eliminar petición: {}", id, e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar petición: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/analisis/solicitud";
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO <-> ENTIDAD ====================

    private Kpi convertirDTOaKpi(KpiDTO dto) {
        Kpi kpi = new Kpi();
        kpi.setId(dto.getId());
        kpi.setNombre(dto.getNombre());
        kpi.setNombreProyecto(dto.getNombreProyecto());
        kpi.setTipo(dto.getTipo());
        kpi.setDescripcion(dto.getDescripcion());
        kpi.setFechaCreacion(dto.getFechaCreacion());
        kpi.setEstado(dto.getEstado());
        kpi.setPropietario(dto.getPropietario());
        return kpi;
    }

    private KpiDTO convertirKpiaDTO(Kpi kpi) {
        KpiDTO dto = new KpiDTO();
        dto.setId(kpi.getId());
        dto.setNombre(kpi.getNombre());
        dto.setNombreProyecto(kpi.getNombreProyecto());
        dto.setTipo(kpi.getTipo());
        dto.setDescripcion(kpi.getDescripcion());
        dto.setFechaCreacion(kpi.getFechaCreacion());
        dto.setEstado(kpi.getEstado());
        dto.setPropietario(kpi.getPropietario());
        return dto;
    }

    private Peticion convertirDTOaPeticion(PeticionDTO dto) {
        log.info("🔄 Iniciando conversión DTO → Petición");

        Peticion peticion = new Peticion();
        peticion.setId(dto.getId());
        peticion.setTitulo(dto.getTitulo());
        peticion.setTipo(dto.getTipo());
        peticion.setDescripcion(dto.getDescripcion());
        peticion.setClienteId(dto.getClienteId());
        peticion.setProyectoId(dto.getProyectoId());
        peticion.setSolicitante(dto.getSolicitante());
        peticion.setPrioridad(dto.getPrioridad());
        peticion.setComentarios(dto.getComentarios());

        // Inicializar estado y progreso con valores por defecto si vienen null
        peticion.setEstado(dto.getEstado() != null ? dto.getEstado() : "Pendiente");
        peticion.setProgreso(dto.getProgreso() != null ? dto.getProgreso() : 0);

        // Inicializar fechas con null checks
        Date ahora = new Date();
        peticion.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : ahora);
        peticion.setFechaSolicitud(dto.getFechaSolicitud() != null ? dto.getFechaSolicitud() : ahora);
        peticion.setFechaActualizacion(dto.getFechaActualizacion() != null ? dto.getFechaActualizacion() : ahora);

        // ✅ FIX: null check en fechaEstimada — campo opcional del formulario
        if (dto.getFechaEstimada() != null) {
            peticion.setFechaEstimada(dto.getFechaEstimada());
        }

        log.info("✅ Conversión completada");
        log.info("  - Estado final: {}", peticion.getEstado());
        log.info("  - Progreso final: {}", peticion.getProgreso());

        return peticion;
    }

}