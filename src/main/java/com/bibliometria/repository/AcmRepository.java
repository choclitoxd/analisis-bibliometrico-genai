package com.bibliometria.repository;

import com.bibliometria.model.*;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja la obtención de datos desde ACM Digital Library.
 */
@Repository
public class AcmRepository implements ArticleProvider {

    private static final Logger log = LoggerFactory.getLogger(AcmRepository.class);

    @Override
    public List<ScientificArticle> buscarArticulos(String query) {
        log.info("Buscando en ACM Digital Library: {}", query);
        return Arrays.asList(
            new ScientificArticle("ACM_1", "Generative AI in Education", Arrays.asList("C"), "...", Arrays.asList("AI", "Edu"), "ACM")
        );
    }

    @Override
    public String getSourceName() {
        return "ACM";
    }
}
