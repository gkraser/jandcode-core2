package jandcode.commons.datetime;

import jandcode.commons.*;

import java.time.format.*;

/**
 * Форматер дат и времени.
 */
public interface XDateTimeFormatter {

    /**
     * Дата в формате ISO
     */
    XDateTimeFormatter ISO_DATE = UtDateTime.createFormatter(DateTimeFormatter.ISO_LOCAL_DATE);

    /**
     * Время с msec в формате ISO
     */
    XDateTimeFormatter ISO_DATE_TIME = UtDateTime.createFormatter(DateTimeFormatter.ISO_DATE_TIME);

    /**
     * Дату в строку
     *
     * @param d дата
     * @return строковое представление
     */
    String toString(XDateTime d);

    /**
     * Строку в дату
     */
    XDateTime parse(String s);

    //////

    /**
     * Дату в строку
     *
     * @param d дата
     * @return строковое представление
     */
    String toString(XDateTime2 d);

    /**
     * Строку в дату
     */
    XDateTime2 parseDateTime(String s);

    /**
     * Дату в строку
     *
     * @param d дата
     * @return строковое представление
     */
    String toString(XDate d);

    /**
     * Строку в дату
     */
    XDate parseDate(String s);

}
