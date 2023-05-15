package com.pietrowski.exercise;

import com.pietrowski.exercise.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ExerciseApplication implements CommandLineRunner {
    @Autowired
    private MenuService menuService;

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
