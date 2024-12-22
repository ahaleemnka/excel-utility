package com.excel.utility.extractor;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.util.ClassTypeUtils;
import com.excel.utility.util.HeaderNameProcessUtils;
import com.excel.utility.util.ValidationUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for extracting metadata related to Excel columns from a given POJO (Plain Old Java Object) class.
 * It processes the annotations on fields (such as @ExcelColumn) and builds a list of ColumnMetadata objects, which hold information
 * about the column order, header names, field types, and the hierarchy of fields for the given class.
 * <p>
 * The `process` method is the entry point, which triggers the extraction process and validation of the metadata.
 * The `extractColumnMetadata` method recursively traverses the fields of a class to gather column-related metadata.
 * It also ensures that there are no circular references during the metadata extraction and that missing column orders are assigned sequentially.
 * <p>
 * This class relies on annotations (`@ExcelMapper`, `@ExcelColumn`) to extract necessary metadata. Additionally, it utilizes a utility
 * class (`HeaderNameProcessUtils`) to compute the appropriate header names for each column.
 */
public class ColumnMetadataExtractor {

    /**
     * A set used to track the classes that have been visited during the metadata extraction process.
     * This is to avoid circular references when dealing with nested objects or hierarchical POJOs.
     */
    private final Set<Class<?>> visited = new HashSet<>();

    /**
     * A utility class used to process and generate the header names based on the column annotations.
     * It combines the parent header and the field's name to generate the final header name.
     */
    private final HeaderNameProcessUtils headerNameProcessUtils;

    /**
     * Default constructor initializing the HeaderNameProcessUtils instance.
     */
    public ColumnMetadataExtractor() {
        headerNameProcessUtils = new HeaderNameProcessUtils();
    }

    /**
     * Processes the given POJO class and extracts the column metadata.
     *
     * @param clazz The class to process for column metadata extraction.
     * @return A list of ColumnMetadata objects that contain metadata about each column.
     * @throws IllegalArgumentException if the class has circular references or is missing the @ExcelMapper annotation.
     */
    public List<ColumnMetadata> process(Class<?> clazz) {
        List<ColumnMetadata> metadataList = new ArrayList<>();

        // Start the metadata extraction from the class and traverse its fields recursively.
        extractColumnMetadata(clazz, null, metadataList, new ArrayList<>());

        // Assign missing column orders if they are not already set.
        assignMissingColumnOrders(metadataList);

        // Validate the metadata for any potential issues.
        ValidationUtils.validateMetadata(metadataList);

        return metadataList;
    }

    /**
     * Recursively extracts column metadata from the given class and its fields.
     * This method handles nested classes and ensures proper column order and header name generation.
     *
     * @param clazz           The class to process.
     * @param parentHeader    The header name of the parent class.
     * @param metadataList    The list that will hold the extracted column metadata.
     * @param parentClassList A list of parent fields to keep track of the field hierarchy.
     * @throws IllegalArgumentException if the class is missing the @ExcelMapper annotation or contains circular references.
     */
    protected void extractColumnMetadata(Class<?> clazz, String parentHeader, List<ColumnMetadata> metadataList, ArrayList<Field> parentClassList) {
        // Avoid circular references by tracking visited classes.
        if (visited.contains(clazz)) {
            throw new IllegalArgumentException("Circular reference detected in class: " + clazz.getName());
        }
        visited.add(clazz);

        // Ensure the class has the required @ExcelMapper annotation.
        ExcelMapper sheetAnnotation = clazz.getAnnotation(ExcelMapper.class);
        if (sheetAnnotation == null) {
            throw new IllegalArgumentException("POJO class must have an @ExcelMapper annotation." + clazz.getName());
        }

        // Process each declared field in the class.
        for (Field field : clazz.getDeclaredFields()) {
            List<Field> fieldList = new ArrayList<>(parentClassList);
            fieldList.add(field);
            field.setAccessible(true);

            // If the field has the @ExcelColumn annotation, process it.
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);

                // Generate the computed header name based on the annotation and parent header.
                String computedHeader = headerNameProcessUtils.convertHeader(annotation.header(), parentHeader, field.getName());
                int columnOrder = annotation.columnOrder();

                // If the field type is not a primitive or wrapper, recursively extract metadata from the field's type.
                if (!ClassTypeUtils.isPrimitiveOrWrapper(field.getType())) {
                    extractColumnMetadata(field.getType(), computedHeader, metadataList, new ArrayList<>(fieldList));
                } else {
                    // Create ColumnMetadata for primitive or wrapper types and add to the metadata list.
                    ColumnMetadata columnMetadata = new ColumnMetadata(
                            field,
                            computedHeader,
                            columnOrder,
                            field.getType(),
                            Collections.unmodifiableList(fieldList)
                    );

                    metadataList.add(columnMetadata);
                }
            }
        }

        // After processing all fields in the class, remove the class from the visited set.
        visited.remove(clazz);
    }

    /**
     * Assigns column orders to fields that have a missing or invalid column order.
     * If a column order is not provided or is less than or equal to 0, it assigns the next available column order.
     *
     * @param metadataList The list of ColumnMetadata objects to which column orders will be assigned.
     */
    protected void assignMissingColumnOrders(List<ColumnMetadata> metadataList) {
        if (Objects.isNull(metadataList)) {
            return;
        }

        // Set to keep track of already assigned column orders.
        int nextAvailableOrder = 1;
        Set<Integer> takenColumnOrderSet = metadataList.stream().map(ColumnMetadata::getColumnOrder).collect(Collectors.toSet());

        // Iterate over the metadata and assign missing column orders.
        for (ColumnMetadata metadata : metadataList) {
            // If the column order is less than or equal to 0, assign a new order.
            if (metadata.getColumnOrder() <= 0) {
                while (takenColumnOrderSet.contains(nextAvailableOrder)) {
                    nextAvailableOrder++;
                }
                metadata.setColumnOrder(nextAvailableOrder++);
            }
        }
    }
}
