package org.slf4j.test;

import java.util.List;

import org.slf4j.test.utils.Pair;

public interface ListAppender<E> {

    List<Pair<E, String>> getEvents();

}
