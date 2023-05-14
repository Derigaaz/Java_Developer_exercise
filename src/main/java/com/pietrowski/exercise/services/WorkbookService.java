package com.pietrowski.exercise.services;

import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.services.SubstanceService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkbookService {
    @Autowired
    SubstanceService substanceService;

    public void processWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheet("ATP_18");
        List<Substance> substances = substanceService.findAll();
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int index = firstRow + 6; index <= lastRow; index++) {
            processRow(sheet, substances, index);
        }
    }

    private void processRow(Sheet sheet, List<Substance> substances, int index) {
        Optional<Row> row = Optional.ofNullable(sheet.getRow(index));
        Optional<Substance> rowSubstance = row.map(SubstanceService::buildSubstance);
        if (rowSubstance.isPresent()) {
            Substance newSubstanceEntry = rowSubstance.get();
            substanceService.processNewSubstance(substances, newSubstanceEntry);
        }
    }

}
