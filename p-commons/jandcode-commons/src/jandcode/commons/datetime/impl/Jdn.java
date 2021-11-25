package jandcode.commons.datetime.impl;

import jandcode.commons.*;

import java.time.*;
import java.util.*;

/**
 * Юлианские дни и время.
 */
public final class Jdn implements Comparable<Jdn>, DateTimeConsts {

    /**
     * Юлианские дни
     */
    public final int jdn;

    /**
     * Милисекунды дня
     */
    public final int time;

    ////// factory

    public static Jdn create(int jdn, int time) {
        return new Jdn(jdn, time);
    }

    public static Jdn create(String s) {
        XDateTimeDecodedImpl ddt = new XDateTimeDecodedImpl();
        ddt.parse(s);
        return create(encodeJulianDay(ddt), encodeTime(ddt));
    }

    public static Jdn create(XDateTimeDecodedImpl ddt) {
        return create(encodeJulianDay(ddt), encodeTime(ddt));
    }

    public static Jdn create(int year, int month, int day) {
        return create(encodeJulianDay(year, month, day), 0);
    }

    public static Jdn create(int year, int month, int day, int hour, int min, int sec, int msec) {
        return create(encodeJulianDay(year, month, day), encodeTime(hour, min, sec, msec));
    }

    public static Jdn create(LocalDateTime d) {
        return create(
                encodeJulianDay(d.getYear(), d.getMonthValue(), d.getDayOfMonth()),
                encodeTime(d.getHour(), d.getMinute(), d.getSecond(), d.getNano() / NANOSEC_IN_MSEC)
        );
    }

    public static Jdn create(LocalDate d) {
        return create(encodeJulianDay(d.getYear(), d.getMonthValue(), d.getDayOfMonth()), 0);
    }

    public static Jdn create(long instant) {
        return create(instant, ZoneId.systemDefault());
    }

    public static Jdn create(long instant, ZoneId zone) {
        return create(LocalDateTime.ofInstant(Instant.ofEpochMilli(instant), zone));
    }

    public static Jdn create(Date date) {
        return create(date.getTime());
    }

    //////

    /**
     * Юлианские дни разбирает на запчасти и кладет в ddt
     */
    public static void decodeJulianDay(XDateTimeDecodedImpl ddt, int jdn) {
        int a = jdn + 32044;
        int b = (4 * a + 3) / 146097;
        int c = a - (146097 * b) / 4;
        int d = (4 * c + 3) / 1461;
        int e = c - (1461 * d) / 4;
        int m = (5 * e + 2) / 153;

        ddt.day = e - (153 * m + 2) / 5 + 1;
        ddt.month = m + 3 - 12 * (m / 10);
        ddt.year = 100 * b + d - 4800 + m / 10;
        ddt.dow = jdn % 7 + 1;
    }

    /**
     * Собирает юлианскую дату
     */
    public static int encodeJulianDay(int year, int month, int day) {
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        return day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    /**
     * Собирает юлианскую дату
     */
    public static int encodeJulianDay(XDateTimeDecodedImpl ddt) {
        return encodeJulianDay(ddt.year, ddt.month, ddt.day);
    }

    /**
     * Разбирает время на запчасти
     */
    public static void decodeTime(XDateTimeDecodedImpl ddt, int time) {
        ddt.msec = (time % MSEC_IN_SEC);
        time = time / MSEC_IN_SEC;
        ddt.sec = time % SEC_IN_MIN;
        time = time / SEC_IN_MIN;
        ddt.min = time % MIN_IN_HOUR;
        time = time / MIN_IN_HOUR;
        ddt.hour = time;
    }

    /**
     * Собирает время из запчастей
     */
    public static int encodeTime(int hour, int min, int sec, int msec) {
        return msec + sec * MSEC_IN_SEC + min * MSEC_IN_MIN + hour * MSEC_IN_HOUR;
    }


    /**
     * Собирает время из запчастей
     */
    public static int encodeTime(XDateTimeDecodedImpl ddt) {
        return encodeTime(ddt.hour, ddt.min, ddt.sec, ddt.msec);
    }

    //////

    private Jdn(int jdn, int time) {
        this.jdn = jdn;
        this.time = time;
    }

    ////// system

    public int hashCode() {
        // превращаем в long
        long value = (((long) jdn) << 32) | time;
        // это hash из Long
        return (int) (value ^ (value >>> 32));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Jdn) {
            Jdn d = (Jdn) obj;
            return d.jdn == jdn && d.time == time;
        }
        return false;
    }

