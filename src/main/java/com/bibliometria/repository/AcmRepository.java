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
 * Maneja la obtención de datos REALES de ACM a través de la API de Semantic Scholar.
 */
@Repository
public class AcmRepository implements ArticleProvider {

    private static final Logger log = LoggerFactory.getLogger(AcmRepository.class);
    private final RestTemplate restTemplate;

    @Value("${api.semanticscholar.url}")
    private String apiUrl;

    public AcmRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${api.semanticscholar.key:none}")
    private String apiKey;

    @Override
    public List<ScientificArticle> buscarArticulos(String query) {
        log.info("Consultando Semantic Scholar con API Key para: {}", query);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            if (!"none".equals(apiKey)) {
                headers.set("x-api-key", apiKey);
            }
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Solicitar s2FieldsOfStudy para enriquecer las keywords y mejorar la precisión
            String url = apiUrl + "?query={query}&limit=10&fields=title,authors,year,abstract,externalIds,venue,s2FieldsOfStudy";
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class, query);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapearRespuesta(response.getBody(), query);
            }
        } catch (Exception e) {
            log.error("Error al consultar Semantic Scholar para '{}': {}", query, e.getMessage());
        }

        return Collections.emptyList();
    }

    private List<ScientificArticle> mapearRespuesta(Map body, String query) {
        List<ScientificArticle> articulos = new ArrayList<>();
        try {
            List<Map> data = (List<Map>) body.get("data");
            if (data == null) return articulos;

            for (Map item : data) {
                String titulo = (String) item.get("title");
                String resume = (String) item.get("abstract");
                Integer anio = (Integer) item.get("year");
                
                List<String> autoresStr = new ArrayList<>();
                List<Map> authors = (List<Map>) item.get("authors");
                if (authors != null) {
                    for (Map auth : authors) {
                        autoresStr.add((String) auth.get("name"));
                    }
                }

                // Extraer keywords de s2FieldsOfStudy
                List<String> keywords = new ArrayList<>();
                keywords.add(query); // La query siempre es una keyword relevante
                
                List<Map> fos = (List<Map>) item.get("s2FieldsOfStudy");
                if (fos != null) {
                    for (Map f : fos) {
                        keywords.add((String) f.get("category"));
                    }
                }

                if (keywords.size() <= 1) {
                    keywords.add("Computing");
                }

                Map externalIds = (Map) item.get("externalIds");
                String id = (externalIds != null && externalIds.get("DOI") != null) 
                            ? (String) externalIds.get("DOI") 
                            : UUID.randomUUID().toString();

                articulos.add(new ScientificArticle(
                    id,
                    titulo != null ? titulo : "Sin título",
                    autoresStr.isEmpty() ? Collections.singletonList("ACM Author") : autoresStr,
                    resume != null ? resume : "Resumen no disponible para este artículo.",
                    keywords,
                    "ACM",
                    anio != null ? anio : 2024
                ));
            }
        } catch (Exception e) {
            log.error("Error mapeando respuesta de Semantic Scholar: {}", e.getMessage());
        }
        return articulos;
    }

    @Override
    public String getSourceName() {
        return "ACM";
    }
}
