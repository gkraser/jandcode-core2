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
    public static XDate EMPTY_DATE = XDate.create(0, 1, 1);

    /**
     * Специальная дата, которая рассматривается как пустая
     */
    public static XDateTime EMPTY_DATETIME = XDateTime.create(0, 1, 1);

    /**
     * Специальная дата, которая рассматривается как пустая дата конца
     */
    public static XDate EMPTY_DATE_END = XDate.create(3333, 12, 31);

    /**
     * Специальная дата, которая рассматривается как пустая дата конца
     */
    public static XDateTime EMPTY_DATETIME_END = XDateTime.create(3333, 12, 31);


    private static final int[] DAYS_PER_MONTH = {
            0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    //////

    /**
     * Проверка на пустую дату
     */
    public static boolean isEmpty(XDateTime d) {
        return d == null || d.equals(EMPTY_DATETIME) || d.equals(EMPTY_DATETIME_END);
    }

    /**
     * Проверка на пустую дату
     */
    public static boolean isEmpty(XDate d) {
        return d == null || d.equals(EMPTY_DATE) || d.equals(EMPTY_DATE_END);
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
