package com.bibliometria.model;

import java.util.*;

/**
 * Representa un artículo científico.
 */
public class ScientificArticle {
    private String id;
    private String title;
    private List<String> authors;
    private String abstractContent;
    private List<String> keywords;
    private String source;

    public ScientificArticle() {}

    public ScientificArticle(String id, String title, List<String> authors, String abstractContent, List<String> keywords, String source) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.abstractContent = abstractContent;
        this.keywords = keywords;
        this.source = source;
    }

    // Getters y Setters simplificados
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    public String getAbstractContent() { return abstractContent; }
    public void setAbstractContent(String abstractContent) { this.abstractContent = abstractContent; }
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScientificArticle that = (ScientificArticle) o;
        return getNormalizedTitle().equals(that.getNormalizedTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNormalizedTitle());
    }

    private String getNormalizedTitle() {
        return title != null ? title.toLowerCase().replaceAll("[^a-z0-9]", "") : "";
    }
}
