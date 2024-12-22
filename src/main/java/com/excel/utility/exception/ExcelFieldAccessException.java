package com.excel.utility.exception;

public class ExcelFieldAccessException extends RuntimeException {
    public ExcelFieldAccessException(String message) {
        super(message);
    }

    public ExcelFieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