    public int compareTo(Jdn z) {
        if (z == null) {
            return 1;
        }
        if (jdn < z.jdn) {
            return -1;
        }
        if (jdn > z.jdn) {
            return 1;
        }
        if (time < z.time) {
            return -1;
        }
        if (time > z.time) {
            return 1;
        }
        return 0;
    }

    //////

    public XDateTimeDecodedImpl decode() {
        XDateTimeDecodedImpl res = new XDateTimeDecodedImpl();
        decodeJulianDay(res, jdn);
        if (time != 0) {
            decodeTime(res, time);
        }
        return res;
    }

    public String toString() {
        return decode().toString();
    }

    public LocalDate toJavaLocalDate() {
        XDateTimeDecodedImpl d = decode();
        return LocalDate.of(d.year, d.month, d.day);
    }

    public LocalDateTime toJavaLocalDateTime() {
        XDateTimeDecodedImpl d = decode();
        return LocalDateTime.of(d.year, d.month, d.day, d.hour, d.min, d.sec, d.msec * NANOSEC_IN_MSEC);
    }

    public ZonedDateTime toJavaZonedDateTime() {
        return toJavaZonedDateTime(ZoneId.systemDefault());
    }

    public ZonedDateTime toJavaZonedDateTime(ZoneId zone) {
        LocalDateTime jd = toJavaLocalDateTime();
        return ZonedDateTime.of(jd, zone);
    }

    public Date toJavaDate() {
        return toJavaDate(ZoneId.systemDefault());
    }

    public Date toJavaDate(ZoneId zone) {
        LocalDateTime jd = toJavaLocalDateTime();
        ZonedDateTime zd = ZonedDateTime.of(jd, zone);
        return Date.from(zd.toInstant());
    }

    //////

    public Jdn clearTime() {
        return create(jdn, 0);
    }

    public boolean hasTime() {
        return time > 0;
    }

    public Jdn clearMSec() {
        int t = time;
        if (t != 0) {
            t = t / MSEC_IN_SEC * MSEC_IN_SEC;
        }
        return create(jdn, t);
    }

    //////

    public Jdn addDays(int days) {
        return create(jdn + days, time);
    }

    public Jdn addMonths(int months) {
        if (months == 0) {
            return this;
        }

        XDateTimeDecodedImpl dt = decode();

        int y = dt.getYear();
        int m = dt.getMonth();
        int d = dt.getDay();

        int sign = 1;
        if (months < 0) {
            sign = -1;
            months = -months;
        }

        if (months > 12) {
            int overYear = months / 12;
            y = y + overYear * sign;
            months = months - overYear * 12;
        }

        if (months != 0) {
            m = m + months * sign;
            if (m > 12) {
                y = y + 1;
                m = m - 12;
            } else if (m < 1) {
                y = y - 1;
                m = m + 12;
            }
        }

        if (d > 28) {
            int dim = UtDateTime.getDaysInMonth(y, m);
            if (d > dim) {
                d = dim;
            }
        }

        int jdn = encodeJulianDay(y, m, d);
        return create(jdn, this.time);
    }

    public Jdn addYears(int years) {
        XDateTimeDecodedImpl dt = decode();
        int y = dt.getYear() + years;
        int d = dt.getDay();
        if (d > 28) {
            int dim = UtDateTime.getDaysInMonth(y, dt.getMonth());
            if (d > dim) {
                d = dim;
            }
        }
        int jdn = encodeJulianDay(y, dt.getMonth(), d);
        return create(jdn, this.time);
    }

    //////

    public Jdn beginOfMonth() {
        XDateTimeDecodedImpl dt = decode();
        int jdn = encodeJulianDay(dt.getYear(), dt.getMonth(), 1);
        return create(jdn, this.time);
    }

    public Jdn endOfMonth() {
        XDateTimeDecodedImpl dt = decode();
        int jdn = encodeJulianDay(dt.getYear(), dt.getMonth(), UtDateTime.getDaysInMonth(dt.getYear(), dt.getMonth()));
        return create(jdn, this.time);
    }

    public Jdn beginOfYear() {
        XDateTimeDecodedImpl dt = decode();
        int jdn = encodeJulianDay(dt.getYear(), 1, 1);
        return create(jdn, this.time);
    }

    public Jdn endOfYear() {
        XDateTimeDecodedImpl dt = decode();
        int jdn = encodeJulianDay(dt.getYear(), 12, UtDateTime.getDaysInMonth(dt.getYear(), 12));
        return create(jdn, this.time);
    }

}
