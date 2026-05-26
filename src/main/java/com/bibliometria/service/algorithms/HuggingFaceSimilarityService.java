package com.bibliometria.service.algorithms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * Servicio de similitud utilizando Modelos de Lenguaje de Hugging Face.
 * Implementación de bajo nivel (Java HttpClient) para bypass de errores de MimeType de Spring 2026.
 */
@Service
public class HuggingFaceSimilarityService implements SimilarityAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(HuggingFaceSimilarityService.class);
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    // URL del Router de Hugging Face (Estable en 2026)
    private final String API_URL = "https://router.huggingface.co/hf-inference/models/sentence-transformers/all-MiniLM-L6-v2";
    
    @Value("${huggingface.api.token:none}")
    private String apiToken;

    public HuggingFaceSimilarityService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public double calculate(String text1, String text2) {
        if (text1 == null || text2 == null || text1.trim().isEmpty() || text2.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Estructura de la petición
            Map<String, Object> inputs = new HashMap<>();
            inputs.put("source_sentence", text1);
            inputs.put("sentences", Collections.singletonList(text2));
            
            String jsonBody = objectMapper.writeValueAsString(Collections.singletonMap("inputs", inputs));

            // Construcción manual de la petición HTTP para evitar validaciones de Spring
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken)
                    .header("X-Wait-For-Model", "true")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Enviamos y recibimos como String
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parseo manual del array [0.85, ...]
                double[] results = objectMapper.readValue(response.body(), double[].class);
                if (results != null && results.length > 0) {
                    return Math.max(0, Math.min(1, results[0]));
                }
            } else {
                log.warn("Hugging Face respondió con código {}: {}", response.statusCode(), response.body());
            }
            
        } catch (Exception e) {
            log.error("Error crítico en Hugging Face (HttpClient 2026): {}", e.getMessage());
        }
        
        return 0.0;
    }

    @Override
    public String getAlgorithmName() {
        return "IA Hugging Face (Sentence-Similarity 2026)";
    }
}
