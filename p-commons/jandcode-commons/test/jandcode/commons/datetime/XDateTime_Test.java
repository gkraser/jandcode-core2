package jandcode.commons.datetime;

import jandcode.commons.datetime.impl.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class XDateTime_Test extends Utils_Test {

    @Test
    public void test_decodeJulianDay_1() throws Exception {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl();
        Jdn.decodeJulianDay(d, 2305448);
        assertEquals(d.toString(), "1600-01-01");
        Jdn.decodeJulianDay(d, 2455940);
        assertEquals(d.toString(), "2012-01-13");
        Jdn.decodeJulianDay(d, 3182030);
        assertEquals(d.toString(), "4000-01-01");
        Jdn.decodeJulianDay(d, 2440587);
        assertEquals(d.toString(), "1969-12-31");
        Jdn.decodeJulianDay(d, 2440588);
        assertEquals(d.toString(), "1970-01-01");
    }

    @Test
    public void test_decode_speed1() throws Exception {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl();
        stopwatch.start();
        long n = 10000000;
        n = 100; // quick
        for (long i = 0; i < n; i++) {
            Jdn.decodeJulianDay(d, 2455940);
            Jdn.decodeTime(d, 2455940);
        }
        stopwatch.stop(n);
        System.out.println(d);
    }

    @Test
    public void test_encode_speed1() throws Exception {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl(2000, 1, 2, 3, 4, 5, 6);
        stopwatch.start();
        long n = 10000000;
        n = 100; // quick
        for (long i = 0; i < n; i++) {
            Jdn.encodeJulianDay(d);
            Jdn.encodeTime(d);
        }
        stopwatch.stop(n);
        System.out.println(d);
    }

    @Test
    public void test_equals1() throws Exception {
        XDateTime d1 = XDateTime.create(2000, 1, 2, 3, 4, 5, 6);
        XDateTime d2 = XDateTime.create(2000, 1, 2, 3, 4, 5, 60);
        System.out.println("compare: " + d1.toString() + " - " + d2.clearMSec().toString());
        assertEquals(d1.equals(d2), false);
        assertEquals(d1.clearMSec().equals(d2.clearMSec()), true);
    }

    @Test
    public void test_decodedToString_speed1() throws Exception {
        XDateTimeDecoded d = new XDateTimeDecodedImpl(2000, 1, 2, 3, 4, 5, 6);
        stopwatch.start();
        long n = 1000000;//0;
        n = 100; // quick
        for (long i = 0; i < n; i++) {
            String s = d.toString();
        }
        stopwatch.stop(n);
        System.out.println(d);
    }


    @Test
    public void test_checkAllDates() throws Exception {
        // java 8 dates
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime d1 = LocalDateTime.of(1, 1, 1, 5, 6, 7);
        LocalDateTime d2 = d1;
        XDateTimeFormatter f2 = new XDateTimeFormatterImpl("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // jc dates
        XDateTime m1 = XDateTime.create(1, 1, 1, 5, 6, 7);
        XDateTime m2 = m1;


        int cntDays = 1000000;
        cntDays = 100; // quick
        stopwatch.start();
        for (int i = 0; i < cntDays; i++) {
            // java 8
            d2 = d1.plusDays(i);
            String d2s = f.format(d2);

            // jc
            m2 = m1.addDays(i);
            String m2s = m2.toString(f2);

            assertEquals(d2s, m2s);
        }
        stopwatch.stop((long) cntDays);

        System.out.println(f.format(d2));
        System.out.println(m2.toString());
    }

    @Test
    public void test_now() throws Exception {
        XDateTime d;
        d = XDateTime.now();
        System.out.println(d.toString());
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void test_fromJavaDateTime() throws Exception {
        XDateTime d = XDateTime.create(LocalDateTime.now());
        System.out.println(d.decode());
        d = XDateTime.create(LocalDate.now());
        System.out.println(d.decode());
    }

    @Test
    public void test_toJavaDateTime() throws Exception {
        XDateTime d2 = XDateTime.create(2000, 1, 2, 3, 4, 5, 60);
        System.out.println(d2.toJavaLocalDateTime());
    }


    @Test
    public void test_toString_fmt1() throws Exception {
        XDateTimeFormatter f;
        XDateTime d;
        //
        f = new XDateTimeFormatterImpl("dd/MMMM/yy");
        System.out.println(f.toString(XDateTime.now()));
        //
        f = new XDateTimeFormatterImpl("dd/MMMM/yy", Locale.US);
        System.out.println(f.toString(XDateTime.now()));
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy");
        d = f.parseDateTime("23/10/1994");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy mm SSS");
        d = f.parseDateTime("23/10/1994 25 001");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy ss");
        d = f.parseDateTime("23/10/1994 25");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("yyyy-MM-dd HH:mm:ss.S[SS]");
        d = f.parseDateTime("2012-11-30 22:23:24.1");
        System.out.println(d);


    }

    @Test
    public void test_fromString_fmt1() throws Exception {
        XDateTime d;
        //
        d = XDateTime.create("1994-12-23");
        System.out.println(d);
        //
        d = XDateTime.create("1994-12-23T12:13:14");
        System.out.println(d);
        //
        d = XDateTime.create("1994-12-23T12:13:14.567");
        System.out.println(d);
        //
        d = XDateTime.create("2020-11-12T00:00:00.000Z");
        System.out.println(d);
        //
        d = XDateTime.create("2020-11-12T06:00:00.000Z");
        System.out.println(d);
        //
        d = XDateTime.create("2020-11-12T06:00:00.000");
        System.out.println(d);
        //
    }

    @Test
    public void test_toZone() throws Exception {
        XDateTime t = XDateTime.now();
        System.out.println(t.toZone(ZoneId.of("UTC")));
    }

    @Test
    public void test_mills() throws Exception {
        long a = System.currentTimeMillis();
        System.out.println(XDateTime.create(a));
        System.out.println(XDateTime.create(a, ZoneId.of("UTC")));
    }

    @Test
    public void test_fromJavaOldDate() throws Exception {
        Date z = new Date();
        System.out.println(z);
        XDateTime d = XDateTime.create(z);
        System.out.println(d);
    }

    //////

    @Test
    public void test_toJava() throws Exception {
        XDateTime d = XDateTime.create("2010-11-12T13:14:15");
        assertEquals(d.toJavaLocalDateTime().toString(), "2010-11-12T13:14:15");
        assertEquals(d.toJavaZonedDateTime().toString().startsWith("2010-11-12T13:14:15+"), true);
        assertEquals(d.toJavaZonedDateTime(ZoneId.of("UTC")).toString(), "2010-11-12T13:14:15Z[UTC]");
        assertEquals(d.toJavaDate().toString().startsWith("Fri Nov 12 13:14:15 "), true);
        assertEquals(d.toJavaDate(ZoneId.of("UTC")).toString().startsWith("Fri Nov 12 19:14:15 "), true);
    }

    //////

    @Test
    public void addMonths_1() throws Exception {
        XDateTime d1 = XDateTime.create("2011-11-30");
        XDateTime d2;

        // +
        d2 = d1.addMonths(13);
        assertEquals(d2.toString(), "2012-12-30");

        d2 = d1.addMonths(2);
        assertEquals(d2.toString(), "2012-01-30");

        d2 = d1.addMonths(3);
        assertEquals(d2.toString(), "2012-02-29");

        d2 = d1.addMonths(15);
        assertEquals(d2.toString(), "2013-02-28");

        d2 = d1.addMonths(12);
        assertEquals(d2.toString(), "2012-11-30");

        d2 = d1.addMonths(14);
        assertEquals(d2.toString(), "2013-01-30");

        d2 = d1.addMonths(24);
        assertEquals(d2.toString(), "2013-11-30");

        // -
        d2 = d1.addMonths(-13);
        assertEquals(d2.toString(), "2010-10-30");

        d2 = d1.addMonths(-11);
        assertEquals(d2.toString(), "2010-12-30");

        d2 = d1.addMonths(-2);
        assertEquals(d2.toString(), "2011-09-30");

        d2 = d1.addMonths(-12);
        assertEquals(d2.toString(), "2010-11-30");

        d2 = d1.addMonths(-14);
        assertEquals(d2.toString(), "2010-09-30");

        d2 = d1.addMonths(-24);
        assertEquals(d2.toString(), "2009-11-30");

        d2 = d1.addMonths(-35);
        assertEquals(d2.toString(), "2008-12-30");

    }

    private void cmp_addMonth_plus() {
        int startY = 1999;
        for (int y = 0; y <= 200; y++) {
            for (int m = 0; m < 12; m++) {
                XDateTime d = XDateTime.create(startY + y, m + 1, 1);
                int months = y * 12 + m;
                XDateTime startDt = XDateTime.create(startY, 1, 1);
                XDateTime d2 = startDt.addMonths(months);
                //System.out.println("" + months + " " + d + " = " + d2);
                assertEquals(d2.toString(), d.toString());
            }
        }
    }

    private void cmp_addMonth_minus() {
        int startY = 1999;
        for (int y = 0; y <= 200; y++) {
            for (int m = 0; m < 12; m++) {
                XDateTime d = XDateTime.create(startY - y, 12 - m, 1);
                int months = y * 12 + m;
                XDateTime startDt = XDateTime.create(startY, 12, 1);
                XDateTime d2 = startDt.addMonths(-months);
                //System.out.println("" + months + " " + d + " = " + d2);
                assertEquals(d2.toString(), d.toString());
            }
        }
    }

    @Test
    public void addMonths_2_all() throws Exception {
        cmp_addMonth_plus();
        cmp_addMonth_minus();
    }

    @Test
    public void addYear_1() throws Exception {
        XDateTime d1 = XDateTime.create("2004-02-29");
        XDateTime d2;

        d2 = d1.addYears(1);
        assertEquals(d2.toString(), "2005-02-28");

        d2 = d1.addYears(-1);
        assertEquals(d2.toString(), "2003-02-28");
    }

    @Test
    public void beginOfMonth() throws Exception {
        assertEquals(XDateTime.create("1999-12-25").beginOfMonth().toString(), "1999-12-01");
        assertEquals(XDateTime.create("1999-12-25T12:13:14").beginOfMonth().toString(), "1999-12-01T12:13:14");
    }

    @Test
    public void endOfMonth() throws Exception {
        assertEquals(XDateTime.create("2004-02-25").endOfMonth().toString(), "2004-02-29");
        assertEquals(XDateTime.create("1999-12-25T12:13:14").endOfMonth().toString(), "1999-12-31T12:13:14");
    }

    @Test
    public void beginOfYear() throws Exception {
        assertEquals(XDateTime.create("1999-12-25").beginOfYear().toString(), "1999-01-01");
        assertEquals(XDateTime.create("1999-12-25T12:13:14").beginOfYear().toString(), "1999-01-01T12:13:14");
    }

    @Test
    public void endOfYear() throws Exception {
        assertEquals(XDateTime.create("2004-02-25").endOfYear().toString(), "2004-12-31");
        assertEquals(XDateTime.create("1999-12-25T12:13:14").endOfYear().toString(), "1999-12-31T12:13:14");
    }

    @Test
    public void compare_date_and_datetime() throws Exception {
        XDateTime dt = XDateTime.create(2011, 11, 22);
        XDate d = XDate.create(2011, 11, 22);

        assertEquals(dt, d);
        assertEquals(dt.compareTo(d), 0);
    }

    @Test
    public void is_today() throws Exception {
        XDateTime dt = XDateTime.now();
        assertTrue(dt.isToday());

        XDate d = XDate.today();
        assertTrue(d.isToday());
    }

}
