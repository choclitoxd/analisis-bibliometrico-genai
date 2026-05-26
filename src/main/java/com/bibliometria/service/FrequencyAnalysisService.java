package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio encargado del análisis estadístico y cálculo de frecuencias.
 */
@Service
public class FrequencyAnalysisService {

    private static final List<String> PALABRAS_ASOCIADAS_BASE = Arrays.asList(
        "generative models", "prompting", "machine learning", "multimodality", 
        "fine tuning", "training data", "algorithmic bias", "explainability", 
        "transparency", "ethics", "privacy", "personalization", 
        "human ai interaction", "ai literacy", "co creation"
    );

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the", "and", "of", "to", "in", "a", "is", "that", "for", "it", "as", 
        "on", "with", "by", "this", "are", "an", "be", "we", "can", "from"
    ));

    public Map<String, Integer> analizarFrecuencias(List<ScientificArticle> articles) {
        Map<String, Integer> mapaFrecuencias = new HashMap<>();
        for (String palabra : PALABRAS_ASOCIADAS_BASE) {
            mapaFrecuencias.put(palabra, 0);
        }

        if (articles == null || articles.isEmpty()) return mapaFrecuencias;

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

    public Map<String, Integer> descubrirNuevasPalabras(List<ScientificArticle> articles) {
        Map<String, Integer> wordFrequencies = new HashMap<>();

        for (ScientificArticle article : articles) {
            if (article.getAbstractContent() == null) continue;
            
            String[] words = article.getAbstractContent().toLowerCase().split("\\W+");
            List<String> validWords = new ArrayList<>();

            for (String word : words) {
                if (word.length() > 3 && !STOP_WORDS.contains(word)) {
                    validWords.add(word);
                    if (!PALABRAS_ASOCIADAS_BASE.contains(word)) {
                        wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }

            for (int i = 0; i < validWords.size() - 1; i++) {
                String bigram = validWords.get(i) + " " + validWords.get(i + 1);
                if (!PALABRAS_ASOCIADAS_BASE.contains(bigram)) {
                    wordFrequencies.put(bigram, wordFrequencies.getOrDefault(bigram, 0) + 1);
                }
            }
        }

        return wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15)
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    /**
     * Calcula la precisión comparando las palabras descubiertas con las Keywords reales/enriquecidas.
     */
    public double calcularPrecision(Map<String, Integer> nuevasPalabras, List<ScientificArticle> articles) {
        if (nuevasPalabras == null || nuevasPalabras.isEmpty()) return 0.0;

        Set<String> keywordsNormalizadas = new HashSet<>();
        for (ScientificArticle article : articles) {
            if (article.getKeywords() != null) {
                for (String kw : article.getKeywords()) {
                    keywordsNormalizadas.add(kw.toLowerCase().trim());
                    // También agregamos las palabras individuales de la keyword para mayor cobertura
                    keywordsNormalizadas.addAll(Arrays.asList(kw.toLowerCase().split("\\W+")));
                }
            }
        }

        int coincidencias = 0;
        for (String nuevaPalabra : nuevasPalabras.keySet()) {
            String np = nuevaPalabra.toLowerCase().trim();
            // Verificamos si la nueva palabra está contenida en alguna keyword o viceversa
            boolean match = false;
            for (String kw : keywordsNormalizadas) {
                if (kw.length() > 3 && (kw.contains(np) || np.contains(kw))) {
                    match = true;
                    break;
                }
            }
            if (match) coincidencias++;
        }

        return ((double) coincidencias / nuevasPalabras.size()) * 100.0;
    }

    private int contarCoincidencias(String texto, String subcadena) {
        int index = 0, conteo = 0;
        while ((index = texto.indexOf(subcadena, index)) != -1) {
            conteo++;
            index += subcadena.length();
        }
        return conteo;
    }
}
