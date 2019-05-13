package org.slf4j.test.logback;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ch.qos.logback.classic.Level;

public class RedirectLogbackTest {

    @Test
    public void testAllLogLevels() {
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        rl.with(() -> {
            new Foo().logAtAllLevels();
        });
        
        // TODO What happened to TRACE?
        assertEquals(
                "DEBUG bar\n" + 
                "INFO bar\n" + 
                "WARN bar\n" + 
                "ERROR bar\n" + 
                "", rl.dump(Level.ALL));
    }
    
    @Test
    public void testLogException() throws Exception {
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        rl.with(() -> {
            new Foo().logException();
        });
        
        // Note: No exception on stdout
        assertEquals(
                "WARN logException\n" + 
                "Just a test\n" + 
                "", rl.dump());
    }
    
    @Test
    public void testLoggingMutableList() throws Exception {
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        rl.with(() -> {
            new Foo().logMovingTarget();
        });
        
        assertEquals(
                "INFO values=[a]\n" + 
                "", rl.dump());
    }
    
    @Test
    public void testOmitDebugLevelByDefault() throws Exception {
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        rl.with(() -> {
            new Foo().bar();
        });
        
        // DEBUG is omitted by default
        assertEquals(
                "", rl.dump());
    }
    
    @Test
    public void testWithIntResult() throws Exception {
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        int actual = rl.with(() -> {
            return new Foo().bar();
        });
        
        assertEquals(5, actual);
        assertEquals(
                "DEBUG bar was called\n" + 
                "", rl.dump(Level.DEBUG));
    }
    
    @Test
    public void testAbsoluteFile() throws Exception {
        File baseDir = new File("").getAbsoluteFile();
        RedirectLogback rl = new RedirectLogback(Foo.class);
        
        File file = rl.with(() -> {
            return new Foo().getFile();
        });
        
        assertNotNull(file);
        // Replace changing path with variable
        String log = rl.dump(Level.DEBUG)
                .replace(baseDir.getPath(), "${basedir}");
        assertEquals(
                "DEBUG Loading POM from ${basedir}/pom.xml\n" + 
                "", log);
        
    }
}
