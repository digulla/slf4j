package org.slf4j.test.utils;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

    @Override
    default void run() {
        try {
            runThrows();
        } catch(Exception e) {
            throw new LambdaException(e);
        }
    }
    
    void runThrows() throws Exception;  // NOSONAR squid:S00112 Code is correct
}
