package com.excel.utility;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AdvancedExcelMapperIT {

    List<String> largeMapValues = new ArrayList<>();
    List<String> largeList = new ArrayList<>();
    @Test
    public void testExcelMapperWithAdvancedEdgeCases() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("Key1", "Value1");
        map1.put("Key2", null);

        Map<String, String> map2 = new HashMap<>();
        map2.put(null, "Null Key");
        map2.put("Key3", "Value3");

        // Sample input data with varied scenarios
        List<AdvancedTestData> inputData = Arrays.asList(
                // Recursive Object
                new AdvancedTestData(1, "John", new ComplexNestedObject("Level 1", new ComplexNestedObject("Level 2", null)),
                        map1, Arrays.asList(123, "String", 45.67), null),

                // Unannotated Nested Object
                new AdvancedTestData(2, "Alice", new Object(),
                        map2, List.of(), "Direct ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                        "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                        "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                        "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                        "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect Value"),

                // Getter vs Field Discrepancies
                new AdvancedTestData(3, "Bob", new ComplexNestedObject("Only Getter", null),
                        Map.of("Key4", "Value4"), List.of("Element1", "Element2"), "Overridden Value"),

                // Large Data Set
                new AdvancedTestData(4, "Eve", null,
                        generateLargeMap(1000), generateLargeList(1000), null)
        );

        // Instantiate Excel utility for mapping
        ExcelUtility excelMapper = new ExcelUtility();

        // Generate and read the Excel file
        Workbook workbook = excelMapper.mapToExcel(inputData);
        Sheet sheet = workbook.getSheetAt(0);

        // Validate header row
        Row headerRow = sheet.getRow(0);
        assertNotNull("Header row should not be null", headerRow);
        assertEquals("Header for ID is incorrect", "ID", headerRow.getCell(0).getStringCellValue());
        assertEquals("Header for Name is incorrect", "Name", headerRow.getCell(1).getStringCellValue());
        assertEquals("Header for Complex Nested Object is incorrect", "Complex Nested Object", headerRow.getCell(2).getStringCellValue());
        assertEquals("Header for Map is incorrect", "Map", headerRow.getCell(3).getStringCellValue());
        assertEquals("Header for Mixed Collection is incorrect", "Mixed Collection", headerRow.getCell(4).getStringCellValue());
        assertEquals("Header for Direct Value is incorrect", "Direct Value", headerRow.getCell(5).getStringCellValue());

        // Validate data rows
        //validateDataRow(sheet.getRow(1), "1", "John", "Level 1 - Level 2", Arrays.asList("Key1 : Value1", "Key2 : null"), "[123, String, 45.67]", "");
        validateDataRow(sheet.getRow(2), "2", "Alice", "java.lang.Object", Arrays.asList("<empty> : Null Key", "Key3 : Value3"), "", "Direct ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect " +
                "ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect ValueDirect Value");
        validateDataRow(sheet.getRow(3), "3", "Bob", "Only Getter", Arrays.asList("Key4 : Value4"), "Element1, Element2", "Overridden Value");
        validateDataRow(sheet.getRow(4), "4", "Eve", "", largeMapValues, String.join(", ", largeList), "");
    }

    private void validateDataRow(Row row, String id, String name, String complexNestedObject, List<String> map, String mixedCollection, String directValue) {
        assertNotNull("Data row should not be null", row);
        assertEquals("ID value is incorrect", id, row.getCell(0).getStringCellValue());
        assertEquals("Name value is incorrect", name, row.getCell(1).getStringCellValue());
        assertTrue("Complex Nested Object value is incorrect", row.getCell(2).getStringCellValue().startsWith(complexNestedObject));

        for (String keyValue : map) {
            assertTrue("Map value is incorrect \nExpected :\n" + keyValue, row.getCell(3).getStringCellValue().contains(keyValue));
        }
        if(mixedCollection != null && mixedCollection.length() > 32767) {
            mixedCollection = mixedCollection.substring(0, 32760);
        }
        assertEquals("Mixed Collection value is incorrect", mixedCollection, row.getCell(4).getStringCellValue());
        assertEquals("Direct Value value is incorrect", directValue, row.getCell(5).getStringCellValue());
    }

    private Map<String, String> generateLargeMap(int size) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put("Key" + i, "Value" + i);
            largeMapValues.add("Key" + i + " : " + "Value" + i);
        }
        return map;
    }

    private List<Object> generateLargeList(int size) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("Element" + i);
            largeList.add("Element" + i);
        }
        return list;
    }
}

/**
 * Data class for testing complex and edge cases.
 */
@ExcelMapper
class AdvancedTestData {
    @ExcelColumn(header = "ID", columnOrder = 1)
    private final int id;

    @ExcelColumn(header = "Name", columnOrder = 2)
    private final String name;

    @ExcelColumn(header = "Complex Nested Object", columnOrder = 3)
    private final Object complexNestedObject;

    @ExcelColumn(header = "Map", columnOrder = 4)
    private final Map<String, String> map;

    @ExcelColumn(header = "Mixed Collection", columnOrder = 5)
    private final List<Object> mixedCollection;

    @ExcelColumn(header = "Direct Value", columnOrder = 6)
    private final String directValue;

    public AdvancedTestData(int id, String name, Object complexNestedObject, Map<String, String> map, List<Object> mixedCollection, String directValue) {
        this.id = id;
        this.name = name;
        this.complexNestedObject = complexNestedObject;
        this.map = map;
        this.mixedCollection = mixedCollection;
        this.directValue = directValue;
    }

    public Object getComplexNestedObject() {
        if (complexNestedObject instanceof ComplexNestedObject) {
            return ((ComplexNestedObject) complexNestedObject).getValue();
        }
        return complexNestedObject.toString();
    }
}

@ExcelMapper
class ComplexNestedObject {
    private final String value;
    private final ComplexNestedObject nestedObject;

    public ComplexNestedObject(String value, ComplexNestedObject nestedObject) {
        this.value = value;
        this.nestedObject = nestedObject;
    }

    public String getValue() {
        return value + (nestedObject != null ? " - " + nestedObject.getValue() : "");
    }
}

