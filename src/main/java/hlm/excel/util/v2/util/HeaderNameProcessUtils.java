package hlm.excel.util.v2.util;

/**
 * Utility class responsible for converting field names into human-readable header names.
 * The class handles field names in different formats (e.g., camelCase, snake_case, etc.)
 * and applies necessary formatting to create a user-friendly header name.
 */
public class HeaderNameProcessUtils {

    /**
     * Converts a given field name into a header name based on the provided annotation,
     * parent header, and field name. This method combines the annotation-based header
     * with the parent header if available and applies formatting rules.
     *
     * @param annotationHeader The header name provided in the annotation, or null if not specified.
     * @param parentHeader     The parent header, used for nested fields (e.g., "Parent - Child").
     * @param fieldName        The field name to be converted into a header.
     * @return A properly formatted header name. If no valid header is provided, a default header is created.
     */
    public String convertHeader(String annotationHeader, String parentHeader, String fieldName) {
        // If the fieldName is null or empty, return an empty string as there's no valid header to generate.
        if (fieldName == null || fieldName.isEmpty()) {
            return "";
        }

        // Use the annotation header if provided, else convert the field name to a header format.
        String header = annotationHeader != null && !annotationHeader.isEmpty()
                ? annotationHeader
                : convertFieldNameToHeader(fieldName);

        // If a parent header is provided, append the field's header to the parent header with a separator.
        if (parentHeader != null && !parentHeader.isEmpty()) {
            return parentHeader + " - " + header;
        }

        // Otherwise, return just the field's header.
        return header;
    }

    /**
     * Converts a field name (in camelCase, snake_case, etc.) into a human-readable header format.
     * This method splits camelCase and snake_case names, capitalizes the first letter of each word,
     * and replaces underscores and dashes with spaces.
     *
     * @param fieldName The field name to be converted into a header format.
     * @return A human-readable header name derived from the field name.
     */
    private String convertFieldNameToHeader(String fieldName) {
        // Replace underscores and dashes with spaces for better readability.
        String header = fieldName
                .replaceAll("[-_]", " ") // Replace underscores and dashes with spaces
                .replaceAll("([a-z])([A-Z])", "$1 $2") // Split camelCase words
                .replaceAll("([0-9])([a-zA-Z])", "$1 $2") // Split numbers followed by letters
                .replaceAll("([a-zA-Z])([0-9])", "$1 $2") // Split letters followed by numbers
                .toLowerCase(); // Convert the entire header to lowercase for uniformity.

        // Capitalize the first letter of each word in the header.
        String[] words = header.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                // Capitalize the first letter of each word (e.g., "employee_name" -> "Employee Name")
                words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
            }
        }

        // Join the words back together with spaces to form the final header string.
        return String.join(" ", words);
    }
}
