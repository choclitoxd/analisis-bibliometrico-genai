package com.bibliometria.controller;

import com.bibliometria.service.*;
import com.bibliometria.util.*;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador principal para la extracción de datos y generación de reportes.
 */
@RestController
public class ExtractionController {

    private static final Logger log = LoggerFactory.getLogger(ExtractionController.class);
    private final ArticleService articleService;
    private final PdfExportService pdfExportService;

    public ExtractionController(ArticleService articleService, PdfExportService pdfExportService) {
        this.articleService = articleService;
        this.pdfExportService = pdfExportService;
    }

    /**
     * Redirección automática a la página del Dashboard al entrar a la raíz.
     */
    @GetMapping("/")
    public org.springframework.web.servlet.ModelAndView home() {
        return new org.springframework.web.servlet.ModelAndView("redirect:/api/bibliometria/dashboard");
    }

    /**
     * Endpoint para ejecutar la extracción y obtener resumen en JSON.
     */
    @GetMapping("/api/bibliometria/extraer")
    public ResponseEntity<Map<String, Object>> extraer(@RequestParam String query) {
        try {
            Map<String, Object> resultado = articleService.procesarExtraccion(query);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Error en el proceso de extracción: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint para visualizar el Dashboard interactivo en el navegador.
     */
    @GetMapping("/api/bibliometria/dashboard")
    public org.springframework.web.servlet.ModelAndView mostrarDashboard(
            @RequestParam(defaultValue = "generative artificial intelligence") String query) {
        Map<String, Object> resultado = articleService.procesarExtraccion(query);
        org.springframework.web.servlet.ModelAndView modelAndView = new org.springframework.web.servlet.ModelAndView("dashboard");
        modelAndView.addObject("resumen", resultado);
        return modelAndView;
    }

    /**
     * Endpoint para exportar y descargar el reporte analítico en PDF.
     */
    @GetMapping("/api/bibliometria/reporte/pdf")
    public ResponseEntity<byte[]> descargarReportePdf(
            @RequestParam(defaultValue = "generative artificial intelligence") String query) {
        try {
            Map<String, Object> resultado = articleService.procesarExtraccion(query);
            byte[] pdfBytes = pdfExportService.generarPdfDesdeHtml("reporte_pdf", resultado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_bibliometrico.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al generar el reporte PDF: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
