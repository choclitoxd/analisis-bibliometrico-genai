package com.bibliometria.controller;

import com.bibliometria.service.*;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/bibliometria")
public class ExtractionController {

    private static final Logger log = LoggerFactory.getLogger(ExtractionController.class);
    private final ArticleService articleService;

    public ExtractionController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/extraer")
    public ResponseEntity<Map<String, Object>> extraer(@RequestParam String query) {
        try {
            Map<String, Object> resultado = articleService.procesarExtraccion(query);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Error en el controlador: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
