package jandcode.commons.datetime.impl;

import jandcode.commons.datetime.*;

import java.time.*;
import java.util.*;

public final class XDateTime2Impl implements XDateTime2 {

    private final Jdn jdn;

    public static XDateTime2 create(Jdn jdn) {
        return new XDateTime2Impl(jdn);
    }

    private XDateTime2Impl(Jdn jdn) {
        this.jdn = jdn;
    }

    public XDateTimeDecoded decode() {
        return jdn.decode();
    }

    public String toString() {
        return decode().toString();
    }

    public String toString(XDateTimeFormatter fmt) {
        return fmt.toString(this);
    }

    public LocalDate toJavaLocalDate() {
        return jdn.toJavaLocalDate();
    }

    public XDateTime2 addDays(int days) {
        return create(jdn.addDays(days));
    }

    public XDateTime2 addMonths(int months) {
        return create(jdn.addMonths(months));
    }

    public XDateTime2 addYears(int years) {
        return create(jdn.addYears(years));
    }

    public XDateTime2 beginOfMonth() {
        return create(jdn.beginOfMonth());
    }

    public XDateTime2 endOfMonth() {
        return create(jdn.endOfMonth());
    }

    public XDateTime2 beginOfYear() {
        return create(jdn.beginOfYear());
    }

    public XDateTime2 endOfYear() {
        return create(jdn.endOfYear());
    }

    ////// system

    public int compareTo(XDateTime2 o) {
        if (!(o instanceof XDateTime2Impl)) {
            return 1;
        }
        return jdn.compareTo(((XDateTime2Impl) o).jdn);
    }

    public int hashCode() {
        return jdn.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof XDateTime2Impl) {
            return jdn.equals(obj);
        } else {
            return false;
        }
    }

    ////// time

    public LocalDateTime toJavaLocalDateTime() {
        return jdn.toJavaLocalDateTime();
    }

    public ZonedDateTime toJavaZonedDateTime() {
        return jdn.toJavaZonedDateTime();
    }

    public ZonedDateTime toJavaZonedDateTime(ZoneId zone) {
        return jdn.toJavaZonedDateTime(zone);
    }

    public Date toJavaDate() {
        return jdn.toJavaDate();
    }

    public Date toJavaDate(ZoneId zone) {
        return jdn.toJavaDate(zone);
    }

    public XDateTime2 toZone(ZoneId from, ZoneId to) {
        LocalDateTime q = toJavaLocalDateTime();
        ZonedDateTime w = q.atZone(from);
        ZonedDateTime e = w.withZoneSameInstant(to);
        return create(Jdn.create(e.toLocalDateTime()));
    }

    public XDateTime2 toZone(ZoneId to) {
        return toZone(ZoneId.systemDefault(), to);
    }

    public XDateTime2 clearTime() {
        return create(jdn.clearTime());
    }

    public boolean hasTime() {
        return jdn.hasTime();
    }

    public XDateTime2 clearMSec() {
        return create(jdn.clearMSec());
    }

}
