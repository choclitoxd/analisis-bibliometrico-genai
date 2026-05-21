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
        log.info("Simulando búsqueda en ACM Digital Library para el dominio: {}", query);

        return Arrays.asList(
            new ScientificArticle("ACM_01", "Impact of Generative Artificial Intelligence in Computer Science Education", 
                Arrays.asList("White, L.", "Brown, T."), 
                "Analysis of how students use prompting and generative models in programming courses.", 
                Arrays.asList("Generative AI", "Education", "Prompting"), "ACM"),
            
            new ScientificArticle("ACM_02", "Ethical Challenges of Generative Artificial Intelligence", 
                Arrays.asList("Garcia, S."), 
                "A study on algorithmic bias and transparency in generative artificial intelligence systems.", 
                Arrays.asList("Ethics", "Transparency", "Generative AI"), "ACM")
        );
    }

    @Override
    public String getSourceName() {
        return "ACM";
    }
}
