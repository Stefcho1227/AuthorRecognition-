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

    private Map<String, Map<String, Integer>> wordCountsPerAuthor = new HashMap<>();
    private Map<String, Integer> totalWordsPerAuthor = new HashMap<>();
    private Map<String, Integer> docCountPerAuthor = new HashMap<>();

    private Map<String, Map<String, Integer>> wordCountsPerGenre = new HashMap<>();
    private Map<String, Integer> totalWordsPerGenre = new HashMap<>();
    private Map<String, Integer> docCountPerGenre = new HashMap<>();

    private Set<String> vocabulary = new HashSet<>();
    private int totalDocuments = 0;

    public void trainModel(String baseDir, String[] authors) {
        for (String author : authors) {
            String authorDirPath = baseDir + File.separator + author;
            File[] genreDirs = new File(authorDirPath).listFiles(File::isDirectory);

            if (genreDirs != null) {
                for (File genreDir : genreDirs) {
                    String genre = genreDir.getName();
                    wordCountsPerGenre.putIfAbsent(genre, new HashMap<>());
                    totalWordsPerGenre.putIfAbsent(genre, 0);
                    docCountPerGenre.putIfAbsent(genre, 0);

                    File[] textFiles = FileUtil.getTextFiles(genreDir.getAbsolutePath());
                    if (textFiles != null) {
                        for (File file : textFiles) {
                            docCountPerAuthor.put(author, docCountPerAuthor.getOrDefault(author, 0) + 1);
                            docCountPerGenre.put(genre, docCountPerGenre.getOrDefault(genre, 0) + 1);
                            totalDocuments++;

                            String content = FileUtil.readFileContent(file);
                            List<String> tokens = FileUtil.tokenizeText(content);

                            for (String token : tokens) {
                                wordCountsPerAuthor.putIfAbsent(author, new HashMap<>());
                                wordCountsPerAuthor.get(author).put(token, wordCountsPerAuthor.get(author).getOrDefault(token, 0) + 1);
                                totalWordsPerAuthor.put(author, totalWordsPerAuthor.getOrDefault(author, 0) + 1);

                                wordCountsPerGenre.get(genre).put(token, wordCountsPerGenre.get(genre).getOrDefault(token, 0) + 1);
                                totalWordsPerGenre.put(genre, totalWordsPerGenre.getOrDefault(genre, 0) + 1);

                                vocabulary.add(token);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveModelToJson(String outputPath) {
        ModelData modelData = new ModelData(
                wordCountsPerAuthor,
                totalWordsPerAuthor,
                docCountPerAuthor,
                wordCountsPerGenre,
                totalWordsPerGenre,
                docCountPerGenre,
                totalDocuments,
                new ArrayList<>(vocabulary));
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
