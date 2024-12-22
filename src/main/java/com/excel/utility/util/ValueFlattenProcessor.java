package com.excel.utility.util;

import java.util.List;
import java.util.Map;

/**
 * Utility class for flattening complex data structures (such as lists and maps) into a string representation.
 * The purpose of this class is to provide a standardized way of converting lists and maps to readable strings,
 * while handling null values gracefully by replacing them with a placeholder ("<empty>").
 */
public class ValueFlattenProcessor {

    // Constant for an empty value placeholder.
    public static final String EMPTY = "<empty>";

    // Delimiter to separate key-value pairs in maps.
    public static final String MAP_DELIMITER = ": ";

    // Delimiter to separate list elements.
    public static final String LIST_DELIMITER = ", ";

    // Line break for formatting map entries on new lines.
    public static final String LINE_BREAK = "\n";

    /**
     * Flattens a list into a string representation where each element is separated by a comma.
     * If the list is empty, an empty string is returned.
     * Null elements in the list are replaced with the placeholder "<empty>".
     *
     * @param list the list to be flattened.
     * @return a string representation of the list, with each element separated by ", ".
     */
    public String flattenList(List<?> list) {
        // Return empty string if the list is empty.
        if (list.isEmpty()) {
            return "";
        }

        // Convert the list into a string with elements separated by LIST_DELIMITER.
        return String.join(LIST_DELIMITER, list.stream()
                .map(element -> element == null ? EMPTY : element.toString()) // Replace nulls with "<empty>"
                .toArray(String[]::new)); // Convert the stream into an array and join it.
    }

    /**
     * Flattens a map into a string representation where each key-value pair is represented as
     * "key: value". If the map has multiple entries, each pair is separated by a line break.
     * Null keys and values are replaced with the placeholder "<empty>".
     *
     * @param map the map to be flattened.
     * @return a string representation of the map, with key-value pairs separated by ": ".
     */
    public String flattenMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();

        // Iterate through each entry in the map and build a string representation.
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            // If the string builder is not empty, append a line break before adding the next entry.
            if (!sb.isEmpty()) {
                sb.append(LINE_BREAK);
            }

            // Handle null key and value, replacing with "<empty>".
            String key = entry.getKey() == null ? EMPTY : entry.getKey().toString();
            String value = entry.getValue() == null ? EMPTY : entry.getValue().toString();

            // Append the key-value pair to the string builder.
            sb.append(key).append(MAP_DELIMITER).append(value);
        }

        return sb.toString();
    }
}
