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
        String baseDir = "Recognition/src/main/resources/data";
        String[] authors = {"vazov", "yovkov", "konstantinov"};
//      Този код се изпълнява само веднъж при първото построяване на програмата и
//      след всяко обновяване на ресурсните текстове. Той обучава програмата.

//        trainerService.trainModel(baseDir, authors);
//        trainerService.saveModelToJson("authorship_model.json");
//
//        System.out.println("Training complete. Model saved to authorship_model.json");
        recognitionService.loadModel("../AuthorRecognition-/authorship_model.json");
        String testText = "синила от бича, следи от теглото"; //
        String predictedAuthor = recognitionService.predictAuthor(testText);
        String output = "";
        if(predictedAuthor.equals("vazov")){
            output = "Иван Вазов";
        } else if(predictedAuthor.equals("yovkov")){
            output = "Йордан Йовков";
        } else if(predictedAuthor.equals("konstantinov")){
            output = "Алеко Константинов";
        }

        System.out.println("Прогнозираният автор е: " + output);

    }
}
