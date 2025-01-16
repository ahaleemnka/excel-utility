package com.excel.utility;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for ExcelMapper functionality, validating mapping scenarios with complex and edge cases.
 */
public class ExcelMapperIT {

    @Test
    public void testExcelMapperWithAdvancedScenarios() {
        // Sample input data with varied scenarios, including null fields and nested objects
        List<TestData> inputData = Arrays.asList(
                new TestData(5, null, null, null, null, null, null), // All null fields
                new TestData(2, "Alice", null, "alice@example.com", null, new Address(null, "City 2"), new File("/test")),
                new TestData(3, null, 25, "bob@example.com", Collections.singletonList("C"), new Address("Street 3", null),  new File("/test-new")),
                new TestData(4, "Eve", 28, "eve@example.com", null, null, null), // Null Address
                new TestData(1, "John", 30, "john@example.com", Arrays.asList("A", "B"), new Address("Street 1", "City 1"),  new File("/document"))
        );

        // Instantiate Excel utility for mapping
        ExcelUtility excelMapper = new ExcelUtility();

        // Generate and read the Excel file
        Workbook workbook = excelMapper.mapToHSSFWorkbook(inputData);
        Sheet sheet = workbook.getSheetAt(0);

        // Validate the content of the Excel file
        Row headerRow = sheet.getRow(0);
        assertNotNull("Header row should not be null", headerRow);
        assertEquals("Header for Full Name is incorrect", "Full Name", headerRow.getCell(0).getStringCellValue());
        assertEquals("Header for Email Address is incorrect", "Email Address", headerRow.getCell(1).getStringCellValue());
        assertEquals("Header for Age is incorrect", "Age", headerRow.getCell(2).getStringCellValue());
        assertEquals("Header for Tags is incorrect", "Tags", headerRow.getCell(4).getStringCellValue());
        assertEquals("Header for ID is incorrect", "ID", headerRow.getCell(3).getStringCellValue());
        assertEquals("Header for Address - Street is incorrect", "Address - Street", headerRow.getCell(5).getStringCellValue());
        assertEquals("Header for Address - City is incorrect", "Address - City", headerRow.getCell(6).getStringCellValue());
        assertEquals("Header for File is incorrect", "File", headerRow.getCell(7).getStringCellValue());

        // Validate data rows with utility method
        validateDataRow(sheet.getRow(1), "No Name", "", "", "", "5", "", "", ""); // All null fields
        validateDataRow(sheet.getRow(2), "Alice", "alice@example.com", "", "", "2", "", "City 2", "/test");
        validateDataRow(sheet.getRow(3), "No Name", "bob@example.com", "25", "C", "3", "Street 3", "", "/test-new");
        validateDataRow(sheet.getRow(4), "Eve", "eve@example.com", "28", "", "4", "", "", "");// Null Address
        validateDataRow(sheet.getRow(5), "John", "john@example.com", "30", "A, B", "1", "Street 1", "City 1", "/document");
    }

    /**
     * Utility method to validate a single row of data in the Excel sheet.
     *
     * @param row    The Excel row to validate.
     * @param name   Expected name value.
     * @param email  Expected email value.
     * @param age    Expected age value.
     * @param tags   Expected tags value.
     * @param id     Expected ID value.
     * @param street Expected street value.
     * @param city   Expected city value.
     * @param file
     */
    private void validateDataRow(Row row, String name, String email, String age, String tags, String id, String street, String city, String file) {
        assertNotNull("Data row should not be null", row);
        assertEquals("Name value is incorrect", name, row.getCell(0).getStringCellValue());
        assertEquals("Email value is incorrect", email, row.getCell(1).getStringCellValue());
        assertEquals("Age value is incorrect", age, row.getCell(2).getStringCellValue());
        assertEquals("Tags value is incorrect", tags, row.getCell(4).getStringCellValue());
        assertEquals("ID value is incorrect", id, row.getCell(3).getStringCellValue());
        assertEquals("Street value is incorrect", street, row.getCell(5).getStringCellValue());
        assertEquals("City value is incorrect", city, row.getCell(6).getStringCellValue());
        assertEquals("File value is incorrect", file, row.getCell(7).getStringCellValue());
    }
}

/**
 * Data class representing test data with annotated fields for Excel mapping.
 */
@ExcelMapper(includeAll = false)
class TestData {
    @ExcelColumn(header = "Full Name", columnOrder = 1)
    private String name;

    @ExcelColumn(header = "ID", columnOrder = 4)
    private Integer id;

    @ExcelColumn(header = "Email Address", columnOrder = 2)
    private String email;

    @ExcelColumn(header = "Tags", columnOrder = 5)
    private List<String> tags;

    @ExcelColumn(header = "Age", columnOrder = 3)
    private Integer age;

    @ExcelColumn
    private Address address;

    @ExcelColumn
    private File file;

    public TestData(Integer id, String name, Integer age, String email, List<String> tags, Address address, File file) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.tags = tags;
        this.address = address;
        this.file = file;
    }

    public String getName() {
        return name != null ? name : "No Name";
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getTags() {
        return tags;
    }

    public Integer getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }
}

/**
 * Nested Address class representing address fields for Excel mapping.
 */
@ExcelMapper(includeAll = true)
class Address {
    private String street;
    private String city;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }
}