package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio encargado del análisis estadístico y cálculo de frecuencias
 * en los abstracts de los artículos científicos unificados.
 */
@Service
public class FrequencyAnalysisService {

    // Lista estricta de palabras asociadas proporcionada por el documento del proyecto
    private static final List<String> PALABRAS_ASOCIADAS_BASE = Arrays.asList(
        "generative models", "prompting", "machine learning", "multimodality", 
        "fine tuning", "training data", "algorithmic bias", "explainability", 
        "transparency", "ethics", "privacy", "personalization", 
        "human ai interaction", "ai literacy", "co creation"
    );

    // Stop-words básicas en inglés para limpiar los abstracts
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the", "and", "of", "to", "in", "a", "is", "that", "for", "it", "as", 
        "on", "with", "by", "this", "are", "an", "be", "we", "can", "from"
    ));

    /**
     * Calcula la frecuencia de aparición de las palabras base en todos los abstracts unificados.
     */
    public Map<String, Integer> analizarFrecuencias(List<ScientificArticle> articles) {
        Map<String, Integer> mapaFrecuencias = new HashMap<>();
        
        // Inicializar el mapa con las palabras en 0
        for (String palabra : PALABRAS_ASOCIADAS_BASE) {
            mapaFrecuencias.put(palabra, 0);
        }

        if (articles == null || articles.isEmpty()) return mapaFrecuencias;

        // Recorrer cada artículo y procesar su abstract
        for (ScientificArticle article : articles) {
            String abstractTexto = article.getAbstractContent();
            if (abstractTexto == null) continue;

            String abstractNormalizado = abstractTexto.toLowerCase();

            for (String palabra : PALABRAS_ASOCIADAS_BASE) {
                int conteo = contarCoincidencias(abstractNormalizado, palabra);
                mapaFrecuencias.put(palabra, mapaFrecuencias.get(palabra) + conteo);
            }
        }

        return mapaFrecuencias;
    }

    /**
     * Analiza los abstracts y descubre las 15 palabras (unigramas y bi-gramas) más frecuentes 
     * que NO están en la lista base original.
     */
    public Map<String, Integer> descubrirNuevasPalabras(List<ScientificArticle> articles) {
        Map<String, Integer> wordFrequencies = new HashMap<>();

        for (ScientificArticle article : articles) {
            if (article.getAbstractContent() == null) continue;
            
            // Tokenizamos el abstract, limpiando puntuación
            String[] words = article.getAbstractContent().toLowerCase().split("\\W+");
            List<String> validWords = new ArrayList<>();

            // 1. Procesar unigramas y recolectar palabras válidas para bi-gramas
            for (String word : words) {
                if (word.length() > 2 
                    && !word.matches(".*\\d.*") 
                    && !STOP_WORDS.contains(word)) {
                    
                    validWords.add(word);
                    
                    // Solo agregar al mapa si no es una palabra base
                    if (!PALABRAS_ASOCIADAS_BASE.contains(word)) {
                        wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }

            // 2. Procesar bi-gramas (N-gramas de tamaño 2)
            for (int i = 0; i < validWords.size() - 1; i++) {
                String bigram = validWords.get(i) + " " + validWords.get(i + 1);
                
                // Solo agregar si no es una palabra base (ej. "generative models" ya está en la base)
                if (!PALABRAS_ASOCIADAS_BASE.contains(bigram)) {
                    wordFrequencies.put(bigram, wordFrequencies.getOrDefault(bigram, 0) + 1);
                }
            }
        }

        // Ordenamos el mapa por frecuencia (de mayor a menor) y limitamos a 15
        return wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15)
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    /**
     * Calcula el porcentaje de precisión de las nuevas palabras generadas.
     * Precisión = (Nuevas palabras que coinciden con las Keywords originales) / (Total de nuevas palabras)
     */
    public double calcularPrecision(Map<String, Integer> nuevasPalabras, List<ScientificArticle> articles) {
        if (nuevasPalabras == null || nuevasPalabras.isEmpty()) return 0.0;

        Set<String> todasLasKeywords = new HashSet<>();
        for (ScientificArticle article : articles) {
            if (article.getKeywords() != null) {
                for (String kw : article.getKeywords()) {
                    // Normalizamos las keywords para la comparación desglosándolas por palabras
                    todasLasKeywords.addAll(Arrays.asList(kw.toLowerCase().split("\\W+")));
                }
            }
        }

        int coincidencias = 0;
        for (String nuevaPalabra : nuevasPalabras.keySet()) {
            if (todasLasKeywords.contains(nuevaPalabra)) {
                coincidencias++;
            }
        }

        // Retornamos la precisión como un porcentaje (0.0 a 100.0)
        return ((double) coincidencias / nuevasPalabras.size()) * 100.0;
    }

    /**
     * Cuenta cuántas veces aparece una subcadena (palabra clave) dentro del texto del abstract.
     */
    private int contarCoincidencias(String texto, String subcadena) {
        int index = 0;
        int conteo = 0;
        while ((index = texto.indexOf(subcadena, index)) != -1) {
            conteo++;
            index += subcadena.length();
        }
        return conteo;
    }
}
