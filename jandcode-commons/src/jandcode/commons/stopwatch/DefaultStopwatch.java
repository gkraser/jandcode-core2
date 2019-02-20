package jandcode.commons.stopwatch;

import jandcode.commons.named.*;

/**
 * Стандартная реализация секундомера
 */
public class DefaultStopwatch extends Named implements Stopwatch {

    private long timeStart;
    private long counter;
    private long timeTotal;
    private StopwatchState state = StopwatchState.stopped;
    private StopwatchFormatter formatter;

    public DefaultStopwatch() {
    }

    public DefaultStopwatch(String name) {
        this.setName(name);
    }

    public void start() {
        this.timeTotal = 0;
        this.counter = 0;
        setState(StopwatchState.runned);
        this.timeStart = System.currentTimeMillis();
    }

    public void stop() {
        if (this.state == StopwatchState.stopped) {
            return;
        }
        if (this.state == StopwatchState.runned) {
            pause();
        }
        if (this.state == StopwatchState.paused) {
            setState(StopwatchState.stopped);
        }
    }

    public void pause() {
        if (this.state == StopwatchState.stopped || this.state == StopwatchState.paused) {
            return;
        }
        if (this.state == StopwatchState.runned) {
            long tcur = System.currentTimeMillis();
            this.timeTotal = this.timeTotal + (tcur - this.timeStart);
            setState(StopwatchState.paused);
        }
    }

    public void resume() {
        if (this.state == StopwatchState.stopped || this.state == StopwatchState.runned) {
            return;
        }
        if (this.state == StopwatchState.paused) {
            this.timeStart = System.currentTimeMillis();
            setState(StopwatchState.runned);
        }
    }

    public StopwatchState getState() {
        return state;
    }

    protected void setState(StopwatchState state) {
        this.state = state;
    }

    public long getTime() {
        if (this.state == StopwatchState.runned) {
            // сейчас запущен
            long tcur = System.currentTimeMillis();
            return this.timeTotal + (tcur - this.timeStart);
        } else {
            return this.timeTotal;
        }
    }

    public long getCounter() {
        return this.counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public void incCounter(long v) {
        this.counter = this.counter + v;
    }

    public void setFormatter(StopwatchFormatter formatter) {
        this.formatter = formatter;
    }

    public String toString() {
        if (formatter == null) {
            return new DefaultStopwatchFormatter().toString(this);
        } else {
            return formatter.toString(this);
        }
    }

}
