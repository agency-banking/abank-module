package com.agencybanking.core.storage;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.List;

public interface ExcelProvider {

    List<String> getSheetHeader(Sheet sheet);
    Sheet getSheet(File fileName, int sheetNum);
    Workbook getWorkBook(File fileName);
    String getRowData(Sheet sheet, String colName, int rowNum);
}
