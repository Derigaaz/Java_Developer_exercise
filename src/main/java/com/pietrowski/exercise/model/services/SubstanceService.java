package com.pietrowski.exercise.model.services;

import com.pietrowski.exercise.model.dao.SubstanceDAO;
import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Setter
public class SubstanceService {

    @Autowired
    private SubstanceDAO substanceDAO;
    @Autowired
    private SubstanceUpdateEntryService substanceUpdateEntryService;

    public static Substance buildSubstance(Row row) {
        return Substance.builder()
                .indexNo(getStringCellValue(row, 0))
                .intChemId(getStringCellValueWithRemovedLineBreaks(row, 1))
                .ecNo(getStringCellValueWithRemovedLineBreaks(row, 2))
                .casNo(getStringCellValueWithRemovedLineBreaks(row, 3))
                .hazardClasses(getStringCellListOfValues(row, 4))
                .hazardStatementCodes(getStringCellListOfValues(row, 5))
                .build();
    }

    public void processNewSubstance(List<Substance> substances, Substance newSubstanceEntry) {
        if (!substances.contains(newSubstanceEntry)) {
            Substance substanceBeforeUpdate = substances.stream().filter(substance -> substance.getIndexNo().equals(newSubstanceEntry.getIndexNo())).findAny().orElse(null);
            Substance updatedSubstance = substanceDAO.update(newSubstanceEntry);
            if (!updatedSubstance.equals(substanceBeforeUpdate) && substanceBeforeUpdate != null) {
                SubstanceUpdateEntry updateEntry = SubstanceUpdateEntryService.buildSubstanceUpdateEntry(substanceBeforeUpdate, updatedSubstance);
                substanceUpdateEntryService.update(updateEntry);
            }
        }
    }

    public List<Substance> findAll() {
        return substanceDAO.findAll();
    }

    public void deleteAll() {
        substanceDAO.deleteAll();
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

}
