package com.excel.utility.util;

import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.exception.DuplicateColumnOrderException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.excel.utility.Config.MAX_COLUMN_ORDER;

/**
 * Utility class responsible for validating column metadata.
 * It validates whether column orders are valid (greater than 0 and within a specified range),
 * ensures there are no duplicate column orders, and checks that metadata is not null.
 */
public class ValidationUtils {

    /**
     * Validates a list of ColumnMetadata objects to ensure column orders are valid,
     * non-duplicate, and within an acceptable range.
     *
     * @param metadataList the list of ColumnMetadata objects to validate.
     * @throws IllegalArgumentException if the metadata is null, column order is invalid, or column orders are duplicate.
     */
    public static void validateMetadata(List<ColumnMetadata> metadataList) {
        // If the metadata list is null or empty, return early (nothing to validate).
        if (metadataList == null || metadataList.isEmpty()) {
            return;
        }

        // Set to track unique column orders and ensure no duplicates.
        Set<Integer> uniqueOrders = new HashSet<>();

        // Iterate through the list of metadata to validate column orders.
        for (ColumnMetadata metadata : metadataList) {
            // Validate the column order
            validateColumnOrder(metadata.getColumnOrder());

            // Check for duplicate column orders.
            checkForDuplicateColumnOrder(metadata.getColumnOrder(), uniqueOrders);
        }
    }

    /**
     * Validates that the column order is greater than 0 and does not exceed the maximum allowed limit.
     *
     * @param columnOrder the column order to validate.
     * @throws IllegalArgumentException if the column order is less than or equal to 0 or exceeds the maximum allowed value.
     */
    private static void validateColumnOrder(int columnOrder) {
        // Column order must be greater than 0.
        if (columnOrder <= 0) {
            throw new IllegalArgumentException("Invalid column order: " + columnOrder + ". Column order must be greater than 0.");
        }

        // Column order must not exceed the maximum limit.
        if (columnOrder > MAX_COLUMN_ORDER) {
            throw new IllegalArgumentException("Invalid column order: " + columnOrder + ". Column order cannot exceed " + MAX_COLUMN_ORDER + ".");
        }
    }

    /**
     * Checks if the given column order is unique within the set of already processed column orders.
     *
     * @param columnOrder  the column order to check.
     * @param uniqueOrders the set containing the already processed column orders.
     * @throws DuplicateColumnOrderException if the column order is a duplicate.
     */
    private static void checkForDuplicateColumnOrder(int columnOrder, Set<Integer> uniqueOrders) {
        // Attempt to add the column order to the set.
        // If the add operation returns false, it means the column order already exists (duplicate).
        if (!uniqueOrders.add(columnOrder)) {
            // Throw an exception if a duplicate column order is found.
            throw new DuplicateColumnOrderException(
                    "Duplicate column order found: " + columnOrder + " for header: " + columnOrder
            );
        }
    }
}
