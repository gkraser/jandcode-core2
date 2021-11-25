package jandcode.commons;

import jandcode.commons.datetime.*;
import jandcode.commons.datetime.impl.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Утилиты для даты и времени
 */
public class UtDateTime {

    /**
     * Специальная дата, которая рассматривается как пустая
     */
    public static XDateTime EMPTY_DATE = XDateTime.create(0, 1, 1);

    /**
     * Специальная дата, которая рассматривается как пустая дата конца
     */
    public static XDateTime EMPTY_DATE_END = XDateTime.create(3333, 12, 31);

    private static final int[] DAYS_PER_MONTH = {
            0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    ////// constructors

    /**
     * Возвращает текущую дату и временя
     */
    @Deprecated
    public static XDateTime now() {
        return XDateTime.now();
    }

    /**
     * Возвращает сегодняшнюю дату без времени
     *
     * @return дата без времени
     */
    @Deprecated
    public static XDateTime today() {
        return XDateTime.now().clearTime();
    }

    /**
     * Создать дату из строки в формате iso
     */
    @Deprecated
    public static XDateTime create(String s) {
        return XDateTime.create(s);
    }

    /**
     * Создать дату из строки в указанном формате
     */
    @Deprecated
    public static XDateTime create(String s, XDateTimeFormatter fmt) {
        return XDateTime.create(s, (XDateTimeFormatterImpl) fmt);
    }

    /**
     * Создать дату из java Date
     */
    @Deprecated
    public static XDateTime create(Date z) {
        return XDateTime.create(z);
    }

    /**
     * Создать дату из милисекунд
     */
    @Deprecated
    public static XDateTime create(long z) {
        return XDateTime.create(z);
    }

    /**
     * Создать дату по указанным частям
     */
    @Deprecated
    public static XDateTime create(int year, int month, int day) {
        return XDateTime.create(year, month, day);
    }

    /**
     * Создать дату по LocalDateTime
     */
    @Deprecated
    public static XDateTime create(LocalDateTime d) {
        return XDateTime.create(d);
    }

    /**
     * Создать дату по LocalDate
     */
    @Deprecated
    public static XDateTime create(LocalDate d) {
        return XDateTime.create(d);
    }

    //////

    /**
     * Проверка на пустую дату
     */
    public static boolean isEmpty(XDateTime d) {
        return d == null || d.equals(EMPTY_DATE) || d.equals(EMPTY_DATE_END);
    }

    /**
     * Проверка на пустую дату
     */
    public static boolean isEmpty(XDate d) {
        return d == null || d.equals(EMPTY_DATE.toDate()) || d.equals(EMPTY_DATE_END.toDate());
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

    /**
     * Создать форматер
     *
     * @param fmt формат
     */
    public static XDateTimeFormatter createFormatter(DateTimeFormatter fmt) {
        return new XDateTimeFormatterImpl(fmt);
    }

}
