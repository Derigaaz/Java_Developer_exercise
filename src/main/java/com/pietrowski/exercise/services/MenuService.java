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
    private static final String PATH_TO_FILE_REQUEST_MESSAGE = "Please enter path to the file you wish to process.";
    private static final String MAIN_MENU_MESSAGE = """
            Type command to execute:
            'substances': displays all substances currently in database
            'updates': displays history of all updates
            'input': allows to input a new Excel file to process
            'output': allows to output database values to a new Excel file
            'clear': clears database entries - THIS ACTION CANNOT BE REVERTED!
            'quit': exits the application
            """;
    @Autowired
    private WorkbookService workbookService;
    @Autowired
    private SubstanceService substanceService;
    @Autowired
    private SubstanceUpdateEntryService substanceUpdateEntryService;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void runMainMenu() throws IOException {
        while (true) {
            System.out.println(MAIN_MENU_MESSAGE);
            String operation = reader.readLine();
            switch (operation) {
                case "quit" -> exit(0);
                case "input" -> executeInputCommand();
                case "substances" -> executeSubstancesDisplay();
                case "updates" -> executeUpdatesDisplay();
                case "clear" -> executeDatabaseClearing();
                case "output" -> executeOutputCommand();
                default -> System.out.println("Unrecognized operation.");
            }
        }
    }

    private void executeOutputCommand() {
        Workbook workbook = workbookService.createWorkbook(substanceUpdateEntryService.findAll());
        FileService.writeNewExcelFile(workbook);
    }

    private void executeDatabaseClearing() {
        substanceService.deleteAll();
    }

    private void executeUpdatesDisplay() {
        substanceUpdateEntryService.findAll().stream().map(SubstanceUpdateEntry::toString).forEachOrdered(System.out::println);
    }

    private void executeSubstancesDisplay() {
        substanceService.findAll().stream().map(Substance::toString).forEachOrdered(System.out::println);
    }

    private void executeInputCommand() throws IOException {
        System.out.println(PATH_TO_FILE_REQUEST_MESSAGE);
        String filePath = reader.readLine();
        FileInputStream inputStream = FileService.openInputStream(filePath);
        if (inputStream != null) {
            Optional<Workbook> workBook = Optional.ofNullable(FileService.getWorkBookFromStream(inputStream));
            workBook.ifPresent(workbook -> workbookService.processWorkbook(workbook));
            FileService.closeInputStream(inputStream);
        }
    }

}
