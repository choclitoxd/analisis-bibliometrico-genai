package com.bibliometria.repository;

import com.bibliometria.model.ScientificArticle;
import java.util.List;

/**
 * Puerto/Interfaz para proveedores de artículos.
 * Permite aplicar el patrón Strategy para desacoplar las fuentes.
 */
public interface ArticleProvider {
    List<ScientificArticle> buscarArticulos(String query);
    String getSourceName();
}
