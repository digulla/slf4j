package org.slf4j.test.utils;

public class FieldAccessException extends RuntimeException {
    private static final long serialVersionUID = 25742338122561580L;

    public FieldAccessException(Object instance,  String name, Throwable cause) {
        super("Unable to access field [" + name + "] of " + instance, cause);
    }
}
