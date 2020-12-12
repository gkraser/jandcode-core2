package jandcode.commons.datetime.impl;


import jandcode.commons.datetime.*;

public class XDateTimeDecodedImpl implements XDateTimeDecoded {

    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int min;
    protected int sec;
    protected int msec;
    protected int dow;

    public XDateTimeDecodedImpl() {
    }

    public XDateTimeDecodedImpl(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public XDateTimeDecodedImpl(int year, int month, int day, int hour, int min, int sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public XDateTimeDecodedImpl(int year, int month, int day, int hour, int min, int sec, int msec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.msec = msec;
    }

    //////

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getMsec() {
        return msec;
    }

    public void setMsec(int msec) {
        this.msec = msec;
    }

    public int getDow() {
        return dow;
    }

    public void setDow(int dow) {
        this.dow = dow;
    }

    //////

    public boolean hasTime() {
        return hour != 0 || min != 0 || sec != 0 || msec != 0;
    }

    //////

    private static char[] nums = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * В iso-формат. Быстро.
     * Если нет милисекунд или времени,
     * они не попадут в выходную строку.
     */
    public String toString() {
        //
        int v;
        int len = 23;

        if (hasTime()) {
            if (msec == 0) {
                len = 19;
            }
        } else {
            len = 10;
        }
        char[] z = new char[len];

        //
        v = year % 10000;
        z[0] = nums[v / 1000];
        v = v % 1000;
        z[1] = nums[v / 100];
        v = v % 100;
        z[2] = nums[v / 10];
        z[3] = nums[v % 10];
        z[4] = '-';

        //
        v = month % 100;
        z[5] = nums[v / 10];
        z[6] = nums[v % 10];
        z[7] = '-';

        //
        v = day % 100;
        z[8] = nums[v / 10];
        z[9] = nums[v % 10];

        if (len > 10) {
            z[10] = 'T';

            //
            v = hour % 100;
            z[11] = nums[v / 10];
            z[12] = nums[v % 10];
            z[13] = ':';

            //
            v = min % 100;
            z[14] = nums[v / 10];
            z[15] = nums[v % 10];
            z[16] = ':';

            //
            v = sec % 100;
            z[17] = nums[v / 10];
            z[18] = nums[v % 10];

            if (len > 19) {
                z[19] = '.';

                //
                v = msec % 1000;
                z[20] = nums[v / 100];
                v = v % 100;
                z[21] = nums[v / 10];
                z[22] = nums[v % 10];
            }

        }

        return new String(z);
    }

    /**
     * Разбор строки iso-формата
     */
    public void parse(String s) {
        if (s == null || s.length() == 0) {
            this.day = 1;
            this.month = 1;
            return;
        }
        //

        if (s.length() <= 10) {
            ((XDateTimeFormatterImpl) XDateTimeFormatter.ISO_DATE).parse(s, this);
        } else {
            ((XDateTimeFormatterImpl) XDateTimeFormatter.ISO_DATE_TIME).parse(s, this);
        }

    }


}
