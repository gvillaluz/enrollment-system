package com.enrollmentsystem.utils;

import com.enrollmentsystem.dtos.ClasslistRecordDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {
    public static void generate(List<ClasslistRecordDTO> data, File file, String sectionName, String schoolYear) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sectionName);

            Row titleRow = sheet.createRow(0);
            titleRow.setHeight((short) 600);

            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(sectionName + " - " + schoolYear);

            titleCell.setCellStyle(setTitleStyle(workbook));

            String[] headers = {"LRN", "LAST NAME", "FIRST NAME", "MIDDLE NAME", "GENDER", "GRADE LEVEL", "SECTION"};
            int lastCol = headers.length - 1;

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));

            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(setHeaderStyle(workbook));
            }

            int rowNum = 3;
            String currentGender = null;

            for (ClasslistRecordDTO record : data) {
                if (currentGender != null && !currentGender.equals(record.getGender().getGender())) {
                    rowNum++;
                }
                currentGender = record.getGender().getGender();

                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(record.getLrn());
                row.createCell(1).setCellValue(record.getLastName());
                row.createCell(2).setCellValue(record.getFirstName());
                row.createCell(3).setCellValue(record.getMiddleName());
                row.createCell(4).setCellValue(record.getGender().getGender());
                row.createCell(5).setCellValue(record.getGradeLevel());
                row.createCell(6).setCellValue(record.getSectionName());

                for (Cell cell : row) {
                    cell.setCellStyle(setDataStyle(workbook));
                }

                rowNum++;
            }

            for (int i = 0; i <= lastCol; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2048);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    private static CellStyle setTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return titleStyle;
    }

    private static CellStyle setHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    private static CellStyle setDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);

        return dataStyle;
    }
}
