package org.slf4j.test.logback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foo {

    private static final Logger LOG = LoggerFactory.getLogger(Foo.class);
    
    public void logAtAllLevels() {
        LOG.trace("bar");
        LOG.debug("bar");
        LOG.info("bar");
        LOG.warn("bar");
        LOG.error("bar");
    }
    
    public void logException() {
        LOG.warn("logException", new Exception("Just a test"));
    }
    
    public void logMovingTarget() {
        List<String> values = new ArrayList<>();
        values.add("a");
        LOG.info("values={}", values);
        
        values.add("b"); // With lazy evaluation, the log message will now change!
    }
    
    public int bar() {
        LOG.debug("bar was called");
        return 5;
    }
    
    public File getFile() {
        File result = new File("pom.xml").getAbsoluteFile();
        LOG.debug("Loading POM from {}", result);
        return result;
    }
}
