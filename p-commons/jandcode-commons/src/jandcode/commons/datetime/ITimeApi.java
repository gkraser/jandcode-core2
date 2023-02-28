package jandcode.commons.datetime;

import java.time.*;
import java.util.*;

/**
 * Методы для времени
 */
public interface ITimeApi<TYPE> {

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
    TYPE toZone(ZoneId from, ZoneId to);

    /**
     * Считая, что дата в текущей зоне, перевести ее в to
     */
    TYPE toZone(ZoneId to);

    //////

    /**
     * Очистить время
     */
    TYPE clearTime();

    /**
     * Есть ли время
     */
    boolean hasTime();

    /**
     * Очистить милисекунды
     */
    TYPE clearMSec();

    //////

    /**
     * Возвращает эту дату с указанным временем
     */
    TYPE withTime(int hour, int min, int sec, int msec);

    /**
     * Прибавить количество милисекунд (+/-) и вернуть новую дату
     */
    TYPE addMSec(long msec);

    /**
     * Разница между указанной датой в милисекундах.
     * Считается по логике (this - dt), т.е. если this меньше dt, возвращает отрицательное
     * значение, если this больше dt, возвращает положительное значение.
     *
     * @param dt для какой даты найти разницу
     * @return число милисекунд (+/-)
     */
    long diffMSec(TYPE dt);

}
