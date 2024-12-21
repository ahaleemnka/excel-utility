package hlm.excel.util.v2.exception;

public class ExcelFieldAccessException extends RuntimeException {
    public ExcelFieldAccessException(String message) {
        super(message);
    }

    public ExcelFieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
