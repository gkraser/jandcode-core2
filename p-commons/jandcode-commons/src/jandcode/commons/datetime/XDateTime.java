package jandcode.commons.datetime;

import java.time.*;
import java.util.*;

/**
 * Дата и время. Без учета timezone. Неизменяемая.
 */
public interface XDateTime extends Comparable<XDateTime> {

    /**
     * Декодировать дату на составные части
     */
    XDateTimeDecoded decode();

    /**
     * Перевести в строку указанного формата
     */
    String toString(XDateTimeFormatter fmt);

    /**
     * Перевести в java LocalDateTime
     */
    LocalDateTime toJavaLocalDateTime();

    /**
     * Перевести в java ZonedDateTime, считая, что дата XDateTime в текущей зоне
     */
    ZonedDateTime toJavaZonedDateTime();

    /**
     * Перевести в java ZonedDateTime, считая, что дата XDateTime в зоне zone
     */
    ZonedDateTime toJavaZonedDateTime(ZoneId zone);

    /**
     * Перевести в java Date, считая, что дата XDateTime в текущей зоне
     */
    Date toJavaDate();

    /**
     * Перевести в java Date, считая, что дата XDateTime в зоне zone
     */
    Date toJavaDate(ZoneId zone);

    /**
     * Считая, что дата в зоне from, перевести ее в to
     */
    XDateTime toZone(ZoneId from, ZoneId to);

    /**
     * Считая, что дата в текущей зоне, перевести ее в to
     */
    XDateTime toZone(ZoneId to);

    //////

    /**
     * Очистить время
     */
    XDateTime clearTime();

    /**
     * Есть ли время
     */
    boolean hasTime();

    /**
     * Очистить милисекунды
     */
    XDateTime clearMSec();

    //////

    /**
     * Прибавить количество дней (+/-) и вернуть новую дату
     */
    XDateTime addDays(int days);

    /**
     * Прибавить количество месяцев (+/-) и вернуть новую дату.
     * Если новая дата за границами месяца, то она приводится к концу месяца.
     */
    XDateTime addMonths(int months);

    /**
     * Прибавить количество лет (+/-) и вернуть новую дату.
     * Если новая дата за границами месяца, то она приводится к концу месяца.
     */
    XDateTime addYears(int years);

    //////

    /**
     * Возвращает дату начала месяца.
     * Время стирается.
     */
    XDateTime beginOfMonth();

    /**
     * Возвращает дату конца месяца.
     * Время стирается.
     */
    XDateTime endOfMonth();

    /**
     * Возвращает дату начала года.
     * Время стирается.
     */
    XDateTime beginOfYear();

    /**
     * Возвращает дату конца года.
     * Время стирается.
     */
    XDateTime endOfYear();

}

