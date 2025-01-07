package com.excel.utility.processor.impl;

import com.excel.utility.processor.HeaderNameProcessor;

/**
 * The DefaultHeaderNameProcessor class implements the HeaderNameProcessor interface to
 * generate human-readable header names for Excel columns. It supports combining annotation
 * headers, parent headers, and field names into a structured format while ensuring
 * readability and uniformity.
 */
public class DefaultHeaderNameProcessor implements HeaderNameProcessor {

    /**
     * Converts a given field name into a header name by applying annotation headers,
     * parent headers, and field name formatting. Ensures nested fields are represented
     * hierarchically.
     *
     * @param annotationHeader The header name provided in the annotation, or null if not specified.
     * @param parentHeader     The parent header, used for nested fields (e.g., "Parent - Child").
     * @param fieldName        The field name to be converted into a header.
     * @return A properly formatted and human-readable header name. Returns an empty string if no valid input is provided.
     */
    @Override
    public String convertHeader(String annotationHeader, String parentHeader, String fieldName) {
        // Validate the fieldName; return an empty string if it's null or empty.
        if (fieldName == null || fieldName.isEmpty()) {
            return "";
        }

        // Use annotationHeader if present; otherwise, convert the fieldName to a human-readable header.
        String header = annotationHeader != null && !annotationHeader.isEmpty()
                ? annotationHeader
                : convertFieldNameToHeader(fieldName);

        // Combine with parentHeader if provided, using a hierarchical separator.
        if (parentHeader != null && !parentHeader.isEmpty()) {
            return parentHeader + " - " + header;
        }

        // Return the standalone header if no parentHeader is available.
        return header;
    }

    /**
     * Converts a field name into a human-readable header by splitting and formatting based on
     * camelCase, snake_case, or other naming conventions.
     *
     * @param fieldName The field name to be converted.
     * @return A human-readable header string with capitalization applied to each word.
     */
    private String convertFieldNameToHeader(String fieldName) {
        // Replace underscores/dashes with spaces, split camelCase, and separate numbers from letters.
        String header = fieldName
                .replaceAll("[-_]", " ") // Replace underscores and dashes with spaces.
                .replaceAll("([a-z])([A-Z])", "$1 $2") // Split camelCase words.
                .replaceAll("([0-9])([a-zA-Z])", "$1 $2") // Split numbers followed by letters.
                .replaceAll("([a-zA-Z])([0-9])", "$1 $2") // Split letters followed by numbers.
                .toLowerCase(); // Convert to lowercase for consistent formatting.

        // Capitalize the first letter of each word for readability.
        String[] words = header.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
            }
        }

        // Join the words into a single string separated by spaces.
        return String.join(" ", words);
    }
}
