package com.pietrowski.exercise;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.util.Optional;

@SpringBootApplication
public class ExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExerciseApplication.class, args);
        FileInputStream inputStream = ExcelProcessingService.openInputStream("C:\\Users\\stach\\Desktop\\exercise\\annex_vi_clp_table_atp18_en.xlsx");
        Optional<Workbook> workBook = Optional.ofNullable(ExcelProcessingService.getWorkBookFromStream(inputStream));
        workBook.ifPresent(ExcelProcessingService::readWorkbook);
        assert inputStream != null;
        ExcelProcessingService.closeInputStream(inputStream);
    }


}
