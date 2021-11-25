package jandcode.commons.datetime;

import java.time.*;

/**
 * Методы для даты
 */
public interface IDateApi<TYPE> {

    /**
     * Декодировать дату на составные части
     */
    XDateTimeDecoded decode();

    /**
     * Перевести в строку указанного формата
     */
    String toString(XDateTimeFormatter fmt);

    /**
     * Перевести в java LocalDate
     */
    LocalDate toJavaLocalDate();

    //////

    /**
     * Является ли дата сегодняшним днем
     */
    boolean isToday();

    //////

    /**
     * Прибавить количество дней (+/-) и вернуть новую дату
     */
    TYPE addDays(int days);

    /**
     * Прибавить количество месяцев (+/-) и вернуть новую дату.
     * Если новая дата за границами месяца, то она приводится к концу месяца.
     */
    TYPE addMonths(int months);

    /**
     * Прибавить количество лет (+/-) и вернуть новую дату.
     * Если новая дата за границами месяца, то она приводится к концу месяца.
     */
    TYPE addYears(int years);

    //////

    /**
     * Возвращает дату начала месяца.
     * Время стирается.
     */
    TYPE beginOfMonth();

    /**
     * Возвращает дату конца месяца.
     * Время стирается.
     */
    TYPE endOfMonth();

    /**
     * Возвращает дату начала года.
     * Время стирается.
     */
    TYPE beginOfYear();

    /**
     * Возвращает дату конца года.
     * Время стирается.
     */
    TYPE endOfYear();

}
