package utils;

import constants.FrameworkConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelReader - Reads test data from Excel files
 * Supports data-driven testing with multiple data sets
 */
public class ExcelReader {
    
    /**
     * Get test data as 2D Object array (for TestNG DataProvider)
     * @param fileName - Excel file name
     * @param sheetName - Sheet name
     * @return 2D array of test data
     */
    public static Object[][] getTestData(String fileName, String sheetName) {
        String filePath = FrameworkConstants.TEST_DATA_PATH + fileName;
        Object[][] data = null;
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            data = new Object[rowCount][colCount];
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i-1][j] = getCellValue(cell);
                }
            }
            
            LogUtil.info("Test data loaded from: " + fileName + " | Sheet: " + sheetName);
            
        } catch (IOException e) {
            LogUtil.error("Error reading Excel file: " + fileName, e);
        }
        
        return data;
    }
    
    /**
     * Get test data as List of Maps (column name -> value)
     * @param fileName - Excel file name
     * @param sheetName - Sheet name
     * @return List of data maps
     */
    public static List<Map<String, String>> getTestDataAsMap(String fileName, String sheetName) {
        String filePath = FrameworkConstants.TEST_DATA_PATH + fileName;
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> dataMap = new HashMap<>();
                
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    String value = getCellValue(row.getCell(j));
                    dataMap.put(key, value);
                }
                dataList.add(dataMap);
            }
            
            LogUtil.info("Test data loaded as Map from: " + fileName);
            
        } catch (IOException e) {
            LogUtil.error("Error reading Excel file: " + fileName, e);
        }
        
        return dataList;
    }
    
    /**
     * Get cell value as String regardless of cell type
     * @param cell - Excel cell
     * @return String value of cell
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
