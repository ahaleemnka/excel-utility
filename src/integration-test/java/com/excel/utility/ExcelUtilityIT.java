package com.excel.utility;

import com.excel.utility.annotation.ExcelMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.excel.utility.TestConfig.MAX_LENGTH_SHEET_NAME;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelUtilityIT {

    private final ExcelUtility excelUtility = new ExcelUtility();

    @Test
    void testSheetName_DefaultSheetName() {
        // Test with null sheet name
        assertThrows("Sheet name should not be NULL.", IllegalArgumentException.class, () -> excelUtility.mapToHSSFWorkbook(List.of(new TestData()), null));
    }

    @Test
    void testSheetName_EmptySheetName() {
        // Test with empty sheet name
        assertThrows("Sheet name should not be empty.", IllegalArgumentException.class, () -> excelUtility.mapToHSSFWorkbook(List.of(new TestData()), ""));
    }

    @Test
    void testSheetName_ValidSheetName() {
        // Test with a valid sheet name
        String validName = "ValidName";
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), validName);
        assertEquals(validName, workbook.getSheetName(0), "Sheet name should match the valid input.");
    }

    @Test
    void testSheetName_LongSheetName() {
        // Test with a long sheet name
        String longName = "ThisIsAVeryLongSheetNameWhichWillBeTrimmed";
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), longName);
        assertEquals(longName.substring(0, MAX_LENGTH_SHEET_NAME), workbook.getSheetName(0), "Sheet name should be truncated to the configured maximum length.");
    }

    @Test
    void testSheetName_ExcelMaxLimit() {
        // Test with a sheet name at the maximum limit
        String maxLengthName = "123456789012345678901234567890"; // 30 characters
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), maxLengthName);
        assertEquals(maxLengthName, workbook.getSheetName(0), "Sheet name should match the input at the Excel limit.");
    }

    @Test
    void testSheetName_ExceedingExcelLimit() {
        // Test with a sheet name exceeding the Excel limit
        String overLimitName = "12345678901234567890123456789012345"; // 35 characters
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), overLimitName);
        assertEquals(overLimitName.substring(0, MAX_LENGTH_SHEET_NAME), workbook.getSheetName(0), "Sheet name should be truncated to the configured maximum length.");
    }

    @Test
    void testSheetName_SpecialCharacters() {
        // Test with sheet names containing special characters
        String specialCharName = "Sheet@#&$%!";
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), specialCharName);
        assertEquals("Sheet@#&$%!", workbook.getSheetName(0), "Sheet name should allow special characters as-is if valid.");

        String invalidCharName = "Invalid/Name\\:*?";
        assertThrows("Sheet name should not contain invalid characters.", IllegalArgumentException.class, () ->excelUtility.mapToHSSFWorkbook(List.of(new TestData()), invalidCharName));
    }

    @Test
    void testSheetName_WhitespaceHandling() {
        // Test with leading/trailing/multiple spaces
        String nameWithSpaces = "   LeadingAndTrailingSpaces   ";
        Workbook workbook = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), nameWithSpaces);
        assertEquals(nameWithSpaces, workbook.getSheetName(0), "Sheet name should not trim leading and trailing spaces.");

        String nameWithInnerSpaces = "Multiple   Inner   Spaces";
        Workbook workbookInnerSpaces = excelUtility.mapToHSSFWorkbook(List.of(new TestData()), nameWithInnerSpaces);
        assertEquals("Multiple   Inner   Spaces", workbookInnerSpaces.getSheetName(0), "Sheet name should handle multiple inner spaces.");
    }

    /**
     * Dummy class annotated with @ExcelMapper for testing purposes.
     */
    @ExcelMapper
    static class TestData {
        // Example fields can be added here as needed
    }
}
