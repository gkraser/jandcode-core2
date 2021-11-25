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

}
