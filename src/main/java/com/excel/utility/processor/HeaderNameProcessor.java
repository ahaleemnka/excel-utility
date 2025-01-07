package com.excel.utility.processor;

/**
 * The HeaderNameProcessor interface defines a contract for processing and generating
 * header names for Excel columns. It provides flexibility in constructing column headers
 * based on the combination of annotation headers, parent headers, and field names.
 */
public interface HeaderNameProcessor {

    /**
     * Generates a header name for an Excel column.
     *
     * @param annotationHeader the header name specified in the annotation. This can be null or empty if not provided.
     * @param parentHeader     the header name of the parent object, used for nested object structures.
     *                         This can also be null or empty if the field is not nested.
     * @param fieldName        the name of the field in the POJO, acting as a fallback for header generation.
     * @return the processed header name as a string, combining the provided inputs based on the implementation logic.
     */
    String convertHeader(String annotationHeader, String parentHeader, String fieldName);
}
