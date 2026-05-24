package com.bibliometria.service;

import com.bibliometria.model.ScientificArticle;
import com.bibliometria.repository.ArticleProvider;
import com.bibliometria.util.FileExportUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ArticleServiceTest {

    private ArticleService articleService;
    private List<ArticleProvider> providers;
    private FileExportUtil exportUtil;
    private FrequencyAnalysisService frequencyService;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        exportUtil = Mockito.mock(FileExportUtil.class);
        frequencyService = Mockito.mock(FrequencyAnalysisService.class);
        analyticsService = Mockito.mock(AnalyticsService.class);
        
        ArticleProvider providerMock = Mockito.mock(ArticleProvider.class);
        providers = Collections.singletonList(providerMock);
        
        // Simulación de comportamiento del proveedor
        ScientificArticle a1 = new ScientificArticle(); a1.setTitle("T1");
        ScientificArticle a2 = new ScientificArticle(); a2.setTitle("T1"); // Duplicado intencional
        when(providerMock.buscarArticulos(anyString())).thenReturn(Arrays.asList(a1, a2));
        
        // Constructor actualizado con AnalyticsService
        articleService = new ArticleService(providers, exportUtil, frequencyService, analyticsService);
    }

    @Test
    @DisplayName("Orquestación: Debe procesar, deduplicar y retornar el resumen completo")
    void testProcesarExtraccion() {
        // Act
        Map<String, Object> result = articleService.procesarExtraccion("query");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get("total_procesados"));
        assertEquals(1, result.get("unicos_guardados"));
        assertEquals(1, result.get("eliminados_duplicados"));
        assertTrue(result.containsKey("visualizacion_geografica"), "Debe incluir datos para el mapa de calor");
        assertTrue(result.containsKey("visualizacion_temporal"), "Debe incluir datos para la línea de tiempo");
    }
}
