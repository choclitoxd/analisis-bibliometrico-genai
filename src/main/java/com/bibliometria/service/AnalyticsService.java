package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la transformación de datos para visualizaciones analíticas.
 */
@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    // Lista simplificada de países para demostración de extracción geográfica
    private static final List<String> PAISES = Arrays.asList(
        "USA", "China", "UK", "Spain", "Germany", "France", "Japan", "Colombia", "Brazil"
    );

    /**
     * Genera datos para el Mapa de Calor Geográfico.
     * Extrae menciones de países en los abstracts/autores.
     */
    public Map<String, Integer> generarDatosGeograficos(List<ScientificArticle> articulos) {
        log.info("Generando datos geográficos para {} artículos.", articulos.size());
        Map<String, Integer> conteoPaises = new HashMap<>();

        for (ScientificArticle art : articulos) {
            String textoParaAnalisis = (art.getAbstractContent() + " " + String.join(" ", art.getAuthors())).toUpperCase();
            
            for (String pais : PAISES) {
                if (textoParaAnalisis.contains(pais.toUpperCase())) {
                    conteoPaises.put(pais, conteoPaises.getOrDefault(pais, 0) + 1);
                }
            }
        }
        return conteoPaises;
    }

    /**
     * Genera datos para la Línea Temporal.
     * Agrupa por Año -> (Revista -> Cantidad).
     */
    public Map<Integer, Map<String, Long>> generarDatosTemporales(List<ScientificArticle> articulos) {
        log.info("Generando datos temporales.");
        
        return articulos.stream()
            .collect(Collectors.groupingBy(
                ScientificArticle::getPublicationYear,
                Collectors.groupingBy(
                    ScientificArticle::getSource,
                    Collectors.counting()
                )
            ));
    }
}
