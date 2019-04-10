package jandcode.commons;

import jandcode.commons.datetime.*;
import jandcode.commons.datetime.impl.*;

import java.time.*;
import java.util.*;

/**
 * Утилиты для даты и времени
 */
public class UtDateTime {

    /**
     * Специальная дата, которая рассматривается как пустая
     */
    public static XDateTime EMPTY_DATE = new XDateTimeImpl(0, 1, 1);

    /**
     * Специальная дата, которая рассматривается как пустая дата конца
     */
    public static XDateTime EMPTY_DATE_END = new XDateTimeImpl(3333, 12, 31);

    private static final int[] DAYS_PER_MONTH = {
            0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    ////// constructors

    /**
     * Возвращает текущую дату и временя
     */
    public static XDateTime now() {
        return new XDateTimeImpl();
    }

    /**
     * Возвращает сегодняшнюю дату без времени
     *
     * @return дата без времени
     */
    public static XDateTime today() {
        return new XDateTimeImpl().clearTime();
    }

    /**
     * Создать дату из строки в формате iso
     */
    public static XDateTime create(String s) {
        return new XDateTimeImpl(s);
    }

    /**
     * Создать дату из строки в указанном формате
     */
    public static XDateTime create(String s, XDateTimeFormatter fmt) {
        return new XDateTimeImpl(s, (XDateTimeFormatterImpl) fmt);
    }

    /**
     * Создать дату из java Date
     */
    public static XDateTime create(Date z) {
        return new XDateTimeImpl(z);
    }

    /**
     * Создать дату из милисекунд
     */
    public static XDateTime create(long z) {
        return new XDateTimeImpl(z);
    }

    /**
     * Создать дату по указанным частям
     */
    public static XDateTime create(int year, int month, int day) {
        return new XDateTimeImpl(year, month, day);
    }

    /**
     * Создать дату по указанным частям
     */
    public static XDateTime create(int year, int month, int day, int hour, int min, int sec) {
        return new XDateTimeImpl(year, month, day, hour, min, sec);
    }

    /**
     * Создать дату по указанным частям
     */
    public static XDateTime create(int year, int month, int day, int hour, int min, int sec, int msec) {
        return new XDateTimeImpl(year, month, day, hour, min, sec, msec);
    }

    /**
     * Создать дату по LocalDateTime
     */
    public static XDateTime create(LocalDateTime d) {
        return new XDateTimeImpl(d);
    }

    /**
     * Создать дату по LocalDate
     */
    public static XDateTime create(LocalDate d) {
        return new XDateTimeImpl(d);
    }

    //////

    /**
     * Проверка на пустую дату
     */
    public static boolean isEmpty(XDateTime d) {
        return d == null || d.equals(EMPTY_DATE) || d.equals(EMPTY_DATE_END);
    }

    /**
     * Проверка, что дата - это сегодня
     *
     * @param dt проверяемая дата
     * @return true, если дата=сегодня
     */
    public static boolean isToday(XDateTime dt) {
        XDateTime today = today();
        return today.equals(dt.clearTime());
    }


    /**
     * Проверка на високосный год
     */
    public static boolean isLeap(int year) {
        return ((GregorianCalendar) GregorianCalendar.getInstance()).isLeapYear(year);
    }

    /**
     * Число дней в месяце
     */
    public static int getDaysInMonth(int year, int month) {
        int a = DAYS_PER_MONTH[month];
        if (month == 2 && isLeap(year)) {
            a++;
        }
        return a;
    }

    ////// formatters

    /**
     * Создать форматер
     *
     * @param pattern формат
     */
    public static XDateTimeFormatter createFormatter(String pattern) {
        return new XDateTimeFormatterImpl(pattern);
    }

    /**
     * Создать форматер для локали
     *
     * @param pattern формат
     * @param locale  локаль
     */
    public static XDateTimeFormatter createFormatter(String pattern, Locale locale) {
        return new XDateTimeFormatterImpl(pattern, locale);
    }

}
