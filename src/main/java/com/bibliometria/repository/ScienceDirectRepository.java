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
        log.info("Buscando en ScienceDirect: {}", query);
        return Arrays.asList(
            new ScientificArticle("1", "Generative AI", Arrays.asList("A"), 
                "An analysis of generative models in high education.", 
                Arrays.asList("Generative AI", "Education"), "ScienceDirect"),
            new ScientificArticle("2", "Machine Learning", Arrays.asList("B"), 
                "Foundations of machine learning and neural networks.", 
                Arrays.asList("Machine Learning", "Neural Networks"), "ScienceDirect"),
            new ScientificArticle("3", "Generative AI", Arrays.asList("A"), 
                "An analysis of generative models in high education.", 
                Arrays.asList("Generative AI"), "ScienceDirect")
        );
    }

    @Override
    public String getSourceName() {
        return "ScienceDirect";
    }
}
