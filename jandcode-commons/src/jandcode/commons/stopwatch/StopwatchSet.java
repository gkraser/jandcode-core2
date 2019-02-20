package jandcode.commons.stopwatch;

import java.util.*;

/**
 * Набор секундомеров и работа с ними
 */
public interface StopwatchSet extends StopwatchFormatter {

    /**
     * Возвращает секундомер с указанным именем.
     * Создается при первом обращении.
     */
    Stopwatch get(String name);

    /**
     * Возвращает секундомер default.
     * Создается при первом обращении.
     */
    Stopwatch get();

    /**
     * Все секундомеры в наборе
     */
    Collection<Stopwatch> getItems();

    /**
     * Напечатать указанный секундомер
     */
    void print(Stopwatch sw);

    /**
     * Установить произвольный форматтер для печати секундомеров
     */
    void setFormatter(StopwatchFormatter formatter);

    /**
     * Остановить все секундомеры
     */
    void stopAll();

    /**
     * Запустить секундомер с указанным именем
     */
    default void start(String name) {
        get(name).start();
    }

    /**
     * Запустить секундомер с именем default
     */
    default void start() {
        get().start();
    }

    /**
     * Остановить секундомер с указанным именем
     */
    default void stop(String name) {
        get(name).stop();
    }

    /**
     * Остановить секундомер с именем default
     */
    default void stop() {
        get().stop();
    }

    /**
     * Остановить секундомер с указанным именем с одновременным присваиванием счетчика
     */
    default void stop(String name, long counter) {
        get(name).stop(counter);
    }

    /**
     * Остановить секундомер с именем default с одновременным присваиванием счетчика
     */
    default void stop(long counter) {
        get().stop(counter);
    }

    /**
     * Приостановить секундомер с указанным именем
     */
    default void pause(String name) {
        get(name).pause();
    }

    /**
     * Приостановить секундомер с именем default
     */
    default void pause() {
        get().pause();
    }

    /**
     * Продолжить работу секундомера с указанным именем
     */
    default void resume(String name) {
        get(name).resume();
    }

    /**
     * Продолжить работу секундомера с именем default
     */
    default void resume() {
        get().resume();
    }

}
