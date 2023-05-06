package com.pietrowski.exercise;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@SpringBootApplication
public class ExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
		FileInputStream inputStream = openInputStream("C:\\Users\\stach\\Desktop\\exercise\\annex_vi_clp_table_atp18_en.xlsx");
		Optional<Workbook> workBook = Optional.ofNullable(getWorkBookFromStream(inputStream));
		workBook.ifPresent(ExerciseApplication::readWorkbook);
		assert inputStream != null;
		closeInputStream(inputStream);
	}

	private static void readWorkbook(Workbook workbook) {
		for (Sheet sheet : workbook) {
			int firstRow = sheet.getFirstRowNum();
			int lastRow = sheet.getLastRowNum();
			for (int index = firstRow + 1; index <= lastRow; index++) {
				Row row = sheet.getRow(index);
				if (row != null) {
					for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
						Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						printCellValue(cell);
					}
				}
			}
		}
	}

	private static void printCellValue(Cell cell) {
		CellType cellType = cell.getCellType().equals(CellType.FORMULA)
				? cell.getCachedFormulaResultType() : cell.getCellType();
		if (cellType.equals(CellType.STRING)) {
			System.out.print(cell.getStringCellValue() + " | ");
		}
		if (cellType.equals(CellType.NUMERIC)) {
			if (DateUtil.isCellDateFormatted(cell)) {
				System.out.print(cell.getDateCellValue() + " | ");
			} else {
				System.out.print(cell.getNumericCellValue() + " | ");
			}
		}
		if (cellType.equals(CellType.BOOLEAN)) {
			System.out.print(cell.getBooleanCellValue() + " | ");
		}
	}

	private static FileInputStream openInputStream(String filePath) {
		File file = new File(filePath);
		try {
			 return new FileInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Workbook getWorkBookFromStream(FileInputStream inputStream) {
		try {
			return new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void closeInputStream(FileInputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
