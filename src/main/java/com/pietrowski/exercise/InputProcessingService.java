package com.pietrowski.exercise;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InputProcessingService {

    public static List<Substance> createListOfSubstancesFromWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheet("ATP_18");
        List<Substance> substances = new ArrayList<>();
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int index = firstRow + 6; index <= lastRow; index++) {
            Optional<Row> row = Optional.ofNullable(sheet.getRow(index));
            Optional<Substance> rowSubstance = row.map(InputProcessingService::readSubstanceValues);
            rowSubstance.ifPresent(substances::add);
        }
        return substances;
    }

    public static Substance readSubstanceValues(Row row) {
        return Substance.builder()
                .indexNo(getStringCellValue(row, 0))
                .intChemId(getStringCellValue(row, 1))
                .ecNo(getStringCellValue(row, 2))
                .casNo(getStringCellValue(row, 3))
                .hazardClasses(getStringCellListOfValues(row, 4))
                .hazardStatementCodes(getStringCellListOfValues(row, 5))
                .build();
    }

    private static String getStringCellValue(Row row, int cellNo) {
        return row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
    }

    private static List<String> getStringCellListOfValues(Row row, int cellNo) {
        return Arrays.stream(row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().split("\n")).toList();
    }
}
