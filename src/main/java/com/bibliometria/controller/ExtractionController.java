package com.bibliometria.controller;

import com.bibliometria.service.*;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bibliometria")
public class ExtractionController {

    private final ArticleService articleService;

    public ExtractionController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/extraer")
    public ResponseEntity<Map<String, Integer>> extraer(@RequestParam String query) {
        try {
            Map<String, Integer> resultado = articleService.procesarExtraccion(query);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
