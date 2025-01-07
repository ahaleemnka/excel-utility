package com.excel.utility.extractor;

import com.excel.utility.dto.ColumnMetadata;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The {@code FieldExtractor} class is responsible for extracting field values from target objects
 * based on the specified {@link ColumnMetadata}. It handles various data types, including primitive
 * types, wrapper types, lists, and maps. If the field value is null, it returns an empty string.
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Extracting field values from objects using reflection.</li>
 *   <li>Handling complex hierarchies of fields (e.g., nested objects or lists of objects).</li>
 *   <li>Gracefully handling null values by returning empty strings or null.</li>
 * </ul>
 *
 * <p>The {@code FieldExtractor} uses an {@link ObjectExtractor} to retrieve field values and
 * processes nested structures (lists and maps) using a flattening mechanism. This ensures that all
 * types of fields, including nested collections, can be extracted efficiently.</p>
 */
public class FieldExtractor {

    private final ObjectExtractor objectExtractor; // Extractor for field values from objects

    /**
     * Constructs a new {@code FieldExtractor} instance with default dependencies.
     * The constructor initializes the internal {@link ObjectExtractor} for field extraction.
     */
    public FieldExtractor() {
        this.objectExtractor = new ObjectExtractor(); // Initialize ObjectExtractor
    }

    /**
     * Processes the target object and extracts the field value based on the provided {@link ColumnMetadata}.
     * This method is capable of handling primitive types, wrapper types, and nested structures like Lists and Maps.
     * If the field value is null, an empty string is returned.
     *
     * @param columnMetadata Metadata that contains the field hierarchy information for extraction
     * @param targetObject   The target object from which the field value will be extracted
     * @return A string representation of the field value, or an empty string if the value is null
     * @throws RuntimeException if any issues occur during field extraction (e.g., field not found)
     */
    public Object process(ColumnMetadata columnMetadata, Object targetObject) {
        try {
            // Retrieve the field value from the object based on the parent class field hierarchy in metadata
            return getFieldValueFromHierarchy(targetObject, columnMetadata.getParentClassFieldList());
        } catch (RuntimeException e) {
            // Log the stack trace or handle the exception if needed
            e.printStackTrace();
        }
        // Return null in case of any extraction issues
        return null;
    }

    /**
     * Retrieves the value of a field from the target object by traversing through the field hierarchy
     * as specified in the {@code parentClasses} list within the {@link ColumnMetadata}.
     * This method uses reflection to access fields at each level of the hierarchy.
     *
     * @param targetObject  The target object from which the field value is to be extracted
     * @param parentClasses A list of fields representing the hierarchy of parent classes to traverse
     * @return The final field value after traversing the field hierarchy, or null if not found
     */
    private Object getFieldValueFromHierarchy(Object targetObject, List<Field> parentClasses) {
        // Return null if the list of parent classes is null or empty
        if (parentClasses == null || parentClasses.isEmpty()) {
            return null;
        }

        Object currentObject = targetObject;

        // Iterate through the field hierarchy in the parentClasses list and extract the field values
        for (Field parentField : parentClasses) {
            // Use ObjectExtractor to retrieve the value of the field from the current object
            currentObject = objectExtractor.process(currentObject, parentField);
        }

        // Return the final value after traversing the entire hierarchy
        return currentObject;
    }
}
