package com.excel.utility.extractor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ObjectValueExtractorTest {

    private final ObjectValueExtractor objectValueExtractor = new ObjectValueExtractor();

    @Test
    void testProcess_WithGetterMethod() throws NoSuchFieldException {
        // Setup: Create an object with a field that has a getter method
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("employeeId");

        // Act: Call the process method to retrieve the field value via getter
        Object fieldValue = objectValueExtractor.process(testObject, field);

        // Assert: Ensure the field value is returned correctly using the getter method
        assertEquals(101, fieldValue);
    }

    @Test
    void testProcess_WithIsMethod() throws NoSuchFieldException {
        // Setup: Create an object with a field that has an is-method (boolean field)
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("employeeActive");

        // Act: Call the process method to retrieve the field value via is-method
        Object fieldValue = objectValueExtractor.process(testObject, field);

        // Assert: Ensure the field value is returned correctly using the is-method
        assertTrue((Boolean) fieldValue);  // Cast fieldValue to Boolean for the assertion
    }

    @Test
    void testProcess_WithDirectFieldAccess() throws NoSuchFieldException {
        // Setup: Create an object with a field without getter or is-method
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("employeeName");

        // Act: Call the process method to retrieve the field value directly
        Object fieldValue = objectValueExtractor.process(testObject, field);

        // Assert: Ensure the field value is returned correctly using direct field access
        assertEquals("John Doe", fieldValue);
    }

    @Test
    void testProcess_FieldNotFound() {
        // Setup: Create an object with a valid field
        MyTestObject testObject = new MyTestObject();
        MyTestObjectWithException testObjectWithException = new MyTestObjectWithException();

        // Act: Call the process method on a non-existent field
        // Directly create a Field object for a non-existent field
        Field field = null;
        try {
            field = testObjectWithException.getClass().getDeclaredField("otherEmployee");
        } catch (NoSuchFieldException e) {
            // Act: We expect the exception to be caught here
            // Process should handle this and return null
        }

        // Assert: Ensure the process method returns null when the field is not found
        Field finalField = field;
        assertThrows(IllegalArgumentException.class, () -> objectValueExtractor.process(testObject, finalField));
    }


    @Test
    void testProcess_WithNullField() {
        // Setup: Create an object with a valid field but pass null for the field
        MyTestObject testObject = new MyTestObject();

        // Act & Assert: Ensure passing null for field results in a NullPointerException
        assertThrows(NullPointerException.class, () -> objectValueExtractor.process(testObject, null));
    }

    @Test
    void testProcess_WithNullObject() throws NoSuchFieldException {
        // Setup: Create a field to extract from a null object
        MyTestObject testObject = null;
        Field field = MyTestObject.class.getDeclaredField("employeeId");

        // Act: Call the process method with a null object
        assertThrows(NullPointerException.class, () -> objectValueExtractor.process(testObject, field));
    }

    @Test
    void testProcess_WithNoGetterAndIsMethod() throws NoSuchFieldException {
        // Setup: Create an object with a field that has no getter or is-method
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("employeeName");

        // Act: Call the process method to retrieve the field value directly
        Object fieldValue = objectValueExtractor.process(testObject, field);

        // Assert: Ensure direct field access retrieves the correct value
        assertEquals("John Doe", fieldValue);
    }

    @Test
    void testProcess_WithPrivateField() throws NoSuchFieldException {
        // Setup: Create an object with a private field
        MyTestObject testObject = new MyTestObject();
        Field field = testObject.getClass().getDeclaredField("privateField");

        // Act: Call the process method to retrieve the field value directly
        Object fieldValue = objectValueExtractor.process(testObject, field);

        // Assert: Ensure the value is extracted even though the field is private
        assertEquals("Private Value", fieldValue);
    }


    // Helper class for testing
    static class MyTestObject {
        private int employeeId = 101;
        private boolean employeeActive = true;
        private String employeeName = "John Doe";
        private String privateField = "Private Value";

        public int getEmployeeId() {
            return employeeId;
        }

        public boolean isEmployeeActive() {
            return employeeActive;
        }

        private String getEmployeeName() {
            return employeeName;
        }

        private String getPrivateField() {
            return privateField;
        }
    }

    // Helper class that throws exception in getter method
    static class MyTestObjectWithException {
        private int employeeId;

        private String otherEmployee;

        public int getEmployeeId() {
            throw new RuntimeException("Error accessing field");
        }
    }
}
