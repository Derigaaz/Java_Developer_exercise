package com.pietrowski.exercise.services;

import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import com.pietrowski.exercise.model.services.SubstanceService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class WorkbookService {
    @Autowired
    SubstanceService substanceService;

    private static void setCellStyleForRow(Row row, CellStyle headerStyle) {
        for (int index = 0; index <= 7; index++) {
            row.getCell(index).setCellStyle(headerStyle);
        }
    }

    public void processWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheet("ATP_18");
        List<Substance> substances = substanceService.findAll();
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int index = firstRow + 6; index <= lastRow; index++) {
            processRowFromInput(sheet, substances, index);
        }
    }

    private void processRowFromInput(Sheet sheet, List<Substance> substances, int index) {
        Optional<Row> row = Optional.ofNullable(sheet.getRow(index));
        Optional<Substance> rowSubstance = row.map(SubstanceService::buildSubstance);
        if (rowSubstance.isPresent()) {
            Substance newSubstanceEntry = rowSubstance.get();
            substanceService.processNewSubstance(substances, newSubstanceEntry);
        }
    }

    public Workbook createWorkbook(List<SubstanceUpdateEntry> updateEntries) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("UpdatedSubstances");
        for (int index = 0; index <= 7; index++) {
            sheet.setColumnWidth(index, 4000);
        }
        createHeader(workbook, sheet);
        CellStyle cellStyle = createCellStyle(workbook);
        IntStream.range(0, updateEntries.size()).forEachOrdered(i -> createDataRow(workbook, sheet, i + 1, updateEntries.get(i), cellStyle));
        return workbook;
    }

    private void createHeader(XSSFWorkbook workbook, Sheet sheet) {
        Row header = sheet.createRow(0);
        createHeaderRowData(workbook, header);
    }

    private void createHeaderRowData(XSSFWorkbook workbook, Row header) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        header.createCell(0).setCellValue("International Chemical Identification");
        header.createCell(1).setCellValue("EC No");
        header.createCell(2).setCellValue("CAS No");
        header.createCell(3).setCellValue("Removed Hazard Classes");
        header.createCell(4).setCellValue("Added Hazard Classes");
        header.createCell(5).setCellValue("Removed Hazard Statement Codes");
        header.createCell(6).setCellValue("Added Hazard StatementCodes");
        header.createCell(7).setCellValue("Update Date");
        setCellStyleForRow(header, headerStyle);
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);

        headerStyle.setFont(font);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.TOP);
        return headerStyle;
    }

    private void createDataRow(XSSFWorkbook workbook, Sheet sheet, int rowNo, SubstanceUpdateEntry substanceUpdateEntry, CellStyle style) {
        Row row = sheet.createRow(rowNo);
        fillRowCellsFromEntry(row, substanceUpdateEntry, style);
        row.setRowStyle(createCellStyle(workbook));
        row.getCell(7).getCellStyle().setDataFormat((short) 22);
    }

    private void fillRowCellsFromEntry(Row row, SubstanceUpdateEntry substanceUpdateEntry, CellStyle style) {
        row.createCell(0).setCellValue(substanceUpdateEntry.getSubstance().getIntChemId());
        row.createCell(1).setCellValue(substanceUpdateEntry.getSubstance().getEcNo());
        row.createCell(2).setCellValue(substanceUpdateEntry.getSubstance().getCasNo());
        row.createCell(3).setCellValue(reduceListToString(substanceUpdateEntry.getRemovedHazardClasses()));
        row.createCell(4).setCellValue(reduceListToString(substanceUpdateEntry.getAddedHazardClasses()));
        row.createCell(5).setCellValue(reduceListToString(substanceUpdateEntry.getRemovedHazardStatementCodes()));
        row.createCell(6).setCellValue(reduceListToString(substanceUpdateEntry.getAddedHazardStatementCodes()));
        row.createCell(7).setCellValue(substanceUpdateEntry.getUpdateTime());
        setCellStyleForRow(row, style);
    }

    private String reduceListToString(List<String> values) {
        return values.stream().reduce("", (partialString, element) -> partialString + "\n\t" + element);
    }

    private CellStyle createCellStyle(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 8);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        return cellStyle;
    }


}
