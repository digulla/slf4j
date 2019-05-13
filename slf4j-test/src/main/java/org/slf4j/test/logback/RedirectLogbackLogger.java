package org.slf4j.test.logback;

import java.lang.reflect.Field;

import org.slf4j.test.ListAppender;
import org.slf4j.test.utils.FieldAccessException;
import org.slf4j.test.utils.Redirector;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class RedirectLogbackLogger implements Redirector<ILoggingEvent> {
    private Logger logger;
    private boolean additive;
    private AppenderAttachableImpl<ILoggingEvent> aai;
    private boolean installed = false;
    private Appender<ILoggingEvent> listAppender;
    
    public RedirectLogbackLogger(org.slf4j.Logger logger) {
        this.logger = (Logger) logger;
        additive = this.logger.isAdditive();
        aai = getPrivateField(this.logger, "aai");
    }

    @SuppressWarnings("unchecked")
    public void setAppender(ListAppender<ILoggingEvent> listAppender) {
        this.listAppender = (Appender<ILoggingEvent>) listAppender;
    }
    
    public void install () {
        if (installed) {
            throw new IllegalStateException("Already installed");
        }
        if (listAppender == null) {
            throw new IllegalStateException("Set listAppender, first");
        }
        
        AppenderAttachableImpl<ILoggingEvent> replacement = new AppenderAttachableImpl<>();
        replacement.addAppender(listAppender);
        
        logger.setAdditive(false);
        setPrivateField(logger, "aai", replacement);
        installed = true;
    }
    
    public void deinstall () {
        if (!installed) {
            throw new IllegalStateException("Not installed");
        }
        
        logger.setAdditive(additive);
        setPrivateField(logger, "aai", aai);
        installed = false;
    }
    
    private void setPrivateField(Object instance,  String name, Object value) {
        try {
            Field f = instance.getClass().getDeclaredField(name);
            f.setAccessible(true);
            
            f.set(instance, value);
        } catch (Exception e) {
            throw new FieldAccessException(instance, name, e);
        }
    }
    
    private <T> T getPrivateField(Object instance,  String name) {
        try {
            Field f = instance.getClass().getDeclaredField(name);
            f.setAccessible(true);
            
            @SuppressWarnings("unchecked")
            T tmp = (T)f.get(instance);
            return tmp;
        } catch (Exception e) {
            throw new FieldAccessException(instance, name, e);
        }
    }
}
