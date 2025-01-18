package org.example.recognition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recognition.model.ModelData;
import org.example.recognition.util.FileUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String predict(String text) {
        String predictedAuthor = predictAuthor(text);
        String predictedGenre = predictGenre(text);
        String outputAuthor = switch (predictedAuthor) {
            case "vazov" -> "Иван Вазов";
            case "yovkov" -> "Йордан Йовков";
            case "konstantinov" -> "Алеко Константинов";
            case "Unknown Author" -> "Неизвестен автор";
            default -> "";
        };
        String outputGenre = switch (predictedGenre) {
            case "feiletoni" -> "Фейлетон";
            case "patepisi" -> "Пътепис";
            case "razkazi" -> "Разказ";
            case "poemi" -> "Поема";
            case "poezii" -> "Поезия";
            case "povesti" -> "Повест";
            case "romani" -> "Роман";

            default -> "";
        };

        System.out.printf("Predicted Author: %s%n", outputAuthor);
        System.out.printf("Predicted Genre: %s%n", outputGenre);

        return String.format("Author: %s, Genre: %s", predictedAuthor, predictedGenre);
    }

    private String predictAuthor(String text) {
        List<String> tokens = FileUtil.tokenizeText(text);

        Map<String, Double> logScores = new HashMap<>();
        for (String author : modelData.getWordCountsPerAuthor().keySet()) {
            double score = calculateLogLikelihood(author, tokens, modelData.getWordCountsPerAuthor(), modelData.getTotalWordsPerAuthor());
            logScores.put(author, score);
        }

        Map.Entry<String, Double> bestAuthorEntry = logScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(Map.entry("Unknown Author", Double.NEGATIVE_INFINITY));

        double maxLogScore = logScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(Double.NEGATIVE_INFINITY);

        double totalLogScoreSum = logScores.values().stream()
                .mapToDouble(score -> Math.exp(score - maxLogScore)) // Shift scores
                .sum();

        Map<String, Double> accuracyScores = new HashMap<>();
        if (totalLogScoreSum > 0) {
            for (Map.Entry<String, Double> entry : logScores.entrySet()) {
                double normalizedProbability = Math.exp(entry.getValue() - maxLogScore) / totalLogScoreSum;
                accuracyScores.put(entry.getKey(), normalizedProbability * 100); // Convert to percentage
            }
        } else {
            logScores.keySet().forEach(author -> accuracyScores.put(author, 0.0));
        }

        Map.Entry<String, Double> maxEntry = accuracyScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (maxEntry == null || maxEntry.getValue() < 80) {
            System.out.println("No author predicted with sufficient accuracy.");
            return "Unknown Author";
        }

        System.out.println(String.format("Accuracy: %.2f%%", maxEntry.getValue()));
        return bestAuthorEntry.getKey();
    }
    private String predictGenre(String text) {
        List<String> tokens = FileUtil.tokenizeText(text);

        Map<String, Double> logScores = new HashMap<>();
        for (String genre : modelData.getWordCountsPerGenre().keySet()) {
            double score = calculateLogLikelihood(genre, tokens, modelData.getWordCountsPerGenre(), modelData.getTotalWordsPerGenre());
            logScores.put(genre, score);
        }

        return logScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown Genre");
    }

    private double calculateLogLikelihood(String label, List<String> tokens, Map<String, Map<String, Integer>> wordCounts, Map<String, Integer> totalWords) {
        int totalWordsForLabel = totalWords.getOrDefault(label, 0);
        double vocabularySize = modelData.getVocabulary().size();

        double logLikelihood = 0.0;
        for (String token : tokens) {
            double wordProbability = (wordCounts.getOrDefault(label, Collections.emptyMap()).getOrDefault(token, 0) + 1)
                    / (double) (totalWordsForLabel + vocabularySize);

            logLikelihood += Math.log(wordProbability);
        }
        return logLikelihood;
    }
}


