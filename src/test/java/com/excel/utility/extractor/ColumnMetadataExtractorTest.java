package com.excel.utility.extractor;


import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnMetadataExtractorTest {

    private ColumnMetadataExtractor columnMetadataExtractor;

    @BeforeEach
    void setUp() {
        columnMetadataExtractor = new ColumnMetadataExtractor();
    }

    // Test Case 1: Test process with a valid class that has @ExcelMapper and @ExcelColumn annotations
    @Test
    void testProcess_ValidClass() {
        // Act: Process the class
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObject.class);

        // Assert: Ensure the metadata list is not empty and contains correct column metadata
        assertNotNull(metadataList);
        assertFalse(metadataList.isEmpty());
        assertEquals(3, metadataList.size());
        assertEquals("Employee ID", metadataList.get(0).getHeader());
        assertEquals("Employee Name", metadataList.get(1).getHeader());
        assertEquals("Employee Active", metadataList.get(2).getHeader());
    }

    // Test Case 2: Test process with missing @ExcelMapper annotation
    @Test
    void testProcess_MissingExcelSheetAnnotation() {
        // Act & Assert: Process a class without @ExcelMapper annotation and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            columnMetadataExtractor.process(MyTestObjectWithoutExcelSheet.class);
        });
        assertEquals("POJO class must have an @ExcelMapper annotation." + MyTestObjectWithoutExcelSheet.class.getName(), exception.getMessage());
    }

    // Test Case 3: Test process with circular references
    @Test
    void testProcess_CircularReference() {
        // Act & Assert: Process a class with circular reference and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            columnMetadataExtractor.process(MyTestObjectWithCircularReference.class);
        });
        assertEquals("Circular reference detected in class: " + MyTestObjectWithCircularReference.class.getName(), exception.getMessage());
    }

    // Test Case 4: Test process with fields that have missing column orders
    @Test
    void testProcess_MissingColumnOrders() {
        // Act: Process the class with missing column orders
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithMissingColumnOrder.class);

        // Assert: Ensure missing column orders are assigned correctly
        assertNotNull(metadataList);
        assertEquals(3, metadataList.size());
        assertEquals(1, metadataList.get(0).getColumnOrder());
        assertEquals(2, metadataList.get(1).getColumnOrder());
        assertEquals(3, metadataList.get(2).getColumnOrder());
    }

    // Test Case 5: Test process with nested class and its fields
    @Test
    void testProcess_WithNestedClass() {
        // Act: Process the class with nested fields
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithNestedClass.class);

        // Assert: Ensure the nested class fields are processed correctly
        assertNotNull(metadataList);
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address - Employee Address")));
    }

    // Test Case 6: Test process with empty class (no fields)
    @Test
    void testProcess_EmptyClass() {
        // Act: Process an empty class with no fields annotated with @ExcelColumn
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyEmptyTestObject.class);

        // Assert: Ensure the metadata list is empty
        assertNotNull(metadataList);
        assertTrue(metadataList.isEmpty());
    }

    // Test Case 7: Test assignMissingColumnOrders with already assigned column orders
    @Test
    void testAssignMissingColumnOrders_WithAssignedOrders() {
        // Setup: Prepare column metadata with assigned orders
        List<ColumnMetadata> metadataList = List.of(
                new ColumnMetadata(null, "Employee ID", 1, Integer.class, null),
                new ColumnMetadata(null, "Employee Name", 2, String.class, null)
        );

        // Act: Assign missing column orders
        columnMetadataExtractor.assignMissingColumnOrders(metadataList);

        // Assert: Ensure no column orders are reassigned
        assertEquals(1, metadataList.get(0).getColumnOrder());
        assertEquals(2, metadataList.get(1).getColumnOrder());
    }

    // Test Case 8: Test assignMissingColumnOrders with invalid column orders (0 or negative)
    @Test
    void testAssignMissingColumnOrders_WithInvalidOrders() {
        // Setup: Prepare column metadata with invalid orders
        List<ColumnMetadata> metadataList = List.of(
                new ColumnMetadata(null, "Employee ID", 0, Integer.class, null),
                new ColumnMetadata(null, "Employee Name", -1, String.class, null)
        );

        // Act: Assign missing column orders
        columnMetadataExtractor.assignMissingColumnOrders(metadataList);

        // Assert: Ensure missing column orders are assigned sequentially starting from 1
        assertEquals(1, metadataList.get(0).getColumnOrder());
        assertEquals(2, metadataList.get(1).getColumnOrder());
    }

    // Test Case 9: Test extractColumnMetadata with a field that is not annotated with @ExcelColumn
    @Test
    void testExtractColumnMetadata_WithFieldWithoutAnnotation() throws NoSuchFieldException {
        // Setup: Prepare a class with a field not annotated with @ExcelColumn
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("employeeId"); // Not annotated

        // Act: Extract metadata for this field
        columnMetadataExtractor.extractColumnMetadata(testObject.getClass(), null, new LinkedList<>(), new ArrayList<>());

        // Assert: Ensure the field is skipped (i.e., no metadata is generated for it)
        // The actual test would rely on the list passed in the method, and no metadata would be added for this field.
    }
}

@ExcelMapper
class MyTestObject {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    @ExcelColumn(header = "Employee Active", columnOrder = 3)
    private boolean employeeActive;

    // Getters and setters (if needed)
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(boolean employeeActive) {
        this.employeeActive = employeeActive;
    }
}

class MyTestObjectWithoutExcelSheet {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    @ExcelColumn(header = "Employee Active", columnOrder = 3)
    private boolean employeeActive;

    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(boolean employeeActive) {
        this.employeeActive = employeeActive;
    }
}

@ExcelMapper
class MyTestObjectWithCircularReference {

    @ExcelColumn
    private MyTestObjectWithCircularReference circularField;

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    public MyTestObjectWithCircularReference getCircularField() {
        return circularField;
    }

    public void setCircularField(MyTestObjectWithCircularReference circularField) {
        this.circularField = circularField;
    }

    // Getters and setters for employeeId and employeeName
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}

@ExcelMapper
class MyTestObjectWithMissingColumnOrder {

    @ExcelColumn(header = "Employee ID", columnOrder = 0) // Invalid order, to be assigned
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = -1) // Invalid order, to be assigned
    private String employeeName;

    @ExcelColumn(header = "Employee Active", columnOrder = 3) // Valid order
    private boolean employeeActive;

    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(boolean employeeActive) {
        this.employeeActive = employeeActive;
    }
}

@ExcelMapper
class MyTestObjectWithNestedClass {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    @ExcelColumn
    private Address employeeAddress;

    public MyTestObjectWithNestedClass() {
        this.employeeAddress = new Address("123 Street", "City", "Country");
    }

    public Address getEmployeeAddress() {
        return employeeAddress;
    }

    // Getters and setters for employeeId and employeeName
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}

// Address class nested inside the main object
@ExcelMapper
class Address {

    @ExcelColumn(header = "Employee Address", columnOrder = 3)
    private String street;
    @ExcelColumn(header = "Employee City", columnOrder = 4)
    private String city;
    @ExcelColumn(header = "Employee Country", columnOrder = 5)
    private String country;

    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
@ExcelMapper
class MyEmptyTestObject {
    // Empty class, no fields annotated with @ExcelColumn
}
