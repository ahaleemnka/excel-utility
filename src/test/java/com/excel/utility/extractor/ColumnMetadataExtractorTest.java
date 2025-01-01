package com.excel.utility.extractor;


import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.AnnotationTypeMismatchException;
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

    // Test Case 10: Test process with includeAll = true
    @Test
    void testProcess_WithIncludeAllTrue() {
        // Act: Process the class where includeAll is true
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllTrue.class);

        // Assert: Ensure that fields without @ExcelColumn are included in the metadata
        assertNotNull(metadataList);
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address")));
        assertEquals(3, metadataList.size()); // Expect 4 fields, including the one without @ExcelColumn
    }

    // Test Case 11: Test process with includeAll = false
    @Test
    void testProcess_WithIncludeAllFalse() {
        // Act: Process the class where includeAll is false
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllFalse.class);

        // Assert: Ensure that fields without @ExcelColumn are NOT included in the metadata
        assertNotNull(metadataList);
        assertFalse(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address")));
        assertEquals(2, metadataList.size()); // Expect 2 fields, only those annotated with @ExcelColumn
    }

    // Test Case 12: Test process with includeAll = true and no fields with @ExcelColumn
    @Test
    void testProcess_WithIncludeAllTrue_NoExcelColumnFields() {
        // Act: Process a class where includeAll is true but no fields have @ExcelColumn
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllTrueAndNoExcelColumn.class);

        // Assert: Ensure all fields, even those without @ExcelColumn, are included in the metadata
        assertNotNull(metadataList);
        assertEquals(3, metadataList.size()); // Expect 3 fields (all fields in the class)
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Age")));
    }

    // Test Case 13: Test process with includeAll = false and only fields with @ExcelColumn
    @Test
    void testProcess_WithIncludeAllFalse_OnlyExcelColumnFields() {
        // Act: Process a class where includeAll is false and only @ExcelColumn fields are present
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllFalseAndOnlyExcelColumnFields.class);

        // Assert: Ensure only fields with @ExcelColumn are included
        assertNotNull(metadataList);
        assertEquals(2, metadataList.size()); // Expect only 2 fields (those with @ExcelColumn)
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Age")));
    }

    // Test Case 14: Test process with includeAll = true and multiple non-@ExcelColumn fields
    @Test
    void testProcess_WithIncludeAllTrue_MultipleNonExcelColumnFields() {
        // Act: Process a class with multiple non-@ExcelColumn fields and includeAll = true
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllTrueAndMultipleNonExcelColumnFields.class);

        // Assert: Ensure that non-@ExcelColumn fields are included
        assertNotNull(metadataList);
        assertEquals(4, metadataList.size()); // Expect 4 fields (3 with @ExcelColumn, 1 without)
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Age")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee ID")));
    }

    // Test Case 15: Test process with includeAll = false and some fields without @ExcelColumn
    @Test
    void testProcess_WithIncludeAllFalse_SomeFieldsWithoutExcelColumn() {
        // Act: Process a class where includeAll is false, and some fields are not annotated with @ExcelColumn
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllFalseAndSomeNonExcelColumnFields.class);

        // Assert: Ensure that only the @ExcelColumn annotated fields are included
        assertNotNull(metadataList);
        assertEquals(2, metadataList.size()); // Expect 2 fields (only those with @ExcelColumn)
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee ID")));
    }

    // Test Case 16: Test process with includeAll = true on a class with nested objects and non-@ExcelColumn fields
    @Test
    void testProcess_WithIncludeAllTrue_NestedClassAndNonExcelColumnFields() {
        // Act: Process a class with a nested class and non-@ExcelColumn fields, with includeAll = true
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllTrueAndNestedClass.class);

        // Assert: Ensure that the nested class fields and non-@ExcelColumn fields are included
        assertNotNull(metadataList);
        assertEquals(5, metadataList.size()); // 4 fields from the outer class + 2 from the nested class
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Address")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Details With Annotation - City")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Age")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Details With Annotation - Country")));
    }

    // Test Case 17: Test process with includeAll = false and nested objects
    @Test
    void testProcess_WithIncludeAllFalse_NestedClassExclusion() {
        // Act: Process a class with nested objects, includeAll = false, and no @ExcelColumn on nested fields
        List<ColumnMetadata> metadataList = columnMetadataExtractor.process(MyTestObjectWithIncludeAllFalseAndNestedClassExclusion.class);

        // Assert: Ensure that nested class fields without @ExcelColumn are not included
        assertNotNull(metadataList);
        assertEquals(2, metadataList.size()); // Only 2 fields from the outer class (nested fields are excluded)
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Name")));
        assertTrue(metadataList.stream().anyMatch(metadata -> metadata.getHeader().equals("Employee Age")));
    }

    // Test Case 18: Test process with includeAll = true on a class with nested objects and non-@ExcelMapper annotation
    @Test
    void testProcess_WithIncludeAllTrue_NestedClassAndWithoutAnnotation() {
        // Assert: Ensure that the nested class is annotated with @ExcelMapper throws exception
        assertThrows(IllegalArgumentException.class,() -> columnMetadataExtractor.process(MyTestObjectWithIncludeAllTrueAndNestedClassWithoutAnnotation.class));
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

// New class with includeAll = true
@ExcelMapper(includeAll = true)
class MyTestObjectWithIncludeAllTrue {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    private String employeeAddress; // This field does not have @ExcelColumn but should be included because includeAll = true

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

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }
}

