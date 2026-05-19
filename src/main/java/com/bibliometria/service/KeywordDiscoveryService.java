package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para el descubrimiento de nuevas palabras clave mediante análisis estadístico.
 * Implementa la Parte D del Requerimiento 3 (Descubrimiento Inteligente).
 */
@Service
public class KeywordDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(KeywordDiscoveryService.class);

    // Stop words básicas para filtrar ruido en el análisis
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the", "and", "a", "an", "of", "to", "in", "for", "with", "on", "at", 
        "by", "from", "up", "about", "into", "over", "after", "is", "are", 
        "was", "were", "be", "been", "being", "have", "has", "had", "do", 
        "does", "did", "but", "if", "or", "as", "what", "which", "this", 
        "that", "these", "those", "it", "its", "we", "our", "their", "they",
        "can", "will", "should", "could", "may", "might", "must", "using",
        "study", "research", "paper", "based", "results", "analysis", "system"
    ));

    /**
     * Descubre los 15 términos más frecuentes (incluyendo bi-gramas) que no están en la lista original.
     */
    public Map<String, Integer> descubrirNuevasPalabras(List<ScientificArticle> articulos, Set<String> palabrasExcluidas) {
        log.info("Iniciando descubrimiento de nuevas palabras clave (N-gramas).");
        
        Map<String, Integer> frecuenciaGlobal = new HashMap<>();
        Set<String> exclusionesNormalizadas = palabrasExcluidas.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for (ScientificArticle articulo : articulos) {
            String abstractTexto = articulo.getAbstractContent();
            if (abstractTexto == null || abstractTexto.isEmpty()) continue;

            // Limpieza y tokenización
            String cleanText = abstractTexto.toLowerCase().replaceAll("[^a-z\\s]", "");
            String[] tokens = cleanText.split("\\s+");

            // 1. Unigramas (Palabras sueltas)
            for (String token : tokens) {
                if (token.length() > 3 && !STOP_WORDS.contains(token) && !exclusionesNormalizadas.contains(token)) {
                    frecuenciaGlobal.put(token, frecuenciaGlobal.getOrDefault(token, 0) + 1);
                }
            }

            // 2. Bi-gramas (Pares de palabras) - "Inteligencia Inferida"
            for (int i = 0; i < tokens.length - 1; i++) {
                String biGram = tokens[i] + " " + tokens[i+1];
                if (!STOP_WORDS.contains(tokens[i]) && !STOP_WORDS.contains(tokens[i+1]) && !exclusionesNormalizadas.contains(biGram)) {
                    frecuenciaGlobal.put(biGram, frecuenciaGlobal.getOrDefault(biGram, 0) + 1);
                }
            }
        }

        return frecuenciaGlobal.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15)
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    Map.Entry::getValue, 
                    (e1, e2) -> e1, 
                    LinkedHashMap::new
                ));
    }

    /**
     * Evalúa la precisión comparando palabras descubiertas con las keywords del autor.
     * Parte E del Requerimiento 3.
     */
    public double evaluarPrecision(Map<String, Integer> descubiertas, List<ScientificArticle> articulos) {
        if (descubiertas.isEmpty()) return 0.0;

        Set<String> keywordsAutores = articulos.stream()
                .flatMap(a -> a.getKeywords().stream())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        long coincidencias = descubiertas.keySet().stream()
                .filter(word -> keywordsAutores.stream().anyMatch(kw -> kw.contains(word) || word.contains(kw)))
                .count();

        double precision = (double) coincidencias / descubiertas.size();
        log.info("Evaluación de precisión completada: {}%", precision * 100);
        return precision;
    }
}
