package org.slf4j.test;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.LoggerFactory;
import org.slf4j.test.utils.Pair;
import org.slf4j.test.utils.Redirector;
import org.slf4j.test.utils.ThrowingRunnable;
import org.slf4j.test.utils.ThrowingSupplier;

public abstract class RedirectLogger<E> {

    private List<Redirector<E>> infos = new ArrayList<>();
    protected Function<E, String> toString;
    protected ListAppender<E> appender;
    
    public RedirectLogger(Class<?>... types) {
        infos = createLoggers (types);
    }
    
    public RedirectLogger(org.slf4j.Logger... loggers) {
        infos = createLoggers (loggers);
    }

    protected abstract List<Redirector<E>> createLoggers(org.slf4j.Logger[] loggers);

    public RedirectLogger<E> toString(Function<E, String> toString) {
        this.toString = toString;
        return this;
    }
    
    private List<Redirector<E>> createLoggers(Class<?>[] types) {
        org.slf4j.Logger[] loggers = new org.slf4j.Logger[types.length];
        for(int i=0; i<types.length; i++) {
            loggers[i] = LoggerFactory.getLogger(types[i]);
        }
        
        return createLoggers(loggers);
    }
    
    public void install() {
        if(infos.isEmpty()) {
            throw new IllegalStateException("No loggers defined");
        }
        
        appender = createAppender();
        
        for(Redirector<E> item: infos) {
            item.setAppender(appender);
            item.install();
        }
    }
    
    protected abstract ListAppender<E> createAppender();
    
    public void deinstall() {
        if(infos.isEmpty()) {
            throw new IllegalStateException("No loggers defined");
        }
        
        for(Redirector<E> item: infos) {
            item.deinstall();
        }
    }
    
    public String dump(Predicate<E> filter) {
        StringBuilder buffer = new StringBuilder();
        
        for(Pair<E, String> event: appender.getEvents()) {
            if (filter.test(event.getKey())) {
                buffer.append(event.getValue());
            }
        }
        
        return buffer.toString().replace("\r\n", "\n");
    }
    
    public void with(ThrowingRunnable runnable) {
        try {
            install();
            
            runnable.run();
        } finally {
            deinstall();
        }
    }
    
    public <T> T with(ThrowingSupplier<T> supplier) {
        try {
            install();
            
            T result = supplier.get();
            return result;
        } finally {
            deinstall();
        }
    }
}
