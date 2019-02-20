package jandcode.commons.test;

import jandcode.commons.stopwatch.*;

import java.util.*;

/**
 * Утилиты для секундомера. Для тестов.
 */
public class StopwatchTestSvc extends BaseTestSvc implements StopwatchSet {

    private StopwatchSet stopwatchSet = new DefaultStopwatchSet(true);

    public Stopwatch get(String name) {
        return stopwatchSet.get(name);
    }

    public Stopwatch get() {
        return stopwatchSet.get();
    }

    public Collection<Stopwatch> getItems() {
        return stopwatchSet.getItems();
    }

    public void print(Stopwatch sw) {
        stopwatchSet.print(sw);
    }

    public void stopAll() {
        stopwatchSet.stopAll();
    }

    public void setFormatter(StopwatchFormatter formatter) {
        stopwatchSet.setFormatter(formatter);
    }

    public String toString(Stopwatch sw) {
        return stopwatchSet.toString(sw);
    }

}
