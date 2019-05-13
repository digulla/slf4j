package org.slf4j.test.utils;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

    @Override
    default T get() {
        try {
            return getThrows();
        } catch(Exception e) {
            throw new LambdaException(e);
        }
    }
    
    T getThrows() throws Exception;  // NOSONAR squid:S00112 Code is correct
}
