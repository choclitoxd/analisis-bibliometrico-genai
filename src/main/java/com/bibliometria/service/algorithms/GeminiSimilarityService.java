package com.bibliometria.service.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Servicio de similitud utilizando Google Gemini (Generative Language API).
 * Proporciona una alternativa gratuita y potente para la vectorización semántica.
 */
@Service
public class GeminiSimilarityService implements SimilarityAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(GeminiSimilarityService.class);
    
    private final RestTemplate restTemplate;
    
    // URL base de la API de Gemini para modelos de lenguaje
    private final String API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    
    @Value("${gemini.api.token:none}")
    private String apiToken;

    public GeminiSimilarityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double calculate(String text1, String text2) {
        if (text1 == null || text2 == null || text1.trim().isEmpty() || text2.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // 1. Obtener embeddings de Gemini
            double[] vector1 = fetchGeminiEmbedding(text1);
            double[] vector2 = fetchGeminiEmbedding(text2);

            // 2. Calcular similitud (Coseno)
            return calculateCosineSimilarity(vector1, vector2);
            
        } catch (Exception e) {
            log.error("Error en la conexión con Gemini AI Service: {}", e.getMessage());
            return 0.0;
        }
    }

    private double[] fetchGeminiEmbedding(String text) {
        // El modelo verificado en tu API Key es gemini-embedding-001
        String url = String.format("%s/models/gemini-embedding-001:embedContent?key=%s", API_BASE_URL, apiToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Estructura de petición requerida por Gemini
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", text);
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> body = new HashMap<>();
        body.put("model", "models/gemini-embedding-001");
        body.put("content", content);
        body.put("task_type", "SEMANTIC_SIMILARITY");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> embeddingMap = (Map<String, Object>) response.getBody().get("embedding");
            List<Number> values = (List<Number>) embeddingMap.get("values");
            return values.stream().mapToDouble(Number::doubleValue).toArray();
        }
        
        return new double[0];
    }

    private double calculateCosineSimilarity(double[] v1, double[] v2) {
        if (v1 == null || v2 == null || v1.length == 0 || v1.length != v2.length) return 0.0;

        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += Math.pow(v1[i], 2);
            normB += Math.pow(v2[i], 2);
        }
        
        if (normA == 0 || normB == 0) return 0.0;
        
        double result = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        return Math.max(0, Math.min(1, result));
    }

    @Override
    public String getAlgorithmName() {
        return "IA Google Gemini (Embeddings Semánticos)";
    }
}
