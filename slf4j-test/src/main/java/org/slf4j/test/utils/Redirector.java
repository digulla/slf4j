package org.slf4j.test.utils;

import org.slf4j.test.ListAppender;

public interface Redirector<E> {
    void setAppender(ListAppender<E> appender);
    void install ();
    void deinstall ();
}
