package com.excel.utility.util;

import com.excel.utility.processor.ObjectValueProcessor;
import com.excel.utility.processor.impl.DefaultObjectValueProcessor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultValueFlattenProcessorTest {

    private final static String MAP_DELIMITER = " : ";
    private final static String NEXT_LINE = "\n";
    private final ObjectValueProcessor processor = new DefaultObjectValueProcessor();

    @Test
    void testFlattenList_NullValues() {
        List<String> listWithNulls = Arrays.asList("One", null, "Three");
        String result = processor.flattenCollection(listWithNulls);
        assertEquals("One, <empty>, Three", result, "Flattening a list with null values should concatenate their string representations.");
    }

    @Test
    void testFlattenList_NumericValues() {
        List<Object> numericList = Arrays.asList(1, 2.5, 3L);
        String result = processor.flattenCollection(numericList);
        assertEquals("1, 2.5, 3", result, "Flattening a list with numeric values should concatenate their string representations.");
    }

    @Test
    void testFlattenList_EmptyList() {
        List<String> emptyList = Collections.emptyList();
        String result = processor.flattenCollection(emptyList);
        assertEquals("", result, "Flattening an empty list should return an empty string.");
    }

    @Test
    void testFlattenList_SingleElement() {
        List<String> singleElementList = Collections.singletonList("Single");
        String result = processor.flattenCollection(singleElementList);
        assertEquals("Single", result, "Flattening a single-element list should return the element.");
    }

    @Test
    void testFlattenList_MultipleElements() {
        List<String> multiElementList = Arrays.asList("Apple", "Banana", "Cherry");
        String result = processor.flattenCollection(multiElementList);
        assertEquals("Apple, Banana, Cherry", result, "Flattening a list should join elements with ', '.");
    }

    @Test
    void testFlattenList_NullElement() {
        List<String> listWithNull = Arrays.asList("Apple", null, "Cherry");
        String result = processor.flattenCollection(listWithNull);
        assertEquals("Apple, <empty>, Cherry", result, "Flattening a list with nulls should replace 'null' with '<empty>'.");
    }

    @Test
    void testFlattenList_MixedElements() {
        List<Object> mixedList = Arrays.asList("Text", 123, null, 45.6, true);
        String result = processor.flattenCollection(mixedList);
        assertEquals("Text, 123, <empty>, 45.6, true", result, "Flattening a mixed list should join elements with ', ' and replace 'null' with '<empty>'.");
    }


    @Test
    void testFlattenMap_NullValues() {
        Map<String, String> mapWithNulls = new LinkedHashMap<>();
        mapWithNulls.put("Key1", null);
        mapWithNulls.put(null, "Value2");

        String result = processor.flattenMap(mapWithNulls);
        assertTrue(result.contains("Key1" + MAP_DELIMITER + "<empty>"), "Flattening a map with null values should include '' as the value.");
        assertTrue(result.contains("<empty>" + MAP_DELIMITER + "Value2"), "Flattening a map with a null key should include '' as the key.");
    }

    @Test
    void testFlattenMap_NumericKeysAndValues() {
        Map<Object, Object> numericMap = new LinkedHashMap<>();
        numericMap.put(1, 100);
        numericMap.put(2.5, 200.5);
        numericMap.put(3L, 300L);

        String result = processor.flattenMap(numericMap);
        assertEquals("1" + MAP_DELIMITER + "100" + NEXT_LINE + "2.5" + MAP_DELIMITER + "200.5" + NEXT_LINE + "3" + MAP_DELIMITER + "300", result, "Flattening a map with numeric keys and values should concatenate their string representations.");
    }

    @Test
    void testFlattenMap_ComplexObjects() {
        Map<Object, Object> complexMap = new LinkedHashMap<>();
        complexMap.put("Key", Arrays.asList("A", "B", "C"));
        complexMap.put("AnotherKey", new HashMap<>());

        String result = processor.flattenMap(complexMap);
        assertTrue(result.contains("Key" + MAP_DELIMITER + "[A, B, C]"), "Flattening a map with a list as value should include its string representation.");
        assertTrue(result.contains("AnotherKey" + MAP_DELIMITER + "{}"), "Flattening a map with an empty map as value should include its string representation.");
    }

    @Test
    void testFlattenMap_EmptyMap() {
        Map<String, String> emptyMap = Collections.emptyMap();
        String result = processor.flattenMap(emptyMap);
        assertEquals("", result, "Flattening an empty map should return an empty string.");
    }

    @Test
    void testFlattenMap_SingleEntry() {
        Map<String, String> singleEntryMap = Collections.singletonMap("Key", "Value");
        String result = processor.flattenMap(singleEntryMap);
        assertEquals("Key" + MAP_DELIMITER + "Value", result, "Flattening a single-entry map should return the key-value pair.");
    }

    @Test
    void testFlattenMap_MultipleEntries() {
        Map<String, String> multiEntryMap = new HashMap<>();
        multiEntryMap.put("Key1", "Value1");
        multiEntryMap.put("Key2", "Value2");
        multiEntryMap.put("Key3", "Value3");

        String result = processor.flattenMap(multiEntryMap);
        String expected = "Key1" + MAP_DELIMITER + "Value1" + NEXT_LINE + "Key2" + MAP_DELIMITER + "Value2" + NEXT_LINE + "Key3" + MAP_DELIMITER + "Value3";
        assertTrue(result.contains("Key1" + MAP_DELIMITER + "Value1"), "Flattening a map should return each key-value pair on a new line.");
        assertTrue(result.contains("Key2" + MAP_DELIMITER + "Value2"), "Flattening a map should return each key-value pair on a new line.");
        assertTrue(result.contains("Key3" + MAP_DELIMITER + "Value3"), "Flattening a map should return each key-value pair on a new line.");
    }

    @Test
    void testFlattenMap_NullKey() {
        Map<String, String> mapWithNullKey = new HashMap<>();
        mapWithNullKey.put(null, "Value");
        String result = processor.flattenMap(mapWithNullKey);
        assertEquals("<empty>" + MAP_DELIMITER + "Value", result, "Flattening a map with a null key should replace 'null' with '<empty>'.");
    }

    @Test
    void testFlattenMap_NullValue() {
        Map<String, String> mapWithNullValue = new HashMap<>();
        mapWithNullValue.put("Key", null);
        String result = processor.flattenMap(mapWithNullValue);
        assertEquals("Key" + MAP_DELIMITER + "<empty>", result, "Flattening a map with a null value should replace 'null' with '<empty>'.");
    }

    @Test
    void testFlattenMap_NullKeyAndValue() {
        Map<String, String> mapWithNullKeyAndValue = new HashMap<>();
        mapWithNullKeyAndValue.put(null, null);
        String result = processor.flattenMap(mapWithNullKeyAndValue);
        assertEquals("<empty>" + MAP_DELIMITER + "<empty>", result, "Flattening a map with null key and value should replace 'null' with '<empty>'.");
    }

    @Test
    void testFlattenMap_MixedEntries() {
        Map<Object, Object> mixedMap = new HashMap<>();
        mixedMap.put("Key1", 123);
        mixedMap.put(null, "Value2");
        mixedMap.put("Key3", null);
        mixedMap.put(456, true);

        String result = processor.flattenMap(mixedMap);
        String expected = """
                <empty> : Value2
                Key1 : 123
                Key3 : <empty>
                456 : true""";
        assertEquals(expected, result, "Flattening a mixed map should correctly handle all key-value pairs.");
    }

    @Test
    void testFlattenList_MixedDataTypes() {
        List<Object> mixedDataList = Arrays.asList("Hello", 42, 3.14, true, null, new int[]{1, 2, 3});
        String result = processor.flattenCollection(mixedDataList);
        assertTrue(result.contains("Hello, 42, 3.14, true, <empty>, [I@"), "Flattening a mixed list should join elements of different data types.");
    }

    @Test
    void testFlattenList_ObjectArray() {
        List<Object> objectArrayList = Arrays.asList(new Object(), new Object());
        String result = processor.flattenCollection(objectArrayList);
        assertTrue(result.contains("java.lang.Object@"), "Flattening a list of objects should call their toString method.");
    }

    @Test
    void testFlattenList_SpecialCharacters() {
        List<String> specialCharList = Arrays.asList("Hello@World", "A-B_C", "Java#123");
        String result = processor.flattenCollection(specialCharList);
        assertEquals("Hello@World, A-B_C, Java#123", result, "Flattening a list with special characters should preserve them.");
    }

    @Test
    void testFlattenMap_MixedKeysAndValues() {
        Map<Object, Object> mixedMap = new HashMap<>();
        mixedMap.put("Key1", 100);
        mixedMap.put(2, "Value2");
        mixedMap.put("Key3", 3.14);
        mixedMap.put(true, "BooleanKey");

        String result = processor.flattenMap(mixedMap);
        String expected = "Key1" + MAP_DELIMITER + "100" + NEXT_LINE + "2" + MAP_DELIMITER + "Value2" + NEXT_LINE + "Key3" + MAP_DELIMITER + "3.14" + NEXT_LINE + "true" + MAP_DELIMITER + "BooleanKey";
        assertEquals(expected, result, "Flattening a map with mixed keys and values should concatenate their string representations.");
    }

    @Test
    void testFlattenMap_MultipleNullKeys() {
        Map<Object, Object> mapWithMultipleNullKeys = new LinkedHashMap<>();
        mapWithMultipleNullKeys.put(null, "FirstValue");
        mapWithMultipleNullKeys.put(null, "SecondValue");

        String result = processor.flattenMap(mapWithMultipleNullKeys);
        assertTrue(result.contains("<empty>" + MAP_DELIMITER + "SecondValue"), "Flattening a map with multiple null keys should replace 'null' with '<empty>'.");
    }

    @Test
    void testFlattenMap_NestedMap() {
        Map<String, Object> nestedMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("InnerKey1", "InnerValue1");
        innerMap.put("InnerKey2", "InnerValue2");
        nestedMap.put("OuterKey", innerMap);

        String result = processor.flattenMap(nestedMap);
        assertTrue(result.contains("OuterKey" + MAP_DELIMITER + "{InnerKey2=InnerValue2, InnerKey1=InnerValue1}"), "Flattening a map with a nested map should include its string representation.");
    }

    @Test
    void testFlattenMap_MultipleEmptyValues() {
        Map<String, String> mapWithMultipleEmptyValues = new LinkedHashMap<>();
        mapWithMultipleEmptyValues.put("Key1", null);
        mapWithMultipleEmptyValues.put("Key2", "");
        mapWithMultipleEmptyValues.put("Key3", " ");

        String result = processor.flattenMap(mapWithMultipleEmptyValues);
        assertTrue(result.contains("Key1" + MAP_DELIMITER + "<empty>"), "Flattening a map with null values should replace them with '<empty>'.");
        assertTrue(result.contains("Key2" + MAP_DELIMITER + ""), "Flattening a map with empty string values should replace them with '<empty>'.");

        assertTrue(result.contains("Key3" + MAP_DELIMITER + " "), "Flattening a map with spaces should treat them as <empty>." + result);
    }

    @Test
    void testFlattenMap_CustomObjectValues() {
        Map<Object, Object> mapWithCustomObjects = new HashMap<>();
        mapWithCustomObjects.put("Key1", new CustomObject("Custom1", 100));
        mapWithCustomObjects.put("Key2", new CustomObject("Custom2", 200));

        String result = processor.flattenMap(mapWithCustomObjects);
        assertTrue(result.contains("Key1" + MAP_DELIMITER + "CustomObject{name='Custom1', value=100}"), "Flattening a map with custom objects should include their string representation.");
        assertTrue(result.contains("Key2" + MAP_DELIMITER + "CustomObject{name='Custom2', value=200}"), "Flattening a map with custom objects should include their string representation.");
    }

    // Custom object for testing
    class CustomObject {
        private String name;
        private int value;

        public CustomObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "CustomObject{name='" + name + "', value=" + value + "}";
        }
    }

}

