package com.excel.utility;

import org.junit.jupiter.api.Test;

import static com.excel.utility.Config.DEFAULT_SHEET_NAME;
import static com.excel.utility.Config.MAX_LENGTH_SHEET_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExcelUtilityTest {

    private final ExcelUtility excelUtility = new ExcelUtility();

    @Test
    void testSheetName_NullInput() {
        // Test when sheet name is null
        String result = invokeGetSheetName(null);
        assertEquals(DEFAULT_SHEET_NAME, result, "Sheet name should default to 'Sheet' when input is null.");
    }

    @Test
    void testSheetName_EmptyInput() {
        // Test when sheet name is an empty string
        String result = invokeGetSheetName("");
        assertEquals(DEFAULT_SHEET_NAME, result, "Sheet name should default to 'Sheet' when input is empty.");
    }

    @Test
    void testSheetName_ValidInput() {
        // Test when a valid sheet name is provided
        String result = invokeGetSheetName("ValidName");
        assertEquals("ValidName", result, "Sheet name should match the valid input.");
    }

    @Test
    void testSheetName_LongInput() {
        // Test when the sheet name exceeds the allowed length
        String longName = "ThisIsALongSheetNameThatExceedsExcelLimit";
        String result = invokeGetSheetName(longName);
        assertEquals(longName.substring(0, MAX_LENGTH_SHEET_NAME), result,
                "Sheet name should be truncated to the first " + MAX_LENGTH_SHEET_NAME + " characters.");
    }

    @Test
    void testSheetName_MaxExcelLimit() {
        // Test when the sheet name exactly matches Excel's 31-character limit
        String maxLengthName = "123456789012345678901234567890"; // 31 characters
        String result = invokeGetSheetName(maxLengthName);
        assertEquals(maxLengthName, result, "Sheet name should match the input when it is within Excel's limit.");
    }

    @Test
    void testSheetName_ExceedsExcelLimit() {
        // Test when the sheet name exceeds Excel's 31-character limit
        String overLimitName = "12345678901234567890123456789012345"; // 35 characters
        String result = invokeGetSheetName(overLimitName);
        assertEquals(overLimitName.substring(0, MAX_LENGTH_SHEET_NAME), result,
                "Sheet name should be truncated to " + MAX_LENGTH_SHEET_NAME + " characters for Excel compatibility.");
    }

    @Test
    void testSheetName_SpecialCharacters() {
        // Test with sheet names containing special characters
        String specialCharName = "Sheet@#&$%!";
        String result = invokeGetSheetName(specialCharName);
        assertEquals(specialCharName, result, "Sheet name should allow special characters as-is if valid.");

        String invalidCharName = "Invalid/Name\\:*?";
        String resultInvalid = invokeGetSheetName(invalidCharName);
        assertEquals("Invalid-Name----", resultInvalid,
                "Sheet name should replace invalid characters with dashes.");
    }

    @Test
    void testSheetName_WhitespaceInput() {
        // Test when the sheet name contains leading/trailing whitespace
        String whitespaceName = "   TrimmedName   ";
        String result = invokeGetSheetName(whitespaceName);
        assertEquals("TrimmedName", result, "Sheet name should be trimmed of leading/trailing whitespace.");
    }

    /**
     * Helper method to invoke the private `getSheetName` method using reflection.
     *
     * @param sheetName the input sheet name
     * @return the processed sheet name
     */
    private String invokeGetSheetName(String sheetName) {
        try {
            var method = ExcelUtility.class.getDeclaredMethod("getSheetName", String.class);
            method.setAccessible(true);
            return (String) method.invoke(excelUtility, sheetName);
        } catch (Exception e) {
            fail("Reflection invocation failed: " + e.getMessage());
            return null;
        }
    }
}
