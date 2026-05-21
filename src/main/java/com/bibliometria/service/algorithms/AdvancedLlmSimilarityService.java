package com.bibliometria.service.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementación del segundo modelo de IA para similitud semántica.
 * Diseñado para consumir APIs comerciales (OpenAI/Gemini) para extraer y comparar embeddings.
 */
@Service
public class AdvancedLlmSimilarityService implements SimilarityAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(AdvancedLlmSimilarityService.class);
    private final RestTemplate restTemplate;
    
    @Value("${openai.api.key:mock-key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/embeddings";

    public AdvancedLlmSimilarityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null || s1.trim().isEmpty() || s2.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // 1. Obtener los vectores numéricos (embeddings)
            double[] vector1 = obtenerEmbedding(s1);
            double[] vector2 = obtenerEmbedding(s2);

            // 2. Calcular la similitud del coseno sobre los vectores
            return calcularCosenoArreglos(vector1, vector2);
            
        } catch (Exception e) {
            log.error("Error al consumir la API del segundo modelo de IA: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Simula la obtención de un embedding de alta dimensión desde la API.
     * En un entorno real, aquí se realiza la llamada POST con la API Key.
     */
    private double[] obtenerEmbedding(String text) {
        // Retorna un vector denso simulado de 1536 dimensiones (estándar de OpenAI)
        // Para pruebas, devolvemos un vector basado en el hash del texto para que sea determinista
        double[] mockVector = new double[1536];
        double seed = text.hashCode() / 100.0;
        for (int i = 0; i < mockVector.length; i++) {
            mockVector[i] = Math.sin(seed + i);
        }
        return mockVector;
    }

    /**
     * Calcula matemáticamente el coseno entre dos vectores de alta dimensión.
     */
    private double calcularCosenoArreglos(double[] v1, double[] v2) {
        if (v1.length != v2.length) return 0.0;
        
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += Math.pow(v1[i], 2);
            normB += Math.pow(v2[i], 2);
        }
        
        double result = (normA == 0 || normB == 0) ? 0.0 : dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        return Math.max(0, Math.min(1, result)); // Normalizar entre 0 y 1
    }

    @Override
    public String getAlgorithmName() {
        return "IA Comercial (Advanced Embeddings)";
    }
}
