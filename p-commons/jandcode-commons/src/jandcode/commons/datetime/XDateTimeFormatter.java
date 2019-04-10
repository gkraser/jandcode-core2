package jandcode.commons.datetime;

import jandcode.commons.*;

/**
 * Форматер дат и времени.
 */
public interface XDateTimeFormatter {

    /**
     * Дата в формате ISO
     */
    XDateTimeFormatter ISO_DATE = UtDateTime.createFormatter("yyyy-MM-dd");

    /**
     * Дата и время в формате ISO
     */
    XDateTimeFormatter ISO_DATE_TIME = UtDateTime.createFormatter("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Дата и время с msec в формате ISO
     */
    XDateTimeFormatter ISO_DATE_TIME_MSEC = UtDateTime.createFormatter("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Время с msec в формате ISO
     */
    XDateTimeFormatter ISO_TIME = UtDateTime.createFormatter("HH:mm:ss");

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

}
