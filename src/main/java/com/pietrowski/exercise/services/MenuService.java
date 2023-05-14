package com.pietrowski.exercise.services;

import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import com.pietrowski.exercise.model.services.SubstanceService;
import com.pietrowski.exercise.model.services.SubstanceUpdateEntryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.lang.System.exit;

@Service
public class MenuService {
    private static final String MAIN_MENU = """
            Type command to execute:
            'substances': displays all substances currently in database
            'updates': displays history of all updates
            'input': allows to input a new Excel file to process
            'clear': clears database entries - THIS ACTION CANNOT BE REVERTED!
            'quit': exits the application
            """;

    @Autowired
    WorkbookService workbookService;

    @Autowired
    SubstanceService substanceService;

    @Autowired
    SubstanceUpdateEntryService substanceUpdateEntryService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void runMainMenu() throws IOException {
        while (true) {
            System.out.println(MAIN_MENU);
            String operation = reader.readLine();
            switch (operation) {
                case "quit" -> exit(0);
                case "input" -> executeInputCommand();
                case "substances" -> executeSubstancesDisplay();
                case "updates" -> executeUpdatesDisplay();
                case "clear" -> executeDatabaseClearing();
                default -> System.out.println("Unrecognized operation.");
            }
        }
    }

    private void executeDatabaseClearing() {
        substanceUpdateEntryService.deleteAll();
        substanceService.deleteAll();
    }

    private void executeUpdatesDisplay() {
        substanceUpdateEntryService.findAll().stream().map(SubstanceUpdateEntry::toString).forEachOrdered(System.out::println);
    }

    private void executeSubstancesDisplay() {
        substanceService.findAll().stream().map(Substance::toString).forEachOrdered(System.out::println);
    }

    private void executeInputCommand() throws IOException {
        System.out.println("Please enter path to the file you wish to process.");
        String filePath = reader.readLine();
        FileInputStream inputStream = FileService.openInputStream(filePath);
        if (inputStream != null) {
            Optional<Workbook> workBook = Optional.ofNullable(FileService.getWorkBookFromStream(inputStream));
            workBook.ifPresent(workbook -> workbookService.processWorkbook(workbook));
            FileService.closeInputStream(inputStream);
        }
    }

}
