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
