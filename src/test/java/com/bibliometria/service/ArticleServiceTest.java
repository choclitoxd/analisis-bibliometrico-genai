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

    @BeforeEach
    void setUp() {
        exportUtil = Mockito.mock(FileExportUtil.class);
        frequencyService = Mockito.mock(FrequencyAnalysisService.class);
        
        ArticleProvider providerMock = Mockito.mock(ArticleProvider.class);
        providers = Collections.singletonList(providerMock);
        
        // Mocking provider behavior
        ScientificArticle a1 = new ScientificArticle(); a1.setTitle("T1");
        ScientificArticle a2 = new ScientificArticle(); a2.setTitle("T1"); // Duplicado
        when(providerMock.buscarArticulos(anyString())).thenReturn(Arrays.asList(a1, a2));
        
        articleService = new ArticleService(providers, exportUtil, frequencyService);
    }

    @Test
    @DisplayName("Orquestación: Debe procesar, deduplicar y retornar el resumen")
    void testProcesarExtraccion() {
        // Act
        Map<String, Object> result = articleService.procesarExtraccion("query");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get("total_procesados"));
        assertEquals(1, result.get("unicos_guardados"));
        assertEquals(1, result.get("eliminados_duplicados"));
    }
}
