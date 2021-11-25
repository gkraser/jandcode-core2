package jandcode.commons.datetime.impl;

import jandcode.commons.datetime.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class XDateTimeFormatterImpl implements XDateTimeFormatter {

    private DateTimeFormatter fmt;

    public XDateTimeFormatterImpl(String pattern) {
        this.fmt = DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.SMART);
    }

    public XDateTimeFormatterImpl(String pattern, Locale locale) {
        this.fmt = DateTimeFormatter.ofPattern(pattern, locale).withResolverStyle(ResolverStyle.SMART);
    }

    public XDateTimeFormatterImpl(DateTimeFormatter fmt) {
        this.fmt = fmt;
    }

    public String toString(XDateTime d) {
        if (d == null) {
            return "";
        }
        LocalDateTime d1 = d.toJavaLocalDateTime();
        return d1.format(fmt);
    }

    protected void parse(String s, XDateTimeDecodedImpl d) {
        TemporalAccessor t = fmt.parse(s);
        //
        if (t.isSupported(ChronoField.YEAR)) {
            d.year = t.get(ChronoField.YEAR);
        }
        if (t.isSupported(ChronoField.MONTH_OF_YEAR)) {
            d.month = t.get(ChronoField.MONTH_OF_YEAR);
        } else {
            d.month = 1;
        }
        if (t.isSupported(ChronoField.DAY_OF_MONTH)) {
            d.day = t.get(ChronoField.DAY_OF_MONTH);
        } else {
            d.day = 1;
        }
        if (t.isSupported(ChronoField.HOUR_OF_DAY)) {
            d.hour = t.get(ChronoField.HOUR_OF_DAY);
        }
        if (t.isSupported(ChronoField.MINUTE_OF_HOUR)) {
            d.min = t.get(ChronoField.MINUTE_OF_HOUR);
        }
        if (t.isSupported(ChronoField.SECOND_OF_MINUTE)) {
            d.sec = t.get(ChronoField.SECOND_OF_MINUTE);
        }
        if (t.isSupported(ChronoField.NANO_OF_SECOND)) {
            d.msec = t.get(ChronoField.NANO_OF_SECOND) / DateTimeConsts.NANOSEC_IN_MSEC;
        }
    }

    public XDateTime parse(String s) {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl();
        parse(s, d);
        return new XDateTimeImpl(d);
    }

    //////

    public String toString(XDateTime2 d) {
        // todo: not implemented toString
        return null;
    }

    public XDateTime2 parseDateTime(String s) {
        // todo: not implemented parseDateTime
        return null;
    }

    public String toString(XDate d) {
        if (d == null) {
            return "";
        }
        LocalDate d1 = d.toJavaLocalDate();
        return d1.format(fmt);
    }

    public XDate parseDate(String s) {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl();
        parse(s, d);
        return XDateImpl.create(Jdn.create(d));
    }
}
