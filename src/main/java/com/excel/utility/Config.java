package com.excel.utility;

/**
 * The {@code Config} interface defines constants that provide configuration values for the Excel utility
 * used in processing and mapping Java objects to Excel sheets.
 *
 * <p>This interface centralizes key configuration settings related to row and column indexing,
 * delimiters for data separation, and other constants that assist in handling Excel sheet structure and
 * formatting.</p>
 *
 * <p>Key configurations include:</p>
 * <ul>
 *   <li>Starting row for data and header placement.</li>
 *   <li>Maximum column order to define a sensible upper limit.</li>
 *   <li>Delimiters used for separating mapped data in lists and maps.</li>
 *   <li>Special value to represent empty or null fields.</li>
 * </ul>
 */
public interface Config {

    /**
     * Row number to start inserting data in the Excel sheet.
     * <p>This is the initial row where data rows will begin to be populated.</p>
     */
    int ROW_VALUE_START_FOR_DATA = 1;

    /**
     * Row number where headers are to be inserted in the Excel sheet.
     * <p>This is the row where the column headers will be placed (usually row 0).</p>
     */
    int ROW_VALUE_FOR_HEADER = 0;

    /**
     * Defines a reasonable upper limit for column orders.
     * <p>This prevents exceeding the maximum allowed column limit and ensures that column indexes do not become too large.</p>
     * <p>The maximum column order is set to 1000.</p>
     */
    int MAX_COLUMN_ORDER = 1000;

    /**
     * Delimiter used to separate key-value pairs in map-like data structures.
     * <p>This delimiter is used when mapping the values in a map or similar data types.</p>
     */
    String MAP_DELIMITER = " : ";

    /**
     * Delimiter used to separate values in list-like data structures.
     * <p>This delimiter is used when mapping values in a list or collection type.</p>
     */
    String LIST_DELIMITER = ", ";

    /**
     * A placeholder string representing an empty or null value in the data.
     * <p>This value is used to represent fields that have no data or are explicitly set to be empty.</p>
     */
    String EMPTY = "<empty>";

    /**
     * The default sheet name used when no name is provided.
     * <p>If no sheet name is specified, the default name "Sheet" will be used.</p>
     */
    String DEFAULT_SHEET_NAME = "Sheet";
}
