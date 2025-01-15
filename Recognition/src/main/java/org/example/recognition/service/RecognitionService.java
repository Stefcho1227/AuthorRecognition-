package org.example.recognition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recognition.model.ModelData;
import org.example.recognition.util.FileUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class RecognitionService {

    private ModelData modelData;

    public void loadModel(String modelPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            modelData = objectMapper.readValue(new File(modelPath), ModelData.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the model.");
        }
    }

    public String predictAuthor(String text) {
        if (modelData == null) {
            throw new IllegalStateException("Model is not loaded. Call loadModel() first.");
        }

        Map<String, Double> authorScores = new HashMap<>();
        List<String> tokens = FileUtil.tokenizeText(text);

        for (String author : modelData.getWordCountsPerAuthor().keySet()) {
            double score = calculateScoreForAuthor(author, tokens);
            authorScores.put(author, score);
        }

        return authorScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown Author");
    }

    private double calculateScoreForAuthor(String author, List<String> tokens) {
        Map<String, Integer> wordCounts = modelData.getWordCountsPerAuthor().get(author);
        int totalWords = modelData.getTotalWordsPerAuthor().getOrDefault(author, 0);
        double vocabularySize = modelData.getVocabulary().size();

        double score = 0.0;

        for (String token : tokens) {
            double wordProbability = (wordCounts.getOrDefault(token, 0) + 1) / (double) (totalWords + vocabularySize);
            score += Math.log(wordProbability);
        }

        return score;
    }
}
