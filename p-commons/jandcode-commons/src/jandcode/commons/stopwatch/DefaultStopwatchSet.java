package jandcode.commons.stopwatch;

import java.util.*;

/**
 * Набор секундомеров по умолчанию
 */
public class DefaultStopwatchSet implements StopwatchSet {

    private boolean autoPrint;
    private Map<String, Stopwatch> items = new LinkedHashMap<>();
    private StopwatchFormatter formatter = new DefaultStopwatchFormatter();

    class StopwatchItem extends DefaultStopwatch {

        public StopwatchItem(String name) {
            setName(name);
            setFormatter(DefaultStopwatchSet.this);
        }

        protected void setState(StopwatchState state) {
            super.setState(state);
            if (autoPrint) {
                if (state == StopwatchState.stopped) {
                    print(this);
                }
            }
        }

        public String toString() {
            return DefaultStopwatchSet.this.toString(this);
        }

    }

    //////

    /**
     * @param autoPrint true - автоматически печатать секундомер
     *                  на консоле при его остановке
     */
    public DefaultStopwatchSet(boolean autoPrint) {
        this.autoPrint = autoPrint;
    }

    public Collection<Stopwatch> getItems() {
        return items.values();
    }

    public void print(Stopwatch sw) {
        System.out.println(sw.toString());
    }

    public Stopwatch get(String name) {
        Stopwatch sw = items.get(name);
        if (sw == null) {
            sw = new StopwatchItem(name);
            items.put(name, sw);
        }
        return sw;
    }

    public Stopwatch get() {
        return get("default");
    }

    public void stopAll() {
        for (Stopwatch sw : items.values()) {
            sw.stop();
        }
    }

    public void setFormatter(StopwatchFormatter formatter) {
        this.formatter = formatter;
        if (this.formatter == null) {
            this.formatter = new DefaultStopwatchFormatter();
        }
    }

    public StopwatchFormatter getFormatter() {
        return formatter;
    }

    public String toString(Stopwatch sw) {
        return formatter.toString(sw);
    }

}
