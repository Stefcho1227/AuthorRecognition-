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

        List<String> tokens = FileUtil.tokenizeText(text);

        Map<String, Double> logScores = new HashMap<>();
        for (String author : modelData.getWordCountsPerAuthor().keySet()) {
            double score = calculateLogLikelihood(author, tokens);
            logScores.put(author, score);
        }

        double sumOfExponentials = 0.0;
        Map<String, Double> expScores = new HashMap<>();

        for (Map.Entry<String, Double> entry : logScores.entrySet()) {
            double expVal = Math.exp(entry.getValue());
            expScores.put(entry.getKey(), expVal);
            sumOfExponentials += expVal;
        }

        Map<String, Double> authorProbabilities = new HashMap<>();
        for (Map.Entry<String, Double> entry : expScores.entrySet()) {
            double probability = entry.getValue() / sumOfExponentials;
            authorProbabilities.put(entry.getKey(), probability);
        }

        String predictedAuthor = authorProbabilities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown Author");

        System.out.println("Probabilities:");
        authorProbabilities.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(e -> {
                    String output = "";
                    if(e.getKey().equals("vazov")){
                        output = "Иван Вазов";
                    } else if(e.getKey().equals("yovkov")){
                        output = "Йордан Йовков";
                    } else if(e.getKey().equals("konstantinov")){
                        output = "Алеко Константинов";
                    }
                    System.out.printf("   %s -> %.4f\n", output, e.getValue());
                });

        return predictedAuthor;
    }


    private double calculateLogLikelihood(String author, List<String> tokens) {
        Map<String, Integer> wordCounts = modelData.getWordCountsPerAuthor().get(author);
        int totalWords = modelData.getTotalWordsPerAuthor().getOrDefault(author, 0);
        double vocabularySize = modelData.getVocabulary().size();

        double logLikelihood = 0.0;
        for (String token : tokens) {
            double wordProbability =
                    (wordCounts.getOrDefault(token, 0) + 1) / (double) (totalWords + vocabularySize);

            logLikelihood += Math.log(wordProbability);
        }
        return logLikelihood;
    }
}
