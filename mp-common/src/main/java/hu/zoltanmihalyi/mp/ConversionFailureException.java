package hu.zoltanmihalyi.mp;

public class ConversionFailureException extends Exception {
    public ConversionFailureException(Throwable cause) {
        super(cause);
    }

    public ConversionFailureException(String message) {
        super(message);
    }
}
