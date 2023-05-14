package com.pietrowski.exercise;

import com.pietrowski.exercise.model.Substance;
import com.pietrowski.exercise.model.SubstanceUpdateEntry;
import com.pietrowski.exercise.model.dao.SubstanceDAO;
import com.pietrowski.exercise.model.dao.SubstanceUpdateEntryDAO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Optional;

@Service
public class InputProcessingService {

    @Autowired
    SubstanceDAO substanceDAO;

    @Autowired
    SubstanceUpdateEntryDAO substanceUpdateEntryDAO;

    public List<Substance> createListOfSubstancesFromWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheet("ATP_18");
        List<Substance> substances = substanceDAO.findAll();
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int index = firstRow + 6; index <= lastRow; index++) {
            Optional<Row> row = Optional.ofNullable(sheet.getRow(index));
            Optional<Substance> rowSubstance = row.map(InputProcessingService::readSubstanceValues);
            if(rowSubstance.isPresent()) {
                Substance newSubstanceEntry = rowSubstance.get();
                if(!substances.contains(newSubstanceEntry)) {
                    Substance substanceBeforeUpdate = substances.stream().filter(substance -> substance.getIndexNo().equals(newSubstanceEntry.getIndexNo())).findAny().orElse(null);
                    Substance updatedSubstance = substanceDAO.update(newSubstanceEntry);
                    if (!(updatedSubstance.equals(substanceBeforeUpdate) || substanceBeforeUpdate == null)) {
                        SubstanceUpdateEntry updateEntry = SubstanceUpdateEntry.builder()
                                .indexNo(updatedSubstance.getIndexNo())
                                .updateTime(LocalDateTime.now())
                                .removedHazardClasses(findDifferencesBetweenLists(substanceBeforeUpdate.getHazardClasses(), updatedSubstance.getHazardClasses()))
                                .addedHazardClasses(findDifferencesBetweenLists(updatedSubstance.getHazardClasses(), substanceBeforeUpdate.getHazardClasses()))
                                .removedHazardStatementCodes(findDifferencesBetweenLists(substanceBeforeUpdate.getHazardStatementCodes(), updatedSubstance.getHazardStatementCodes()))
                                .addedHazardStatementCodes(findDifferencesBetweenLists(updatedSubstance.getHazardStatementCodes(), substanceBeforeUpdate.getHazardStatementCodes()))
                                .build();
                        substanceUpdateEntryDAO.create(updateEntry);
                    }
                }

            }
        }
        return substances;
    }

    public static Substance readSubstanceValues(Row row) {
        return Substance.builder()
                .indexNo(getStringCellValue(row, 0))
                .intChemId(getStringCellValueWithRemovedLineBreaks(row, 1))
                .ecNo(getStringCellValueWithRemovedLineBreaks(row, 2))
                .casNo(getStringCellValueWithRemovedLineBreaks(row, 3))
                .hazardClasses(getStringCellListOfValues(row, 4))
                .hazardStatementCodes(getStringCellListOfValues(row, 5))
                .build();
    }

    private static String getStringCellValue(Row row, int cellNo) {
        return row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
    }

    private static String getStringCellValueWithRemovedLineBreaks(Row row, int cellNo) {
        return row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim().replaceAll("[\n\r]", ", ");
    }

    private static List<String> getStringCellListOfValues(Row row, int cellNo) {
        return Arrays.stream(row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().split("\n")).toList();
    }

    private static List<String> findDifferencesBetweenLists(List<String> listOne, List<String> listTwo) {
        List<String> differences = listOne != null ? new ArrayList<>(listOne): new ArrayList<>();
        if (listTwo == null) {
            return differences;
        }
        differences.removeAll(listTwo);
        return differences;
    }
}
