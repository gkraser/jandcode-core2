package jandcode.commons.datetime;

import jandcode.commons.datetime.impl.*;

import java.time.*;
import java.util.*;

/**
 * Дата и время. Без учета timezone. Неизменяемая.
 */
public interface XDateTime2 extends Comparable<XDateTime2>, IDateApi<XDateTime2>, ITimeApi<XDateTime2> {

    private static XDateTime2 create(Jdn jdn) {
        return XDateTime2Impl.create(jdn);
    }

    /**
     * Возвращает текущую дату и время
     */
    static XDateTime2 now() {
        return create(Jdn.create(LocalDateTime.now()));
    }

    /**
     * Создать из строки в формате iso
     */
    static XDateTime2 create(String s) {
        return create(Jdn.create(s));
    }

    /**
     * Создать из строки в указанном формате
     */
    static XDateTime2 create(String s, XDateTimeFormatter fmt) {
        return fmt.parseDateTime(s);
    }

    /**
     * Создать дату из java Date
     */
    static XDateTime2 create(Date z) {
        return create(Jdn.create(z));
    }

    /**
     * Создать дату из милисекунд
     */
    static XDateTime2 create(long z) {
        return create(Jdn.create(z));
    }

    /**
     * Создать дату из милисекунд
     */
    static XDateTime2 create(long z, ZoneId zone) {
        return create(Jdn.create(z, zone));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDateTime2 create(int year, int month, int day) {
        return create(Jdn.create(year, month, day));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDateTime2 create(int year, int month, int day, int hour, int min, int sec) {
        return create(Jdn.create(year, month, day, hour, min, sec, 0));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDateTime2 create(int year, int month, int day, int hour, int min, int sec, int msec) {
        return create(Jdn.create(year, month, day, hour, min, sec, msec));
    }

    /**
     * Создать дату по LocalDateTime
     */
    static XDateTime2 create(LocalDateTime d) {
        return create(Jdn.create(d));
    }

    /**
     * Создать дату по LocalDate
     */
    static XDateTime2 create(LocalDate d) {
        return create(Jdn.create(d));
    }

}
