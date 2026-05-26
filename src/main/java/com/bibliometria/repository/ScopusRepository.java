package com.bibliometria.repository;

import com.bibliometria.model.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proveedor alternativo de artículos utilizando la API de Scopus (Elsevier).
 */
@Repository
public class ScopusRepository implements ArticleProvider {

    private static final Logger log = LoggerFactory.getLogger(ScopusRepository.class);
    private final RestTemplate restTemplate;

    @Value("${api.elsevier.key}")
    private String apiKey;

    @Value("${api.scopus.url}")
    private String apiUrl;

    public ScopusRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ScientificArticle> buscarArticulos(String query) {
        log.info("Consultando Scopus API para: {}", query);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ELS-APIKey", apiKey);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Usamos view=STANDARD para asegurar compatibilidad
            String url = apiUrl + "?query={query}&count=10&view=STANDARD";
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class, query);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapearRespuesta(response.getBody(), query);
            }
        } catch (Exception e) {
            log.error("Error al consultar Scopus para '{}': {}", query, e.getMessage());
        }

        return Collections.emptyList();
    }

    private List<ScientificArticle> mapearRespuesta(Map body, String query) {
        List<ScientificArticle> articulos = new ArrayList<>();
        try {
            Map searchResults = (Map) body.get("search-results");
            if (searchResults == null) return articulos;

            List<Map> entries = (List<Map>) searchResults.get("entry");
            if (entries == null) return articulos;

            for (Map entry : entries) {
                String id = (String) entry.get("dc:identifier");
                String titulo = (String) entry.get("dc:title");
                String creador = (String) entry.get("dc:creator");
                
                String descripcion = (String) entry.get("dc:description");
                if (descripcion == null) descripcion = (String) entry.get("snippet");
                
                String fecha = (String) entry.get("prism:coverDate");
                
                int anio = 2024;
                if (fecha != null && fecha.length() >= 4) {
                    try {
                        anio = Integer.parseInt(fecha.substring(0, 4));
                    } catch (NumberFormatException e) {
                        anio = 2024;
                    }
                }

                // En Scopus Standard no siempre vienen Keywords, así que usamos la query
                List<String> keywords = new ArrayList<>();
                keywords.add(query);
                keywords.add("Scopus Indexed");

                articulos.add(new ScientificArticle(
                    id != null ? id : UUID.randomUUID().toString(),
                    titulo != null ? titulo : "Sin título (Scopus)",
                    creador != null ? Arrays.asList(creador.split(",")) : Collections.singletonList("Autor Scopus"),
                    descripcion != null ? descripcion : "Resumen no disponible en vista estándar de Scopus.",
                    keywords,
                    getSourceName(),
                    anio
                ));
            }
        } catch (Exception e) {
            log.error("Error mapeando respuesta de Scopus: {}", e.getMessage());
        }
        return articulos;
    }

    @Override
    public String getSourceName() {
        return "Scopus";
    }
}
