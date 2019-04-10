package jandcode.commons.stopwatch;

import jandcode.commons.*;

/**
 * Форматтер секундомера по умолчанию
 */
public class DefaultStopwatchFormatter implements StopwatchFormatter {

    public String toString(Stopwatch sw) {

        String name = sw.getName();
        long cnt = sw.getCounter();
        long time = sw.getTime();


        StringBuilder sb = new StringBuilder();

        if (UtString.empty(name) || "default".equals(name)) {
            sb.append("Completed");
        } else {
            sb.append(name);
            sb.append(": completed");
        }

        sb.append(String.format(" in %.3f sec", time / 1000.0));

        if (cnt > 0 && time > 0) {
            double insec = (double) cnt / time * 1000.0;
            sb.append(String.format(" (~%.0f in sec, total: %d)", insec, cnt)); //NON-NLS
        }

        return sb.toString();
    }
}
