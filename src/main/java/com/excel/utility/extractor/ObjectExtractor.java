package com.excel.utility.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The ObjectExtractor class provides functionality to extract the value of a field
 * from an object using reflection. It attempts to retrieve the value using various methods
 * such as getter methods, `is` methods for boolean fields, and direct field access.
 */
public class ObjectExtractor {

    /**
     * Processes the object and retrieves the value of the specified field using reflection.
     * It first tries to access the field using the getter method, then tries the `is` method,
     * and finally, it attempts to directly access the field.
     *
     * @param object the object from which the field value is to be extracted
     * @param field  the field to extract the value from
     * @return the value of the field, or null if the field is not accessible
     */
    public Object process(Object object, Field field) {
        if (field == null) {
            throw new NullPointerException("hlm.excel.util.extractor.ObjectExtractor.process() Field is null");
        }
        Object fieldValue = null;
        String fieldName = field.getName();
        try {
            // Try to get the value using the getter method
            fieldValue = findGetterMethod(fieldName, object);

            // If getter method is not available, try to find the 'is' method (for boolean fields)
            if (fieldValue == null) {
                fieldValue = findIsMethod(fieldName, object);
            }

            if (object == null) {
                return null;
            }

            // If neither getter nor 'is' method is found, access the field directly
            if (fieldValue == null) {
                field.setAccessible(true);
                fieldValue = field.get(object);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // Log the exception stack trace for debugging purposes
        }
        return fieldValue; // Return the extracted value (or null if it couldn't be retrieved)
    }

    /**
     * Attempts to find the getter method for the given field name in the object's class.
     * The method name is expected to follow the "get<FieldName>" convention (e.g., getEmployeeId).
     *
     * @param fieldName the name of the field (e.g., "employeeId")
     * @param object    the object whose class is being examined
     * @return the result of invoking the getter method, or null if the method is not found
     */
    private Object findGetterMethod(String fieldName, Object object) {
        // Generate the getter method name by capitalizing the first letter of the field name
        return callMethod("get" + capitalizeFirstLetter(fieldName), object);
    }

    /**
     * Attempts to find the 'is' method for the given field name in the object's class.
     * The 'is' method is typically used for boolean fields (e.g., isEmployeeActive).
     *
     * @param fieldName the name of the field (e.g., "employeeActive")
     * @param object    the object whose class is being examined
     * @return the result of invoking the 'is' method, or null if the method is not found
     */
    private Object findIsMethod(String fieldName, Object object) {
        // Generate the 'is' method name by capitalizing the first letter of the field name
        return callMethod("is" + capitalizeFirstLetter(fieldName), object);
    }

    /**
     * Attempts to invoke a method on the object with the specified method name.
     *
     * @param methodName the name of the method to invoke (e.g., "getEmployeeId")
     * @param object     the object whose method is being invoked
     * @return the result of invoking the method, or null if the method is not found or fails
     */
    private Object callMethod(String methodName, Object object) {
        try {
            // Retrieve the method by name and invoke it
            Method getterMethod = object.getClass().getMethod(methodName);
            getterMethod.setAccessible(true);
            return getterMethod.invoke(object); // Return the result of invoking the method
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Capitalizes the first letter of a string for generating the method.
     *
     * @param str the input string
     * @return the input string with the first letter capitalized, or the same string if it is empty or null
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return the string as-is if it's null or empty
        }
        // Capitalize the first letter and return the rest of the string unchanged
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}