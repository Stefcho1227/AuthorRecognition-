package org.example.recognition;

import org.example.recognition.service.RecognitionService;
import org.example.recognition.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecognitionApplication implements CommandLineRunner {

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private RecognitionService recognitionService;

    public static void main(String[] args) {
        SpringApplication.run(RecognitionApplication.class, args);
    }

    @Override
    public void run(String... args) {

//      Този код се изпълнява само веднъж при първото построяване на програмата и
//      след всяко обновяване на ресурсните текстове. Той обучава програмата.
        /*String baseDir = "Recognition/src/main/resources/data";
        String[] authors = {"vazov", "yovkov", "konstantinov"};
        trainerService.trainModel(baseDir, authors);
        trainerService.saveModelToJson("authorship_model.json");
//
        System.out.println("Обучението завърши успешно. Моделът е записан в authorship_model.json");*/
        recognitionService.loadModel("../AuthorRecognition-/authorship_model.json");
        //Стойността на testText може да се промени, за да се тества разпознаването на автора
        String testText = "Най-после уреченият ден дохожда и вие тръгваме";
        recognitionService.predict(testText);

    }
}
