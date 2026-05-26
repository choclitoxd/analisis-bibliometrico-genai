package com.bibliometria.integration;

import com.bibliometria.service.ArticleService;
import com.bibliometria.util.PdfExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileOutputStream;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PdfReportIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private PdfExportService pdfExportService;

    @Test
    public void testPdfGenerationWithRealData() {
        String query = "generative artificial intelligence";
        
        // 1. Obtener datos reales
        Map<String, Object> resultado = articleService.procesarExtraccion(query);
        assertNotNull(resultado);
        
        // 2. Generar PDF
        try {
            byte[] pdfBytes = pdfExportService.generarPdfDesdeHtml("reporte_pdf", resultado);
            
            assertNotNull(pdfBytes, "El PDF generado no debería ser nulo");
            assertTrue(pdfBytes.length > 0, "El PDF generado no debería estar vacío");

            // Guardar una copia temporal para verificación manual si es necesario
            try (FileOutputStream fos = new FileOutputStream("build/reporte_test_real.pdf")) {
                fos.write(pdfBytes);
            }

            System.out.println("TEST EXITOSO: Reporte PDF generado correctamente con " + pdfBytes.length + " bytes.");
            System.out.println("Archivo guardado en: build/reporte_test_real.pdf");
            
        } catch (Exception e) {
            fail("Error al generar el PDF: " + e.getMessage());
        }
    }
}
