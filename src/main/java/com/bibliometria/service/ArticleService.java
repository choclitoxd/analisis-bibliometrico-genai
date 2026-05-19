package com.bibliometria.service;

import com.bibliometria.model.*;
import com.bibliometria.repository.*;
import com.bibliometria.util.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private final List<ArticleProvider> providers;
    private final FileExportUtil exportUtil;

    public ArticleService(List<ArticleProvider> providers, FileExportUtil exportUtil) {
        this.providers = providers;
        this.exportUtil = exportUtil;
    }

    public Map<String, Integer> procesarExtraccion(String query) {
        log.info("Iniciando proceso de extracción para: {}", query);
        
        List<ScientificArticle> todos = new ArrayList<>();
        for (ArticleProvider provider : providers) {
            todos.addAll(provider.buscarArticulos(query));
        }
        
        Set<ScientificArticle> unicos = new LinkedHashSet<>();
        List<ScientificArticle> eliminados = new ArrayList<>();

        for (ScientificArticle a : todos) {
            if (!unicos.add(a)) {
                eliminados.add(a);
            }
        }

        exportUtil.guardarResultados(new ArrayList<>(unicos), "articulos_unificados.csv");
        exportUtil.guardarResultados(eliminados, "articulos_eliminados.csv");

        Map<String, Integer> resumen = new HashMap<>();
        resumen.put("total_procesados", todos.size());
        resumen.put("unicos_guardados", unicos.size());
        resumen.put("eliminados_duplicados", eliminados.size());

        log.info("Extracción completada. Únicos: {} | Eliminados: {}", unicos.size(), eliminados.size());

        return resumen;
    }
}
