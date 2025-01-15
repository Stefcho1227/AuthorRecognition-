package org.example.recognition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.recognition.model.ModelData;
import org.example.recognition.util.FileUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class TrainerService {

    // Data structures for training
    private Map<String, Map<String, Integer>> wordCountsPerAuthor = new HashMap<>();
    private Map<String, Integer> totalWordsPerAuthor = new HashMap<>();
    private Map<String, Integer> docCountPerAuthor = new HashMap<>();
    private Set<String> vocabulary = new HashSet<>();
    private int totalDocuments = 0;

    public void trainModel(String baseDir, String[] authors) {
        for (String author : authors) {
            wordCountsPerAuthor.put(author, new HashMap<>());
            totalWordsPerAuthor.put(author, 0);
            docCountPerAuthor.put(author, 0);

            String authorDirPath = baseDir + File.separator + author;
            File[] textFiles = FileUtil.getTextFiles(authorDirPath);

            if (textFiles != null) {
                for (File file : textFiles) {
                    docCountPerAuthor.put(author, docCountPerAuthor.get(author) + 1);
                    totalDocuments++;

                    String content = FileUtil.readFileContent(file);
                    List<String> tokens = FileUtil.tokenizeText(content);

                    for (String token : tokens) {
                        wordCountsPerAuthor.get(author).put(token, wordCountsPerAuthor.get(author).getOrDefault(token, 0) + 1);
                        totalWordsPerAuthor.put(author, totalWordsPerAuthor.get(author) + 1);
                        vocabulary.add(token);
                    }
                }
            }
        }
    }

    public void saveModelToJson(String outputPath) {
        ModelData modelData = new ModelData(wordCountsPerAuthor, totalWordsPerAuthor, docCountPerAuthor, totalDocuments, new ArrayList<>(vocabulary));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String jsonString = objectMapper.writeValueAsString(modelData);
            FileUtil.writeToFile(outputPath, jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
