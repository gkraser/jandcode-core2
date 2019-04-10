package jandcode.commons.datetime.impl;

import jandcode.commons.datetime.*;

import java.time.*;
import java.util.*;

public class XDateTimeImpl implements XDateTime, DateTimeConsts {

    /**
     * Юлианские дни
     */
    protected int jdn;

    /**
     * Милисекунды дня
     */
    protected int time;

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
    protected static int encodeJulianDay(int year, int month, int day) {
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
    protected static int encodeTime(int hour, int min, int sec, int msec) {
        return msec + sec * MSEC_IN_SEC + min * MSEC_IN_MIN + hour * MSEC_IN_HOUR;
    }


    /**
     * Собирает время из запчастей
     */
    public static int encodeTime(XDateTimeDecodedImpl ddt) {
        return encodeTime(ddt.hour, ddt.min, ddt.sec, ddt.msec);
    }

    ////// constructors

    protected XDateTimeImpl(int jdn, int time) {
        this.jdn = jdn;
        this.time = time;
    }

    protected XDateTimeImpl(XDateTimeDecodedImpl ddt) {
        this.jdn = encodeJulianDay(ddt);
        this.time = encodeTime(ddt);
    }

    /**
     * Инициализируется текущим временем
     */
    public XDateTimeImpl() {
        LocalDateTime d = LocalDateTime.now();
        applyLocalDateTime(d);
    }

    public XDateTimeImpl(int year, int month, int day) {
        this.jdn = encodeJulianDay(year, month, day);
    }

    public XDateTimeImpl(int year, int month, int day, int hour, int min, int sec) {
        this.jdn = encodeJulianDay(year, month, day);
        this.time = encodeTime(hour, min, sec, 0);
    }

    public XDateTimeImpl(int year, int month, int day, int hour, int min, int sec, int msec) {
        this.jdn = encodeJulianDay(year, month, day);
        this.time = encodeTime(hour, min, sec, msec);
    }

    public XDateTimeImpl(LocalDateTime d) {
        applyLocalDateTime(d);
    }

    public XDateTimeImpl(LocalDate d) {
        this(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }

    public XDateTimeImpl(String s) {
        XDateTimeDecodedImpl ddt = new XDateTimeDecodedImpl();
        ddt.parse(s);
        this.jdn = encodeJulianDay(ddt);
        this.time = encodeTime(ddt);
    }

    public XDateTimeImpl(String s, XDateTimeFormatterImpl fmt) {
        XDateTimeDecodedImpl ddt = new XDateTimeDecodedImpl();
        fmt.parse(s, ddt);
        this.jdn = encodeJulianDay(ddt);
        this.time = encodeTime(ddt);
    }

    public XDateTimeImpl(long instant) {
        this(instant, ZoneId.systemDefault());
    }

    public XDateTimeImpl(long instant, ZoneId zone) {
        Instant m = Instant.ofEpochMilli(instant);
        applyLocalDateTime(LocalDateTime.ofInstant(m, zone));
    }

    public XDateTimeImpl(Date date) {
        this(date.getTime());
    }

    ////// system

    private void applyLocalDateTime(LocalDateTime d) {
        this.jdn = encodeJulianDay(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
        this.time = encodeTime(d.getHour(), d.getMinute(), d.getSecond(), d.getNano() / NANOSEC_IN_MSEC);
    }

    public int hashCode() {
        // превращаем в long
        long value = (((long) jdn) << 32) | time;
        // это hash из Long
        return (int) (value ^ (value >>> 32));
    }

    public boolean equals(Object obj) {
        if (obj instanceof XDateTimeImpl) {
            XDateTimeImpl d = (XDateTimeImpl) obj;
            return d.jdn == jdn && d.time == time;
        }
        return false;
    }

    public int compareTo(XDateTime z1) {
        XDateTimeImpl z = (XDateTimeImpl) z1;
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

    public String toString(XDateTimeFormatter fmt) {
        return fmt.toString(this);
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

    public XDateTimeImpl toZone(ZoneId from, ZoneId to) {
        LocalDateTime q = toJavaLocalDateTime();
        ZonedDateTime w = q.atZone(from);
        ZonedDateTime e = w.withZoneSameInstant(to);
        return new XDateTimeImpl(e.toLocalDateTime());
    }

    public XDateTimeImpl toZone(ZoneId to) {
        return toZone(ZoneId.systemDefault(), to);
    }

    //////

    public XDateTimeImpl clearTime() {
        return new XDateTimeImpl(jdn, 0);
    }

    public boolean hasTime() {
        return time > 0;
    }

    public XDateTimeImpl clearMSec() {
        int t = time;
        if (t != 0) {
            t = t / MSEC_IN_SEC * MSEC_IN_SEC;
        }
        return new XDateTimeImpl(jdn, t);
    }

    //////

    public XDateTimeImpl addDays(int days) {
        return new XDateTimeImpl(jdn + days, time);
    }


}
