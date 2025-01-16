package com.excel.utility;


import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ExcelUtilityListMapNestedObjectIT {

    @Test
    public void testExcelMapperWithAdvancedScenarios() {
        Map<String, String> map = new HashMap<>();
        map.put("NullKey", null);
        map.put(null, "NullValue");

        // Sample input data with special characters and complex structures
        List<ComplexTestData> inputData = Arrays.asList(
                new ComplexTestData(1, "John@Doe", "john@example.com", Arrays.asList("Tag1", "Tag@2", "Tag\n3"),
                        Map.of("Key1", "Value1", "Key@2", "Value#2", "Key\n3", "Value\t3"), new NestedObject("Nested@Data")),
                new ComplexTestData(2, "Alice\nBob", "alice@example.com", Arrays.asList("A", "B@C"),
                        Map.of("A@Key", "B#Value", "C\nKey", "D\tValue"), new NestedObject("Nested\tInfo")),
                new ComplexTestData(3, "Eve\t123", null, Collections.emptyList(),
                        map, null)
        );

        ExcelUtility excelMapper = new ExcelUtility();

        Workbook workbook = excelMapper.mapToHSSFWorkbook(inputData);
        Sheet sheet = workbook.getSheetAt(0);

        // Validate headers
        Row headerRow = sheet.getRow(0);
        assertNotNull("Header row should not be null", headerRow);
        assertEquals("Header for Name is incorrect", "Name", headerRow.getCell(0).getStringCellValue());
        assertEquals("Header for Email is incorrect", "Email", headerRow.getCell(1).getStringCellValue());
        assertEquals("Header for Tags is incorrect", "Tags", headerRow.getCell(2).getStringCellValue());
        assertEquals("Header for Map is incorrect", "Map", headerRow.getCell(3).getStringCellValue());
        assertEquals("Header for Nested Object is incorrect", "Nested Object", headerRow.getCell(4).getStringCellValue());

        // Validate data rows
        validateDataRow(sheet.getRow(1), "John@Doe", "john@example.com", "Tag1, Tag@2, Tag\n3",
                Arrays.asList("Key1 : Value1", "Key@2 : Value#2", "Key\n3 : Value\t3"), "Nested@Data");
        validateDataRow(sheet.getRow(2), "Alice\nBob", "alice@example.com", "A, B@C",
                Arrays.asList("A@Key : B#Value", "C\nKey : D\tValue"), "Nested\tInfo");
        validateDataRow(sheet.getRow(3), "Eve\t123", "", "", Arrays.asList("NullKey : <empty>", "<empty> : NullValue"), "");
    }

    private void validateDataRow(Row row, String name, String email, String tags, List<String> map, String nestedObject) {
        assertNotNull("Data row should not be null", row);
        assertEquals("Name value is incorrect", name, row.getCell(0).getStringCellValue());
        assertEquals("Email value is incorrect", email, row.getCell(1).getStringCellValue());
        assertEquals("Tags value is incorrect", tags, row.getCell(2).getStringCellValue());
        for (String keyValue : map) {
            assertTrue("Map value is incorrect \nExpected :\n" + keyValue + "\nActual :\n" + row.getCell(3).getStringCellValue(), row.getCell(3).getStringCellValue().contains(keyValue));
        }
        assertEquals("Nested Object value is incorrect", nestedObject, row.getCell(4).getStringCellValue());
    }
}

@ExcelMapper
class ComplexTestData {
    @ExcelColumn(header = "Name", columnOrder = 1)
    private String name;

    @ExcelColumn(header = "Email", columnOrder = 2)
    private String email;

    @ExcelColumn(header = "Tags", columnOrder = 3)
    private List<String> tags;

    @ExcelColumn(header = "Map", columnOrder = 4)
    private Map<String, String> dataMap;

    @ExcelColumn(header = "Nested Object", columnOrder = 5)
    private NestedObject nestedObject;

    public ComplexTestData(Integer id, String name, String email, List<String> tags, Map<String, String> dataMap, NestedObject nestedObject) {
        this.name = name;
        this.email = email;
        this.tags = tags;
        this.dataMap = dataMap;
        this.nestedObject = nestedObject;
    }
}

class NestedObject {
    private String data;

    public NestedObject(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}

