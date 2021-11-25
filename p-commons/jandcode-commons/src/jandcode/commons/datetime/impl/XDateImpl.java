package jandcode.commons.datetime.impl;

import jandcode.commons.datetime.*;

import java.time.*;

public final class XDateImpl implements XDate {

    private final Jdn jdn;

    public static XDate create(Jdn jdn) {
        return new XDateImpl(jdn);
    }

    private XDateImpl(Jdn jdn) {
        if (jdn.time != 0) {
            this.jdn = Jdn.create(jdn.jdn, 0);
        } else {
            this.jdn = jdn;
        }
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

    public XDate addDays(int days) {
        return create(jdn.addDays(days));
    }

    public XDate addMonths(int months) {
        return create(jdn.addMonths(months));
    }

    public XDate addYears(int years) {
        return create(jdn.addYears(years));
    }

    public XDate beginOfMonth() {
        return create(jdn.beginOfMonth());
    }

    public XDate endOfMonth() {
        return create(jdn.endOfMonth());
    }

    public XDate beginOfYear() {
        return create(jdn.beginOfYear());
    }

    public XDate endOfYear() {
        return create(jdn.endOfYear());
    }

    ////// system

    public int compareTo(XDate o) {
        if (!(o instanceof XDateImpl)) {
            return 1;
        }
        return jdn.compareTo(((XDateImpl) o).jdn);
    }

    public int hashCode() {
        return jdn.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof XDateImpl) {
            return jdn.equals(((XDateImpl) obj).jdn);
        } else {
            return false;
        }
    }

    //////

    public XDateTime toDateTime() {
        return XDateTimeImpl.create(jdn);
    }

}
