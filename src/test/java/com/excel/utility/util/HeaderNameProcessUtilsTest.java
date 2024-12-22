package com.excel.utility.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderNameProcessUtilsTest {

    private final HeaderNameProcessUtils utils = new HeaderNameProcessUtils();

    @Test
    void testConvertHeader_WithAnnotationHeader() {
        String annotationHeader = "Custom Header";
        String parentHeader = null;
        String fieldName = "fieldName";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Custom Header", result, "Should return the annotation header when provided");
    }

    @Test
    void testConvertHeader_WithAnnotationAndParentHeader() {
        String annotationHeader = "Child Header";
        String parentHeader = "Parent Header";
        String fieldName = "fieldName";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Parent Header - Child Header", result, "Should concatenate parent and annotation headers");
    }

    @Test
    void testConvertHeader_WithNullAnnotationHeader() {
        String annotationHeader = null;
        String parentHeader = null;
        String fieldName = "fieldName";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Field Name", result, "Should convert the field name to a header when annotation header is null");
    }

    @Test
    void testConvertHeader_WithEmptyAnnotationHeader() {
        String annotationHeader = "";
        String parentHeader = "Parent Header";
        String fieldName = "myFieldName";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Parent Header - My Field Name", result, "Should use field name when annotation header is empty");
    }

    @Test
    void testConvertHeader_WithParentHeaderOnly() {
        String annotationHeader = "";
        String parentHeader = "Parent Header";
        String fieldName = "anotherField";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Parent Header - Another Field", result, "Should concatenate parent header and field name");
    }

    @Test
    void testConvertHeader_FieldNameWithCamelCase() {
        String fieldName = "camelCaseField";
        String result = utils.convertHeader(null, null, fieldName);

        assertEquals("Camel Case Field", result, "Should split camel case field names into words");
    }

    @Test
    void testConvertHeader_FieldNameWithUnderscores() {
        String fieldName = "field_name_with_underscores";
        String result = utils.convertHeader(null, null, fieldName);

        assertEquals("Field Name With Underscores", result, "Should replace underscores with spaces and capitalize words");
    }

    @Test
    void testConvertHeader_FieldNameWithNumbers() {
        String fieldName = "field1With2Numbers";
        String result = utils.convertHeader(null, null, fieldName);

        assertEquals("Field 1 With 2 Numbers", result, "Should split numbers and letters in the field name");
    }

    @Test
    void testConvertHeader_FieldNameWithSpecialCharacters() {
        String fieldName = "field-name_with-specialCharacters";
        String result = utils.convertHeader(null, null, fieldName);

        assertEquals("Field Name With Special Characters", result, "Should handle special characters correctly");
    }

    @Test
    void testConvertHeader_WithParentHeaderAndFieldNameOnly() {
        String annotationHeader = null;
        String parentHeader = "Parent";
        String fieldName = "childField";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Parent - Child Field", result, "Should combine parent header with formatted field name");
    }

    @Test
    void testConvertHeader_AllNullInputs() {
        String result = utils.convertHeader(null, null, null);

        assertEquals("", result, "Should return an empty string when all inputs are null");
    }

    @Test
    void testConvertHeader_FieldNameWithMultipleCapitalLetters() {
        String fieldName = "FieldWithMultipleCAPITALS";
        String result = utils.convertHeader(null, null, fieldName);

        assertEquals("Field With Multiple Capitals", result, "Should handle multiple consecutive capital letters correctly");
    }

    @Test
    void testConvertHeader_NullParentAndNonEmptyAnnotation() {
        String annotationHeader = "Custom Annotation";
        String parentHeader = null;
        String fieldName = "exampleField";

        String result = utils.convertHeader(annotationHeader, parentHeader, fieldName);

        assertEquals("Custom Annotation", result, "Should prioritize annotation header over field name");
    }
}
