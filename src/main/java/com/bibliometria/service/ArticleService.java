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
    private final FrequencyAnalysisService frequencyAnalysisService;
    private final AnalyticsService analyticsService;

    public ArticleService(List<ArticleProvider> providers, 
                          FileExportUtil exportUtil, 
                          FrequencyAnalysisService frequencyAnalysisService,
                          AnalyticsService analyticsService) {
        this.providers = providers;
        this.exportUtil = exportUtil;
        this.frequencyAnalysisService = frequencyAnalysisService;
        this.analyticsService = analyticsService;
    }

    public Map<String, Object> procesarExtraccion(String query) {
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

        List<ScientificArticle> listaUnicos = new ArrayList<>(unicos);

        // 3. Requerimiento 3: Análisis de Frecuencias (Parte C)
        Map<String, Integer> frecuencias = frequencyAnalysisService.analizarFrecuencias(listaUnicos);

        // 4. Requerimiento 3: Descubrimiento de Nuevas Palabras (Parte D)
        Map<String, Integer> nuevasPalabras = frequencyAnalysisService.descubrirNuevasPalabras(listaUnicos);

        // 5. Requerimiento 3: Evaluación de Precisión (Parte E)
        double precision = frequencyAnalysisService.calcularPrecision(nuevasPalabras, listaUnicos);

        // 6. Fase de Visualización (Data Prep)
        Map<String, Integer> datosGeograficos = analyticsService.generarDatosGeograficos(listaUnicos);
        Map<Integer, Map<String, Long>> datosTemporales = analyticsService.generarDatosTemporales(listaUnicos);

        // 7. Exportación de archivos
        exportUtil.guardarResultados(listaUnicos, "articulos_unificados.csv");
        exportUtil.guardarResultados(eliminados, "articulos_eliminados.csv");

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("query", query);
        resumen.put("total_procesados", todos.size());
        resumen.put("unicos_guardados", unicos.size());
        resumen.put("eliminados_duplicados", eliminados.size());
        resumen.put("analisis_frecuencias_base", frecuencias);
        resumen.put("descubrimiento_nuevas_palabras", nuevasPalabras);
        resumen.put("precision_descubrimiento_ia", String.format("%.2f%%", precision));
        
        // Datos para Visualización
        resumen.put("visualizacion_geografica", datosGeograficos);
        resumen.put("visualizacion_temporal", datosTemporales);

        log.info("Extracción completada. Únicos: {} | Precisión IA: {}%", unicos.size(), String.format("%.2f", precision));

        return resumen;
    }
}
