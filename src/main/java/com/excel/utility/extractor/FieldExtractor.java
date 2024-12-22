package com.excel.utility.extractor;

import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.util.ClassTypeUtils;
import com.excel.utility.util.ValueFlattenProcessor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The FieldExtractor class processes a target object and extracts a field value from the object
 * based on the provided ColumnMetadata. It handles various field types including primitive,
 * wrapper, List, and Map types. If the field value is null, an empty string is returned.
 * <p>
 * This class uses ObjectValueExtractor to retrieve field values from the object and ValueFlattenProcessor
 * to process nested List and Map types.
 */
public class FieldExtractor {

    private final ObjectValueExtractor objectValueExtractor; // Extracts field values from objects
    private final ValueFlattenProcessor valueFlattenProcessor; // Processes lists and maps to flatten them

    /**
     * Constructs a FieldExtractor instance with default dependencies.
     */
    public FieldExtractor() {
        this.objectValueExtractor = new ObjectValueExtractor(); // Initialize ObjectValueExtractor
        this.valueFlattenProcessor = new ValueFlattenProcessor(); // Initialize ValueFlattenProcessor
    }

    /**
     * Processes the target object and extracts the field value based on the ColumnMetadata provided.
     * This method handles primitive types, wrapper types, Lists, and Maps, and flattens them as needed.
     *
     * @param columnMetadata Metadata of the column to determine the field and hierarchy to extract
     * @param targetObject   The target object from which to extract the field value
     * @return A string representation of the field value or an empty string if the value is null
     */
    public String process(ColumnMetadata columnMetadata, Object targetObject) {
        try {
            // Retrieve the field value from the object based on the parent class field list in metadata
            Object fieldValue = getFieldValueFromHierarchy(targetObject, columnMetadata.getParentClassFieldList());

            // If the field value is null, return an empty string
            if (fieldValue == null) {
                return "";
            }
            // If the field value is a primitive or wrapper, return its string representation
            else if (ClassTypeUtils.isPrimitiveOrWrapper(fieldValue.getClass())) {
                return fieldValue.toString();
            }
            // If the field value is a List, flatten it using ValueFlattenProcessor
            else if (fieldValue instanceof List) {
                return valueFlattenProcessor.flattenList((List<?>) fieldValue);
            }
            // If the field value is a Map, flatten it using ValueFlattenProcessor
            else if (fieldValue instanceof Map) {
                return valueFlattenProcessor.flattenMap((Map<?, ?>) fieldValue);
            }
            // Otherwise, return the field value's string representation
            else {
                return fieldValue.toString();
            }
        } catch (RuntimeException e) {
            // Log or print the stack trace in case of an exception
            e.printStackTrace();
        }
        // Return an empty string in case of any exceptions
        return "";
    }

    /**
     * Retrieves the value of a field from the target object, traversing the field hierarchy
     * as specified in the parentClasses list.
     *
     * @param targetObject  The target object from which the field value is to be extracted
     * @param parentClasses A list of fields representing the hierarchy of parent classes to traverse
     * @return The field value at the end of the hierarchy, or null if not found
     */
    private Object getFieldValueFromHierarchy(Object targetObject, List<Field> parentClasses) {
        // Return null if the target object or parent classes list is null or empty
        if (targetObject == null || parentClasses == null || parentClasses.isEmpty()) {
            return null;
        }

        Object currentObject = targetObject;

        // Iterate through the parent class field list and traverse the field hierarchy
        for (Field parentField : parentClasses) {
            // If the current object is null, return null (field not found)
            if (currentObject == null) {
                return null;
            }
            // Use ObjectValueExtractor to extract the field value from the current object
            currentObject = objectValueExtractor.process(currentObject, parentField);
        }

        // Return the final field value after traversing the hierarchy
        return currentObject;
    }
}
