package org.slf4j.test.logback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.test.ListAppender;
import org.slf4j.test.utils.Pair;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class LogbackListAppender extends AppenderBase<ILoggingEvent> implements ListAppender<ILoggingEvent> {

    private final List<Pair<ILoggingEvent, String>> events = new ArrayList<>();
    private Function<ILoggingEvent, String> toString;

    public LogbackListAppender(Function<ILoggingEvent, String> toString) {
        this.toString = toString;
    }
    
    @Override
    protected void append(ILoggingEvent e) {
        String string = toString.apply(e);
        events.add(Pair.of(e, string));
    }
    
    @Override
    public List<Pair<ILoggingEvent, String>> getEvents() {
        return events;
    }
}
