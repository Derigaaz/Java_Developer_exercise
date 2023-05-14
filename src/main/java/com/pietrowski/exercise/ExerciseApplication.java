package com.pietrowski.exercise;

import com.pietrowski.exercise.model.Substance;
import com.pietrowski.exercise.model.SubstanceUpdateEntry;
import com.pietrowski.exercise.model.dao.SubstanceDAO;
import com.pietrowski.exercise.model.dao.SubstanceUpdateEntryDAO;
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

    @Autowired
    SubstanceDAO substanceDAO;

    @Autowired
    SubstanceUpdateEntryDAO substanceUpdateEntryDAO;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ExerciseApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        while (true) {
            System.out.println("Type 'substances' to display all substances currently in database or 'updates' to display history of all updates");
            System.out.println("Type 'quit' to exit the application or 'input' to input a new Excel file to process.");
            System.out.println("Type 'clear' to clear database entries - THIS ACTION CANNOT BE REVERTED!");
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
                    if(inputStream != null) {
                        Optional<Workbook> workBook = Optional.ofNullable(ExcelProcessingService.getWorkBookFromStream(inputStream));
                        List<Substance> substances = workBook.map(workbook -> inputProcessingService.createListOfSubstancesFromWorkbook(workbook)).orElseGet(ArrayList::new);
                        substances.stream().map(Substance::toString).forEachOrdered(System.out::println);
                        ExcelProcessingService.closeInputStream(inputStream);
                    }
                }
                case "substances" -> substanceDAO.findAll().stream().map(Substance::toString).forEachOrdered(System.out::println);
                case "updates" -> substanceUpdateEntryDAO.findAll().stream().map(SubstanceUpdateEntry::toString).forEachOrdered(System.out::println);
                case "clear" -> {
                    substanceDAO.deleteAll();
                    substanceUpdateEntryDAO.deleteAll();
                }
                default -> System.out.println("Unrecognized operation.");
            }
        }
    }
}
