package com.excel.utility.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ClassTypeUtils {
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||                       // Primitive types (int, boolean, etc.)
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Short.class ||
                clazz == Byte.class ||
                clazz == String.class ||                     // String
                Number.class.isAssignableFrom(clazz) ||      // Wrapper types for numbers (Integer, Double, etc.)
                clazz == Boolean.class ||                    // Boolean wrapper type
                clazz == LocalDateTime.class ||              // Java 8 LocalDateTime
                clazz == LocalDate.class ||                 // Java 8 LocalDate
                clazz == LocalTime.class ||                 // Java 8 LocalTime
                clazz == Date.class ||                      // java.util.Date
                clazz == Instant.class ||                   // Java 8 Instant
                clazz == BigDecimal.class ||                // BigDecimal for precise calculations
                clazz == BigInteger.class ||                // BigInteger for large integers
                clazz == Currency.class ||                  // Currency type
                clazz == UUID.class ||                      // UUID type
                clazz == Character.class ||                 // Character wrapper type
                clazz.isEnum();
    }

    public static boolean isListSetMap(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz) ||
                Set.class.isAssignableFrom(clazz) ||
                Map.class.isAssignableFrom(clazz) ||
                Queue.class.isAssignableFrom(clazz) ||
                Deque.class.isAssignableFrom(clazz) ||
                SortedSet.class.isAssignableFrom(clazz) ||
                SortedMap.class.isAssignableFrom(clazz) ||
                NavigableSet.class.isAssignableFrom(clazz) ||
                NavigableMap.class.isAssignableFrom(clazz);
    }

}
