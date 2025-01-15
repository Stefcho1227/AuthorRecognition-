package org.example.recognition;

import org.example.recognition.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecognitionApplication implements CommandLineRunner {

    @Autowired
    private TrainerService trainerService;

    public static void main(String[] args) {
        SpringApplication.run(RecognitionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String baseDir = "Recognition/src/main/resources/data";
        String[] authors = {"vazov", "yovkov", "konstantinov"};

        trainerService.trainModel(baseDir, authors);
        trainerService.saveModelToJson("authorship_model.json");

        System.out.println("Training complete. Model saved to authorship_model.json");
    }
}
