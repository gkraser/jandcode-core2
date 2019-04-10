package jandcode.commons.stopwatch;

/**
 * Преобразование секундомера в текст для печати
 */
public interface StopwatchFormatter {

    /**
     * Преобразовать секундомер в текст
     */
    String toString(Stopwatch sw);

}
