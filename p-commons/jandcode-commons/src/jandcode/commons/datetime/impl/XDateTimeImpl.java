package jandcode.commons.datetime.impl;

import jandcode.commons.datetime.*;

import java.time.*;
import java.util.*;

public final class XDateTimeImpl implements XDateTime {

    final Jdn jdn;

    public static XDateTime create(Jdn jdn) {
        return new XDateTimeImpl(jdn);
    }

    private XDateTimeImpl(Jdn jdn) {
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

    public XDateTime addDays(int days) {
        return create(jdn.addDays(days));
    }

    public XDateTime addMonths(int months) {
        return create(jdn.addMonths(months));
    }

    public XDateTime addYears(int years) {
        return create(jdn.addYears(years));
    }

    public XDateTime beginOfMonth() {
        return create(jdn.beginOfMonth());
    }

    public XDateTime endOfMonth() {
        return create(jdn.endOfMonth());
    }

    public XDateTime beginOfYear() {
        return create(jdn.beginOfYear());
    }

    public XDateTime endOfYear() {
        return create(jdn.endOfYear());
    }

    ////// system

    public int compareTo(Object o) {
        if (o instanceof XDateTimeImpl) {
            return jdn.compareTo(((XDateTimeImpl) o).jdn);
        }
        if (o instanceof XDateImpl) {
            return jdn.compareTo(((XDateImpl) o).jdn);
        }
        return 1;
    }

    public int hashCode() {
        return jdn.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof XDateTimeImpl) {
            return jdn.equals(((XDateTimeImpl) obj).jdn);
        } else if (obj instanceof XDateImpl) {
            return jdn.equals(((XDateImpl) obj).jdn);
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

    public XDateTime toZone(ZoneId from, ZoneId to) {
        LocalDateTime q = toJavaLocalDateTime();
        ZonedDateTime w = q.atZone(from);
        ZonedDateTime e = w.withZoneSameInstant(to);
        return create(Jdn.create(e.toLocalDateTime()));
    }

    public XDateTime toZone(ZoneId to) {
        return toZone(ZoneId.systemDefault(), to);
    }

    public XDateTime clearTime() {
        return create(jdn.clearTime());
    }

    public boolean hasTime() {
        return jdn.hasTime();
    }

    public XDateTime clearMSec() {
        return create(jdn.clearMSec());
    }

    //////

    public XDate toDate() {
        return XDateImpl.create(jdn);
    }

    public boolean isToday() {
        return this.clearTime().equals(XDateTime.today());
    }

}
