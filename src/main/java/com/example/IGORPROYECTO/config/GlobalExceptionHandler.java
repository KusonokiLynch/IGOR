package com.example.IGORPROYECTO.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;

/**
 * ✅ MANEJADOR GLOBAL DE EXCEPCIONES
 * 
 * Este componente captura TODAS las excepciones no manejadas
 * y las muestra de forma útil al usuario en lugar de error 500 genérico.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de validación (IllegalArgumentException)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e,
                                       RedirectAttributes redirectAttributes) {
        log.warn("⚠️ Error de validación: {}", e.getMessage());
        
        redirectAttributes.addFlashAttribute("mensaje", "❌ " + e.getMessage());
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        
        return "redirect:/";
    }

    /**
     * Maneja excepciones de recurso no encontrado
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e,
                                        RedirectAttributes redirectAttributes) {
        log.error("❌ Error en tiempo de ejecución: {}", e.getMessage());
        
        String mensaje = e.getMessage();
        if (mensaje == null || mensaje.isEmpty()) {
            mensaje = "Error desconocido en el servidor";
        }
        
        redirectAttributes.addFlashAttribute("mensaje", "❌ Error: " + mensaje);
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        
        return "redirect:/";
    }

    /**
     * Maneja todas las excepciones no capturadas
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e,
                                        RedirectAttributes redirectAttributes) {
        log.error("❌ Error no manejado - Tipo: {}", e.getClass().getName(), e);
        
        redirectAttributes.addFlashAttribute(
            "mensaje", 
            "❌ Error interno del servidor: " + e.getClass().getSimpleName()
        );
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        
        return "redirect:/";
    }

    /**
     * Maneja errores 404 - Página no encontrada
     */
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public String handleNotFound(org.springframework.web.servlet.NoHandlerFoundException e,
                                Model model) {
        log.warn("⚠️ Página no encontrada: {}", e.getRequestURL());
        
        model.addAttribute("mensaje", "La página que buscas no existe");
        model.addAttribute("codigo", "404");
        
        return "error/404";
    }

    /**
     * Maneja errores 403 - Acceso denegado
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDenied(org.springframework.security.access.AccessDeniedException e,
                                     Model model) {
        log.warn("⚠️ Acceso denegado: {}", e.getMessage());
        
        model.addAttribute("mensaje", "No tienes permiso para acceder a este recurso");
        model.addAttribute("codigo", "403");
        
        return "error/403";
    }

    /**
     * Maneja errores de datos nulos
     */
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException e,
                                   RedirectAttributes redirectAttributes) {
        log.error("❌ Error de referencia nula", e);
        
        redirectAttributes.addFlashAttribute(
            "mensaje", 
            "❌ Dato no encontrado o inválido"
        );
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        
        return "redirect:/";
    }
}