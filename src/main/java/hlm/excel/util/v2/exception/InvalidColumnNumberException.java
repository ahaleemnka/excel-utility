package hlm.excel.util.v2.exception;

public class InvalidColumnNumberException extends RuntimeException {
    public InvalidColumnNumberException(int columnOrder) {
        super("Invalid column number : " + columnOrder);
    }
}
