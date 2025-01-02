package com.excel.utility.extractor;

import com.excel.utility.dto.ColumnMetadata;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldExtractorTest {

    private static final String MAP_DELIMITER = " : ";
    private final FieldExtractor fieldExtractor = new FieldExtractor();

    // Helper method for creating a dummy ColumnMetadata with parent class fields
    private ColumnMetadata createColumnMetadata(List<Field> parentClassFields) {
        ColumnMetadata metadata = new ColumnMetadata(null, null, 0, null, null);
        metadata.setParentClassFieldList(parentClassFields);
        return metadata;
    }

    @Test
    void testProcess_EmptyField() {
        // Given an empty object and field metadata
        ColumnMetadata metadata = createColumnMetadata(Collections.emptyList());
        Object targetObject = new Object();  // Random object

        // Process the object and assert empty string
        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("", result, "Empty field should return an empty string.");
    }

    @Test
    void testProcess_PrimitiveValue() throws NoSuchFieldException {
        // Test a primitive field in the target object
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("primitiveField")));
        SomeClass targetObject = new SomeClass(42);

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("42", result, "Primitive field value should be returned as a string.");
    }

    @Test
    void testProcess_WrapperValue() throws NoSuchFieldException {
        // Test a wrapper field in the target object
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("wrapperField")));
        SomeClass targetObject = new SomeClass(Integer.valueOf(100));

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("100", result, "Wrapper field value should be returned as a string.");
    }

    @Test
    void testProcess_NullValue() throws NoSuchFieldException {
        // Test a null field in the target object
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("nullField")));
        SomeClass targetObject = new SomeClass(Optional.ofNullable(null));

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("", result, "Null field value should return an empty string.");
    }

    @Test
    void testProcess_ListField() throws NoSuchFieldException {
        // Test a List field in the target object
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("listField")));
        SomeClass targetObject = new SomeClass(Arrays.asList("Apple", "Banana", "Cherry"));

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("Apple, Banana, Cherry", result, "List field should be flattened correctly.");
    }

    @Test
    void testProcess_MapField() throws NoSuchFieldException {
        // Test a Map field in the target object
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("mapField")));
        SomeClass targetObject = new SomeClass(Map.of("Key1", "Value1", "Key2", "Value2"));

        String result = fieldExtractor.process(metadata, targetObject);
        assertTrue(result.contains("Key1" + MAP_DELIMITER + "Value1\nKey2" + MAP_DELIMITER + "Value2") || result.contains("Key2" + MAP_DELIMITER + "Value2\nKey1" + MAP_DELIMITER + "Value1"), "Map field should be flattened correctly.");
    }

    @Test
    void testProcess_NestedObject() throws NoSuchFieldException {
        // Test a nested object with multiple levels of hierarchy
        ColumnMetadata metadata = createColumnMetadata(Arrays.asList(
                SomeClass.class.getDeclaredField("nestedField"),
                NestedClass.class.getDeclaredField("nestedValue")
        ));
        SomeClass targetObject = new SomeClass(new NestedClass("NestedValue"));

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("NestedValue", result, "Nested object should be traversed correctly.");
    }

    @Test
    void testProcess_NullFieldInHierarchy() throws NoSuchFieldException {
        // Test a null field in a hierarchy of objects
        ColumnMetadata metadata = createColumnMetadata(Arrays.asList(
                SomeClass.class.getDeclaredField("nestedField"),
                NestedClass.class.getDeclaredField("nestedValue")
        ));
        SomeClass targetObject = new SomeClass(Optional.ofNullable(null));

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("", result, "If any part of the hierarchy is null, it should return an empty string.");
    }

    @Test
    void testProcess_EmptyFieldList() {
        // Test when the list of parent fields is empty (no hierarchy)
        ColumnMetadata metadata = createColumnMetadata(Collections.emptyList());
        SomeClass targetObject = new SomeClass("SomeString");

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("", result, "If no field list is provided, the field should be empty.");
    }

    @Test
    void testProcess_StringFieldList() throws NoSuchFieldException {
        // Test when the list of parent fields is empty (no hierarchy)
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("nullField")));
        SomeClass targetObject = new SomeClass("SomeString");

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("SomeString", result, "If no field list is provided, the field should be returned directly.");
    }

    @Test
    void testProcess_PrimitiveFieldWithNullTargetObject() throws NoSuchFieldException {
        // Test when the target object is null, but the field exists in metadata
        ColumnMetadata metadata = createColumnMetadata(Collections.singletonList(SomeClass.class.getDeclaredField("primitiveField")));
        SomeClass targetObject = null;

        String result = fieldExtractor.process(metadata, targetObject);
        assertEquals("", result, "When the target object is null, the result should be an empty string.");
    }

    // Inner classes to simulate real-world objects
    static class SomeClass {
        private int primitiveField;
        private Integer wrapperField;
        private String nullField;
        private List<String> listField;
        private Map<String, String> mapField;
        private NestedClass nestedField;

        public SomeClass() {
        }

        public SomeClass(Object fieldValue) {
            if (fieldValue instanceof List) {
                this.listField = (List<String>) fieldValue;
            } else if (fieldValue instanceof Map) {
                this.mapField = (Map<String, String>) fieldValue;
            } else if (fieldValue instanceof NestedClass) {
                this.nestedField = (NestedClass) fieldValue;
            }
        }

        public SomeClass(int primitiveField) {
            this.primitiveField = primitiveField;
        }

        public SomeClass(Integer wrapperField) {
            this.wrapperField = wrapperField;
        }

        public SomeClass(String nullField) {
            this.nullField = nullField;
        }

        public SomeClass(List<String> listField) {
            this.listField = listField;
        }

        public SomeClass(Map<String, String> mapField) {
            this.mapField = mapField;
        }

        public SomeClass(NestedClass nestedField) {
            this.nestedField = nestedField;
        }

        public NestedClass getNestedField() {
            return nestedField;
        }
    }

    static class NestedClass {
        private String nestedValue;

        public NestedClass(String nestedValue) {
            this.nestedValue = nestedValue;
        }

        public String getNestedValue() {
            return nestedValue;
        }
    }
}

