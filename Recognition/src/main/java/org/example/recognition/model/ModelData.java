package org.example.recognition.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ModelData {
    public ModelData() {
    }

    @JsonProperty("wordCountsPerAuthor")
    private Map<String, Map<String, Integer>> wordCountsPerAuthor;

    @JsonProperty("totalWordsPerAuthor")
    private Map<String, Integer> totalWordsPerAuthor;

    @JsonProperty("docCountPerAuthor")
    private Map<String, Integer> docCountPerAuthor;

    @JsonProperty("wordCountsPerGenre")
    private Map<String, Map<String, Integer>> wordCountsPerGenre;

    @JsonProperty("totalWordsPerGenre")
    private Map<String, Integer> totalWordsPerGenre;

    @JsonProperty("docCountPerGenre")
    private Map<String, Integer> docCountPerGenre;

    @JsonProperty("totalDocuments")
    private int totalDocuments;

    @JsonProperty("vocabulary")
    private List<String> vocabulary;

    public ModelData(
            @JsonProperty("wordCountsPerAuthor") Map<String, Map<String, Integer>> wordCountsPerAuthor,
            @JsonProperty("totalWordsPerAuthor") Map<String, Integer> totalWordsPerAuthor,
            @JsonProperty("docCountPerAuthor") Map<String, Integer> docCountPerAuthor,
            @JsonProperty("wordCountsPerGenre") Map<String, Map<String, Integer>> wordCountsPerGenre,
            @JsonProperty("totalWordsPerGenre") Map<String, Integer> totalWordsPerGenre,
            @JsonProperty("docCountPerGenre") Map<String, Integer> docCountPerGenre,
            @JsonProperty("totalDocuments") int totalDocuments,
            @JsonProperty("vocabulary") List<String> vocabulary) {
        this.wordCountsPerAuthor = wordCountsPerAuthor;
        this.totalWordsPerAuthor = totalWordsPerAuthor;
        this.docCountPerAuthor = docCountPerAuthor;
        this.wordCountsPerGenre = wordCountsPerGenre;
        this.totalWordsPerGenre = totalWordsPerGenre;
        this.docCountPerGenre = docCountPerGenre;
        this.totalDocuments = totalDocuments;
        this.vocabulary = vocabulary;
    }

    public Map<String, Map<String, Integer>> getWordCountsPerAuthor() {
        return wordCountsPerAuthor;
    }

    public void setWordCountsPerAuthor(Map<String, Map<String, Integer>> wordCountsPerAuthor) {
        this.wordCountsPerAuthor = wordCountsPerAuthor;
    }

    public Map<String, Integer> getTotalWordsPerAuthor() {
        return totalWordsPerAuthor;
    }

    public void setTotalWordsPerAuthor(Map<String, Integer> totalWordsPerAuthor) {
        this.totalWordsPerAuthor = totalWordsPerAuthor;
    }

    public Map<String, Integer> getDocCountPerAuthor() {
        return docCountPerAuthor;
    }

    public void setDocCountPerAuthor(Map<String, Integer> docCountPerAuthor) {
        this.docCountPerAuthor = docCountPerAuthor;
    }

    public Map<String, Map<String, Integer>> getWordCountsPerGenre() {
        return wordCountsPerGenre;
    }

    public void setWordCountsPerGenre(Map<String, Map<String, Integer>> wordCountsPerGenre) {
        this.wordCountsPerGenre = wordCountsPerGenre;
    }

    public Map<String, Integer> getTotalWordsPerGenre() {
        return totalWordsPerGenre;
    }

    public void setTotalWordsPerGenre(Map<String, Integer> totalWordsPerGenre) {
        this.totalWordsPerGenre = totalWordsPerGenre;
    }

    public Map<String, Integer> getDocCountPerGenre() {
        return docCountPerGenre;
    }

    public void setDocCountPerGenre(Map<String, Integer> docCountPerGenre) {
        this.docCountPerGenre = docCountPerGenre;
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(int totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public List<String> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<String> vocabulary) {
        this.vocabulary = vocabulary;
    }
}

