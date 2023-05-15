package com.pietrowski.exercise.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileServiceTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    static void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    void openInputStreamNoFileExists() {
        FileInputStream fileInputStream = FileService.openInputStream("");
        assertNull(fileInputStream);
        assertEquals("Error opening the file.", outContent.toString().trim());
    }

}