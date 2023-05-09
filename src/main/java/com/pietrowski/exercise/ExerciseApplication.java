package com.pietrowski.exercise;

import com.pietrowski.exercise.model.Substance;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.System.exit;

@SpringBootApplication
public class ExerciseApplication implements CommandLineRunner {

    @Autowired
    InputProcessingService inputProcessingService;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ExerciseApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        while (true) {
            System.out.println("Type 'quit' to exit the application or 'input' to input a new Excel file to process.");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            String operation;
            operation = reader.readLine();
            switch (operation) {
                case "quit" -> exit(0);
                case "input" -> {
                    System.out.println("Please enter path to the file you wish to process.");
                    String filePath = reader.readLine();
                    FileInputStream inputStream = ExcelProcessingService.openInputStream(filePath);
                    Optional<Workbook> workBook = Optional.ofNullable(ExcelProcessingService.getWorkBookFromStream(inputStream));
                    List<Substance> substances = workBook.map(workbook -> inputProcessingService.createListOfSubstancesFromWorkbook(workbook)).orElseGet(ArrayList::new);
                    substances.stream().map(Substance::toString).forEachOrdered(System.out::println);
                    assert inputStream != null;
                    ExcelProcessingService.closeInputStream(inputStream);
                }
                default -> System.out.println("Unrecognized operation.");
            }
        }
    }
}
