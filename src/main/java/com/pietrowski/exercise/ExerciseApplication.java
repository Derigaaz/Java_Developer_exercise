package com.pietrowski.exercise;

import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import com.pietrowski.exercise.model.services.SubstanceService;
import com.pietrowski.exercise.model.services.SubstanceUpdateEntryService;
import com.pietrowski.exercise.services.FileService;
import com.pietrowski.exercise.services.MenuService;
import com.pietrowski.exercise.services.WorkbookService;
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
    MenuService menuService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ExerciseApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        menuService.runMainMenu();
    }
}
