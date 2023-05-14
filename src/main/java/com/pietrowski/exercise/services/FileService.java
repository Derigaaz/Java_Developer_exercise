package com.pietrowski.exercise.services;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@UtilityClass
public class FileService {

    public static FileInputStream openInputStream(String filePath) {
        File file = new File(filePath);
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Workbook getWorkBookFromStream(FileInputStream inputStream) {
        try {
            return new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeInputStream(FileInputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNewExcelFile(Workbook workbook) {
        try {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "updated_substances.xlsx";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
