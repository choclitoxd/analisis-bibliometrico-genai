package com.bibliometria.service.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Servicio de similitud utilizando Modelos de Lenguaje de Hugging Face.
 * Obtiene embeddings de texto y calcula su similitud cosenoidal.
 */
@Service
public class HuggingFaceSimilarityService implements SimilarityAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(HuggingFaceSimilarityService.class);
    
    private final RestTemplate restTemplate;
    
    // URL del modelo en Hugging Face (Sentence Transformers)
    private final String API_URL = "https://api-inference.huggingface.co/models/sentence-transformers/all-MiniLM-L6-v2";
    
    @Value("${huggingface.api.token:none}")
    private String apiToken;

    public HuggingFaceSimilarityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double calculate(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) return 0.0;

        try {
            // 1. Obtener embeddings para ambos textos
            double[] embedding1 = fetchEmbedding(text1);
            double[] embedding2 = fetchEmbedding(text2);

            // 2. Calcular similitud del coseno entre los vectores
            return calculateCosineSimilarity(embedding1, embedding2);
            
        } catch (Exception e) {
            log.error("Error al consultar Hugging Face AI: {}", e.getMessage());
            return 0.0; // Fallback en caso de error de red o cuota
        }
    }

    private double[] fetchEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!"none".equals(apiToken)) {
            headers.setBearerAuth(apiToken);
        }

        Map<String, String> body = new HashMap<>();
        body.put("inputs", text);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        
        // La API de Hugging Face para este modelo devuelve un arreglo de dobles
        ResponseEntity<double[]> response = restTemplate.postForEntity(API_URL, entity, double[].class);
        
        return response.getBody();
    }

    private double calculateCosineSimilarity(double[] v1, double[] v2) {
        if (v1 == null || v2 == null || v1.length != v2.length) return 0.0;

        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += Math.pow(v1[i], 2);
            normB += Math.pow(v2[i], 2);
        }
        
        double result = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        return Math.max(0, Math.min(1, result)); // Normalizar entre 0 y 1
    }

    @Override
    public String getAlgorithmName() {
        return "IA Hugging Face (Embeddings + Coseno)";
    }
}
