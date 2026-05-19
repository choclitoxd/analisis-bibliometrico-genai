package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Servicio para el análisis de frecuencias de términos específicos.
 * Cumple con el Requerimiento 3: Conceptos de Generative AI en Educación.
 */
@Service
public class FrequencyAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(FrequencyAnalysisService.class);

    // Lista constante de conceptos requeridos
    private static final List<String> CONCEPTOS_IA = Arrays.asList(
        "Generative models", "Prompting", "Machine learning", "Multimodality", 
        "Fine-tuning", "Training data", "Algorithmic bias", "Explainability", 
        "Transparency", "Ethics", "Privacy", "Personalization", 
        "Human-AI interaction", "AI literacy", "Co-creation"
    );

    /**
     * Analiza la frecuencia de los conceptos de IA en una lista de artículos.
     * @param articulos Lista de artículos unificados.
     * @return Mapa con el concepto y su frecuencia total de aparición.
     */
    public Map<String, Integer> analizarFrecuencias(List<ScientificArticle> articulos) {
        log.info("Iniciando análisis de frecuencias sobre {} artículos.", articulos.size());
        
        Map<String, Integer> frecuencias = new LinkedHashMap<>();
        // Inicializar mapa con ceros
        for (String concepto : CONCEPTOS_IA) {
            frecuencias.put(concepto, 0);
        }

        for (ScientificArticle articulo : articulos) {
            String abstractTexto = articulo.getAbstractContent();
            if (abstractTexto == null || abstractTexto.isEmpty()) continue;

            String abstractLower = abstractTexto.toLowerCase();

            for (String concepto : CONCEPTOS_IA) {
                int conteo = contarOcurrencias(abstractLower, concepto.toLowerCase());
                if (conteo > 0) {
                    frecuencias.put(concepto, frecuencias.get(concepto) + conteo);
                }
            }
        }

        log.info("Análisis de frecuencias completado.");
        return frecuencias;
    }

    /**
     * Cuenta cuántas veces aparece una subcadena en un texto.
     */
    private int contarOcurrencias(String texto, String subcadena) {
        int count = 0;
        int index = 0;
        while ((index = texto.indexOf(subcadena, index)) != -1) {
            count++;
            index += subcadena.length();
        }
        return count;
    }
}
