package com.bibliometria.service.algorithms;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Implementación de la Similitud de Jaccard.
 * Mide el grado de similitud entre dos conjuntos de palabras (Intersección / Unión).
 */
@Service
public class JaccardService implements SimilarityAlgorithm {

    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null || s1.trim().isEmpty() || s2.trim().isEmpty()) {
            return 0.0;
        }

        // Tokenización: Convertimos a minúsculas y separamos por cualquier carácter no alfanumérico
        Set<String> set1 = new HashSet<>(Arrays.asList(s1.toLowerCase().split("\\W+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(s2.toLowerCase().split("\\W+")));
        
        // Evitar división por cero
        if (set1.isEmpty() || (set1.size() == 1 && set1.contains(""))) return 0.0;

        // Calculamos la intersección
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Calculamos la unión
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    @Override
    public String getAlgorithmName() {
        return "Similitud de Jaccard";
    }
}
