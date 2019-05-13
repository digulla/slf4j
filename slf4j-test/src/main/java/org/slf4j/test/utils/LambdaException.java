package org.slf4j.test.utils;

public class LambdaException extends RuntimeException {
    private static final long serialVersionUID = -5283056772010836220L;

    private final boolean hasMessage;
    
    public LambdaException(String message, Exception cause) {
        super(message, cause);
        this.hasMessage = true;
    }
    
    public LambdaException(Exception cause) {
        super(cause);
        this.hasMessage = false;
    }
    
    @Override
    public String getMessage() {
        return hasMessage ? super.getMessage() : null;
    }
}
