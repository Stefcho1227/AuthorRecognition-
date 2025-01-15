package org.example.recognition.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static File[] getTextFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory: " + directoryPath);
            return new File[0];
        }
        return directory.listFiles((dir, name) -> name.endsWith(".txt"));
    }

    public static String readFileContent(File file) {
        if (!file.exists() || !file.isFile()) {
            System.err.println("Invalid file: " + file.getAbsolutePath());
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    public static List<String> tokenizeText(String text) {
        text = text.toLowerCase().replaceAll("[^a-zа-я0-9\\s]+", " ");
        String[] tokens = text.trim().split("\\s+");
        List<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    public static void writeToFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (file.exists()) {
                System.out.println("Warning: Overwriting existing file: " + filePath);
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }
}
