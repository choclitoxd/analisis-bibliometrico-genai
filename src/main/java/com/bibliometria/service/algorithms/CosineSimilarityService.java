package com.bibliometria.service.algorithms;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Implementación de la Similitud del Coseno basada en Frecuencia de Términos (TF).
 * Representa los textos como vectores y calcula el coseno del ángulo entre ellos.
 */
@Service
public class CosineSimilarityService implements SimilarityAlgorithm {

    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null || s1.trim().isEmpty() || s2.trim().isEmpty()) {
            return 0.0;
        }

        Map<String, Integer> vector1 = getTermFrequency(s1);
        Map<String, Integer> vector2 = getTermFrequency(s2);

        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(vector1.keySet());
        allTerms.addAll(vector2.keySet());

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String term : allTerms) {
            int v1 = vector1.getOrDefault(term, 0);
            int v2 = vector2.getOrDefault(term, 0);
            
            dotProduct += (double) v1 * v2;
            magnitude1 += Math.pow(v1, 2);
            magnitude2 += Math.pow(v2, 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0.0 || magnitude2 == 0.0) return 0.0;
        
        return dotProduct / (magnitude1 * magnitude2);
    }

    private Map<String, Integer> getTermFrequency(String text) {
        Map<String, Integer> tf = new HashMap<>();
        // Tokenización básica
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            if (!word.isEmpty()) {
                tf.put(word, tf.getOrDefault(word, 0) + 1);
            }
        }
        return tf;
    }

    @Override
    public String getAlgorithmName() {
        return "Similitud del Coseno (Vectorización Estadística)";
    }
}
