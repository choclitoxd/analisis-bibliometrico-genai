package com.bibliometria.util;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Servicio encargado de convertir plantillas HTML en documentos PDF.
 */
@Service
public class PdfExportService {

    private final TemplateEngine templateEngine;

    public PdfExportService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Genera un archivo PDF a partir de una plantilla y sus datos.
     */
    public byte[] generarPdfDesdeHtml(String templateName, Map<String, Object> data) {
        try {
            // 1. Procesar la plantilla con Thymeleaf para obtener el HTML final
            Context context = new Context();
            context.setVariables(data);
            String htmlContent = templateEngine.process(templateName, context);

            // 2. Usar Flying Saucer para renderizar el HTML a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            
            // Flying Saucer requiere que el HTML sea XHTML válido
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage(), e);
        }
    }
}
