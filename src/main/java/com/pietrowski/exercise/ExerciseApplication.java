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
import java.util.Optional;

import static java.lang.System.exit;

@SpringBootApplication
public class ExerciseApplication implements CommandLineRunner {

    private static final String MAIN_MENU = """
            Type command to execute:
            'substances': displays all substances currently in database
            'updates': displays history of all updates
            'input': allows to input a new Excel file to process
            'clear': clears database entries - THIS ACTION CANNOT BE REVERTED!
            'quit': exits the application
            """;

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
            System.out.println(MAIN_MENU);
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
                        workBook.ifPresent(workbook -> inputProcessingService.createListOfSubstancesFromWorkbook(workbook));
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
