package bookingApp.exception;

public class MethodNotAllowedException extends AppException {

    private final String allowedMethods;

    public MethodNotAllowedException(String message, String methods) {
        super(message);
        this.allowedMethods = methods;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }
}
