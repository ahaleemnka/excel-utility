package com.excel.utility;


import com.excel.utility.dto.ListAndMapTestDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ListAndMapIT {

    @Ignore
    @Test
    public void testMapAndListExtractionToExcel() throws Exception {
        // Create test data with List and Map fields
        Map<String, String> map1 = new HashMap<>();
        map1.put("Department", "HR");
        map1.put("Location", "USA");

        Map<String, String> map2 = new HashMap<>();
        map2.put("Department", "IT");
        map2.put("Location", "UK");
        ListAndMapTestDto dto1 = new ListAndMapTestDto(
                101,
                "Alice",
                Arrays.asList("Admin", "Manager"),
                map1,
                LocalDateTime.now(),
                new BigDecimal("1000.50")
        );

        ListAndMapTestDto dto2 = new ListAndMapTestDto(
                102,
                "Bob",
                Arrays.asList("Developer", "Team Lead"),
                map2,
                LocalDateTime.now(),
                new BigDecimal("2000.75")
        );

        List<ListAndMapTestDto> testList = Arrays.asList(dto1, dto2);

        ExcelUtility excelUtility = new ExcelUtility();

        // Parse the generated Excel stream
        Workbook workbook = excelUtility.mapToExcel(testList);
        Sheet sheet = workbook.getSheet("TestDTO");

        // Validate headers
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow, "Header row should exist");

        List<String> expectedHeaders = Arrays.asList(
                "Id", "Name", "Roles", "Attributes", "Timestamp", "Salary"
        );

        for (int i = 0; i < expectedHeaders.size(); i++) {
            Cell cell = headerRow.getCell(i);
            assertNotNull(cell, "Header cell should not be null");
            assertEquals(expectedHeaders.get(i), cell.getStringCellValue(),
                    "Header mismatch at index " + i);
        }

        // Validate the first data row
        Row firstDataRow = sheet.getRow(1);
        assertNotNull(firstDataRow, "First data row should exist");

        // Verify data values mapped correctly
        assertEquals(101, (int) firstDataRow.getCell(0).getNumericCellValue(), "ID mismatch for first data row");
        assertEquals("Alice", firstDataRow.getCell(1).getStringCellValue(), "Name mismatch for first data row");

        // Validate the list mapped as a comma-separated string
        assertEquals("Admin, Manager", firstDataRow.getCell(2).getStringCellValue(), "Roles mismatch for first data row");

        // Validate the map content mapped with line breaks
        String expectedAttributes = "Department : HR" + "\nLocation : USA";
        assertEquals(expectedAttributes, firstDataRow.getCell(3).getStringCellValue(), "Attributes mismatch for first data row ");

        // Check the timestamp is populated
        assertNotNull(firstDataRow.getCell(4), "Timestamp should exist");
        assertEquals(dto1.getTimestamp().toString(), firstDataRow.getCell(4).getStringCellValue(), "Timestamp mismatch");

        // Verify salary value
        assertEquals(new BigDecimal("1000.50").toString(),
                firstDataRow.getCell(5).getStringCellValue(), "Salary mismatch");

        // Validate the second data row
        Row secondDataRow = sheet.getRow(2);
        assertNotNull(secondDataRow, "Second data row should exist");

        assertEquals(102, (int) secondDataRow.getCell(0).getNumericCellValue(), "ID mismatch for second data row");
        assertEquals("Bob", secondDataRow.getCell(1).getStringCellValue(), "Name mismatch for second data row");
        assertEquals("Developer, Team Lead", secondDataRow.getCell(2).getStringCellValue(), "Roles mismatch for second data row");

        String expectedAttributes2 = "Department : IT\nLocation : UK";
        assertEquals(expectedAttributes2, secondDataRow.getCell(3).getStringCellValue(), "Attributes mismatch for second data row");

        assertNotNull(secondDataRow.getCell(4), "Timestamp should exist");
        assertEquals(dto2.getTimestamp().toString(), secondDataRow.getCell(4).getStringCellValue(), "Timestamp mismatch");

        assertEquals(new BigDecimal("2000.75").toString(),
                secondDataRow.getCell(5).getStringCellValue(), "Salary mismatch");

        workbook.close();
    }

}
