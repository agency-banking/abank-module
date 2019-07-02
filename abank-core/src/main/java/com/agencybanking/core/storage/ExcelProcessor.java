package com.agencybanking.core.storage;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelProcessor {

    private Sheet sheet;

    public ExcelProcessor(Sheet sheet) {
        this.sheet = sheet;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public static Workbook getWorkBook(File fileName) {
        Workbook workbook = null;
        try {
            String myFileName = fileName.getName();
            String extension = myFileName.substring(myFileName.lastIndexOf("."));
            if (extension.equalsIgnoreCase(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(fileName));
            } else if (extension.equalsIgnoreCase(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(fileName));
            } else {
                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return workbook;
    }

    public String getCellData(String colName, int rowNum) {
        return getCellStringValue(sheet.getRow(rowNum).getCell(getPosition(colName)));
    }

//    private Cell getCell(String colName, int rowNum) {
//        Row row = getSheet().getRow(0);
//        Cell cell;
//        int lastCellNum = row.getLastCellNum();
//        int colNum = -1;
//
//        System.out.println("Col number " + lastCellNum);
//
//        for (int i = 0; i < lastCellNum; i++) {
//            if (getCellStringValue(row.getCell(i)).trim().equals(colName)) {
//                colNum = i;
//            }
//        }
//        if (colNum > -1) {
//            row = getSheet().getRow(rowNum);
//            cell = row.getCell(colNum);
//            return cell;
//        }
//        return null;
//    }

    public boolean isRowEmpty(int rowNum) {
        return StringUtils.isEmpty(getCellStringValue(getSheet().getRow(rowNum).getCell(0)));
    }

    private static String getCellStringValue(Cell cell) {
        StringBuilder val = new StringBuilder();
        if (!ObjectUtils.isEmpty(cell)) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    val.append(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    val.append(cell.getBooleanCellValue());
                    break;
                case STRING:
                    val.append(cell.getStringCellValue());
                    break;
                case BLANK:
                    break;
                default:
                    break;
            }
        }
        return val.toString();
    }

    public List<String> getSheetHeader() {
        List<String> headerNames = new ArrayList<>();
        if (null != getSheet()) {
            int lastCellNum = getSheet().getRow(0).getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                String colName = getSheet().getRow(0).getCell(i).getStringCellValue();
                headerNames.add(colName);
            }
        }
        return headerNames;
    }

    private int getPosition(String headerName) {
        Map<String, Integer> nameAndPosition = getColNameAndPosition();
        return nameAndPosition.get(headerName);
    }

    public Map<String, Integer> getColNameAndPosition() {
        Map<String, Integer> map = new HashMap<>();
        if (null != getSheet()) {
            int lastCellNum = getSheet().getRow(0).getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                String colName = getSheet().getRow(0).getCell(i).getStringCellValue();
                if (!StringUtils.isEmpty(colName)) {
                    map.put(colName, i);
                }
            }
        }
        return map;
    }
}
