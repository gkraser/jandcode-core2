package jandcode.commons.datetime;

import jandcode.commons.datetime.impl.*;

import java.time.*;
import java.util.*;

/**
 * Дата. Неизменяемая.
 */
public interface XDate extends Comparable<XDate>, IDateApi<XDate> {

    private static XDate create(Jdn jdn) {
        return XDateImpl.create(jdn);
    }

    /**
     * Возвращает сегодняшнюю дату
     */
    static XDate today() {
        return create(Jdn.create(LocalDate.now()));
    }

    /**
     * Создать из строки в формате iso
     */
    static XDate create(String s) {
        return create(Jdn.create(s));
    }

    /**
     * Создать из строки в указанном формате
     */
    static XDate create(String s, XDateTimeFormatter fmt) {
        return fmt.parseDate(s);
    }

    /**
     * Создать дату из java Date
     */
    static XDate create(Date z) {
        return create(Jdn.create(z));
    }

    /**
     * Создать дату из милисекунд
     */
    static XDate create(long z) {
        return create(Jdn.create(z));
    }

    /**
     * Создать дату из милисекунд
     */
    static XDate create(long z, ZoneId zone) {
        return create(Jdn.create(z, zone));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDate create(int year, int month, int day) {
        return create(Jdn.create(year, month, day));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDate create(int year, int month, int day, int hour, int min, int sec) {
        return create(Jdn.create(year, month, day, hour, min, sec, 0));
    }

    /**
     * Создать дату по указанным частям
     */
    static XDate create(int year, int month, int day, int hour, int min, int sec, int msec) {
        return create(Jdn.create(year, month, day, hour, min, sec, msec));
    }

    /**
     * Создать дату по LocalDateTime
     */
    static XDate create(LocalDateTime d) {
        return create(Jdn.create(d));
    }

    /**
     * Создать дату по LocalDate
     */
    static XDate create(LocalDate d) {
        return create(Jdn.create(d));
    }

    //////

    /**
     * Сконвертировать в дату
     */
    XDateTime toDateTime();

}
