package com.excel.utility.exception;

public class InvalidColumnNumberException extends RuntimeException {
    public InvalidColumnNumberException(int columnOrder) {
        super("Invalid column number : " + columnOrder);
    }
}
