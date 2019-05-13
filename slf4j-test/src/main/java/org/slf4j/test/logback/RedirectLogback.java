package org.slf4j.test.logback;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.test.ListAppender;
import org.slf4j.test.RedirectLogger;
import org.slf4j.test.utils.Redirector;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RedirectLogback extends RedirectLogger<ILoggingEvent> {

    public RedirectLogback(Class<?>... types) {
        super(types);
        
        init();
    }

    public RedirectLogback(Logger... loggers) {
        super(loggers);
        
        init();
    }

    private void init() {
        toString(new EventToString());
    }
    
    protected List<Redirector<ILoggingEvent>> createLoggers(org.slf4j.Logger[] loggers) {
        List<Redirector<ILoggingEvent>> result = new ArrayList<>();
        
        for(org.slf4j.Logger logger: loggers) {
            result.add(new RedirectLogbackLogger (logger));
        }
        
        return result;
    }

    @Override
    public void deinstall() {
        super.deinstall();
        
        ((LogbackListAppender)appender).stop();
    }
    
    public String dump() {
        return dump(Level.INFO);
    }
    
    public String dump(Level level) {
        return dump (LevelFilter.greaterOrEqual(level));
    }

    @Override
    protected ListAppender<ILoggingEvent> createAppender() {
        LogbackListAppender result = new LogbackListAppender(toString);
        
        result.start();
        
        return result;
    }
}
