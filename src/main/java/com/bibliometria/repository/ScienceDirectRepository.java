package com.bibliometria.repository;

import com.bibliometria.model.*;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja la obtención de datos desde ScienceDirect.
 */
@Repository
public class ScienceDirectRepository implements ArticleProvider {

    private static final Logger log = LoggerFactory.getLogger(ScienceDirectRepository.class);

    @Override
    public List<ScientificArticle> buscarArticulos(String query) {
        log.info("Simulando búsqueda en ScienceDirect para el dominio: {}", query);

        return Arrays.asList(
            new ScientificArticle("SD_01", "The Rise of Generative Artificial Intelligence in Research", 
                Arrays.asList("Smith, J.", "Doe, R."), 
                "This paper explores how generative artificial intelligence is transforming scientific writing in USA.", 
                Arrays.asList("Generative AI", "Scientific Research", "Ethics"), "ScienceDirect", 2023),

            new ScientificArticle("SD_02", "Large Language Models: A New Era of Generative AI", 
                Arrays.asList("Johnson, M."), 
                "An overview of the development of LLMs in China landscape.", 
                Arrays.asList("LLM", "Generative AI", "NLP"), "ScienceDirect", 2024),

            new ScientificArticle("SD_03", "The Rise of Generative Artificial Intelligence in Research", 
                Arrays.asList("Smith, J."), 
                "This paper explores how generative artificial intelligence is transforming scientific writing in USA.", 
                Arrays.asList("Generative AI"), "ScienceDirect", 2023)
        );

    }
    @Override
    public String getSourceName() {
        return "ScienceDirect";
    }
}
