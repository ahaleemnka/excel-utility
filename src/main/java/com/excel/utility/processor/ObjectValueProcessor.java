package com.excel.utility.processor;

import java.util.Collection;
import java.util.Map;

/**
 * The ObjectValueProcessor interface defines methods for processing object values into
 * string representations suitable for use in Excel cells. It provides functionality
 * for handling primitive values, collections, and maps.
 */
public interface ObjectValueProcessor {

    /**
     * Processes a given object and converts it into a string representation.
     * Handles null values, primitive types, user-defined objects, collections, and maps.
     *
     * @param fieldValue The object value to be processed.
     * @return A string representation of the object. For null values, it returns an empty string.
     */
    String process(Object fieldValue);

    /**
     * Flattens a Map into a string representation. The resulting string format
     * combines key-value pairs in a structured manner, making it suitable for
     * display in a single Excel cell.
     *
     * Example:
     * Input: {key1=value1, key2=value2}
     * Output: "key1: value1, key2: value2"
     *
     * @param map The map to be flattened.
     * @return A string representation of the map. Returns an empty string if the map is null or empty.
     */
    String flattenMap(Map<?, ?> map);

    /**
     * Flattens a Collection into a string representation. The resulting string format
     * combines elements of the collection in a comma-separated list for display in
     * a single Excel cell.
     *
     * Example:
     * Input: [value1, value2, value3]
     * Output: "value1, value2, value3"
     *
     * @param collection The collection to be flattened.
     * @return A string representation of the collection. Returns an empty string if the collection is null or empty.
     */
    String flattenCollection(Collection<?> collection);
}