// New class with includeAll = false
@ExcelMapper(includeAll = false)
class MyTestObjectWithIncludeAllFalse {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    private String employeeAddress; // This field does not have @ExcelColumn and should be excluded

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

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }
}

// New class with includeAll = true and no @ExcelColumn fields
@ExcelMapper(includeAll = true)
class MyTestObjectWithIncludeAllTrueAndNoExcelColumn {

    private String employeeName; // Not annotated with @ExcelColumn
    private String employeeAddress; // Not annotated with @ExcelColumn
    private int employeeAge; // Not annotated with @ExcelColumn

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }
}

// New class with includeAll = false and only @ExcelColumn fields
@ExcelMapper(includeAll = false)
class MyTestObjectWithIncludeAllFalseAndOnlyExcelColumnFields {

    @ExcelColumn(header = "Employee Name", columnOrder = 1)
    private String employeeName;

    @ExcelColumn(header = "Employee Age", columnOrder = 2)
    private int employeeAge;

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }
}

// New class with includeAll = true and multiple non-@ExcelColumn fields
@ExcelMapper(includeAll = true)
class MyTestObjectWithIncludeAllTrueAndMultipleNonExcelColumnFields {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    private String employeeAddress; // Not annotated with @ExcelColumn
    private int employeeAge; // Not annotated with @ExcelColumn

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

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }
}

// New class with includeAll = false and some fields without @ExcelColumn
@ExcelMapper(includeAll = false)
class MyTestObjectWithIncludeAllFalseAndSomeNonExcelColumnFields {

    @ExcelColumn(header = "Employee ID", columnOrder = 1)
    private int employeeId;

    @ExcelColumn(header = "Employee Name", columnOrder = 2)
    private String employeeName;

    private String employeeAddress; // Not annotated with @ExcelColumn
    private int employeeAge; // Not annotated with @ExcelColumn

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

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }
}

@ExcelMapper(includeAll = true)
class MyTestObjectWithIncludeAllTrueAndNestedClass {

    @ExcelColumn(header = "Employee Name", columnOrder = 1)
    private String employeeName;

    @ExcelColumn(header = "Employee Age", columnOrder = 2)
    private int employeeAge;

    private String employeeAddress; // Not annotated with @ExcelColumn

    private EmployeeDetailsWithAnnotation employeeDetailsWithAnnotation; // Nested class, not annotated with @ExcelColumn

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public EmployeeDetailsWithAnnotation getEmployeeDetailsWithAnnotation() {
        return employeeDetailsWithAnnotation;
    }

    public void setEmployeeDetailsWithAnnotation(EmployeeDetailsWithAnnotation employeeDetailsWithAnnotation) {
        this.employeeDetailsWithAnnotation = employeeDetailsWithAnnotation;
    }
}

// Nested class
@ExcelMapper(includeAll = true)
class EmployeeDetailsWithAnnotation {
    private String city;
    private String country;

    // Getters and setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

@ExcelMapper(includeAll = true)
class MyTestObjectWithIncludeAllTrueAndNestedClassWithoutAnnotation {

    @ExcelColumn(header = "Employee Name", columnOrder = 1)
    private String employeeName;

    @ExcelColumn(header = "Employee Age", columnOrder = 2)
    private int employeeAge;

    private String employeeAddress; // Not annotated with @ExcelColumn

    private EmployeeDetailsWithoutAnnotation employeeDetailsWithoutAnnotation; // Nested class, not annotated with @ExcelColumn

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public EmployeeDetailsWithoutAnnotation getEmployeeDetailsWithoutAnnotation() {
        return employeeDetailsWithoutAnnotation;
    }

    public void setEmployeeDetailsWithoutAnnotation(EmployeeDetailsWithoutAnnotation employeeDetailsWithoutAnnotation) {
        this.employeeDetailsWithoutAnnotation = employeeDetailsWithoutAnnotation;
    }
}


// Nested class
class EmployeeDetailsWithoutAnnotation {
    private String city;
    private String country;

    // Getters and setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}


@ExcelMapper(includeAll = false)
class MyTestObjectWithIncludeAllFalseAndNestedClassExclusion {

    @ExcelColumn(header = "Employee Name", columnOrder = 1)
    private String employeeName;

    @ExcelColumn(header = "Employee Age", columnOrder = 2)
    private int employeeAge;

    private String employeeAddress; // Not annotated with @ExcelColumn

    private EmployeeDetailsWithAnnotation employeeDetailsWithAnnotation; // Nested class, excluded due to includeAll = false

    // Getters and setters
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public EmployeeDetailsWithAnnotation getEmployeeDetailsWithAnnotation() {
        return employeeDetailsWithAnnotation;
    }

    public void setEmployeeDetailsWithAnnotation(EmployeeDetailsWithAnnotation employeeDetailsWithAnnotation) {
        this.employeeDetailsWithAnnotation = employeeDetailsWithAnnotation;
    }
}
