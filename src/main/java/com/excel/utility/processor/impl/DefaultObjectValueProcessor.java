package com.excel.utility.processor.impl;

import com.excel.utility.Config;
import com.excel.utility.processor.ObjectValueProcessor;
import com.excel.utility.util.ClassTypeUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of the ObjectValueProcessor interface.
 * This class provides methods for converting various object types
 * (e.g., primitives, collections, maps) into string representations.
 */
public class DefaultObjectValueProcessor implements ObjectValueProcessor {

    // Placeholder for empty values.
    private final String empty;

    // Delimiter to separate key-value pairs in maps.
    private final String mapDelimiter;

    // Delimiter to separate elements in collections.
    private final String listDelimiter;

    /**
     * Default constructor using configuration values for delimiters and empty placeholders.
     */
    public DefaultObjectValueProcessor() {
        this.mapDelimiter = Config.MAP_DELIMITER;
        this.listDelimiter = Config.LIST_DELIMITER;
        this.empty = Config.EMPTY;
    }

    /**
     * Constructor allowing custom delimiters and placeholder values.
     *
     * @param mapDelimiter  Delimiter for key-value pairs in maps.
     * @param listDelimiter Delimiter for elements in collections.
     * @param empty         Placeholder for null or empty values.
     */
    public DefaultObjectValueProcessor(String mapDelimiter, String listDelimiter, String empty) {
        this.mapDelimiter = mapDelimiter;
        this.listDelimiter = listDelimiter;
        this.empty = empty;
    }

    /**
     * Processes an object into a string representation.
     * Handles nulls, primitives, collections, and maps.
     *
     * @param fieldValue The object to process.
     * @return A string representation of the object.
     */
    @Override
    public String process(Object fieldValue) {
        if (fieldValue == null) {
            return "";
        } else if (ClassTypeUtils.isPrimitiveOrWrapper(fieldValue.getClass())) {
            return fieldValue.toString();
        } else if (fieldValue instanceof Collection) {
            return flattenCollection((Collection<?>) fieldValue);
        } else if (fieldValue instanceof Map) {
            return flattenMap((Map<?, ?>) fieldValue);
        } else {
            return fieldValue.toString();
        }
    }

    /**
     * Flattens a map into a string representation.
     * Each entry is formatted as "key: value" with entries separated by line breaks.
     * Null keys and values are replaced with the placeholder value.
     *
     * @param map The map to flatten.
     * @return A string representation of the map.
     */
    @Override
    public String flattenMap(Map<?, ?> map) {
        String lineBreak = "\n";
        return map.entrySet()
                .stream()
                .map(entry -> (entry.getKey() == null ? empty : entry.getKey().toString())
                        + mapDelimiter +
                        (entry.getValue() == null ? empty : entry.getValue().toString()))
                .collect(Collectors.joining(lineBreak));
    }

    /**
     * Flattens a collection into a string representation.
     * Each element is separated by the list delimiter.
     * Null elements are replaced with the placeholder value.
     *
     * @param collection The collection to flatten.
     * @return A string representation of the collection.
     */
    @Override
    public String flattenCollection(Collection<?> collection) {
        return collection.stream()
                .map(element -> element == null ? empty : element.toString())
                .collect(Collectors.joining(listDelimiter));
    }
}
