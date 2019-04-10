package jandcode.commons.datetime;

import jandcode.commons.*;
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
        XDateTimeImpl.decodeJulianDay(d, 2305448);
        assertEquals(d.toString(), "1600-01-01");
        XDateTimeImpl.decodeJulianDay(d, 2455940);
        assertEquals(d.toString(), "2012-01-13");
        XDateTimeImpl.decodeJulianDay(d, 3182030);
        assertEquals(d.toString(), "4000-01-01");
        XDateTimeImpl.decodeJulianDay(d, 2440587);
        assertEquals(d.toString(), "1969-12-31");
        XDateTimeImpl.decodeJulianDay(d, 2440588);
        assertEquals(d.toString(), "1970-01-01");
    }

    @Test
    public void test_decode_speed1() throws Exception {
        XDateTimeDecodedImpl d = new XDateTimeDecodedImpl();
        stopwatch.start();
        long n = 10000000;
        n = 100; // quick
        for (long i = 0; i < n; i++) {
            XDateTimeImpl.decodeJulianDay(d, 2455940);
            XDateTimeImpl.decodeTime(d, 2455940);
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
            XDateTimeImpl.encodeJulianDay(d);
            XDateTimeImpl.encodeTime(d);
        }
        stopwatch.stop(n);
        System.out.println(d);
    }

    @Test
    public void test_equals1() throws Exception {
        XDateTime d1 = new XDateTimeImpl(2000, 1, 2, 3, 4, 5, 6);
        XDateTime d2 = new XDateTimeImpl(2000, 1, 2, 3, 4, 5, 60);
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
        LocalDateTime d1 = LocalDateTime.of(0001, 1, 1, 0, 0, 0);
        LocalDateTime d2 = d1;
        XDateTimeFormatter f2 = new XDateTimeFormatterImpl("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // jc dates
        XDateTime m1 = new XDateTimeImpl(1, 1, 1);
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
        d = new XDateTimeImpl();
        System.out.println(d.toString());
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void test_fromJavaDateTime() throws Exception {
        XDateTime d = new XDateTimeImpl(LocalDateTime.now());
        System.out.println(d.decode());
        d = new XDateTimeImpl(LocalDate.now());
        System.out.println(d.decode());
    }

    @Test
    public void test_toJavaDateTime() throws Exception {
        XDateTime d2 = new XDateTimeImpl(2000, 1, 2, 3, 4, 5, 60);
        System.out.println(d2.toJavaLocalDateTime());
    }


    @Test
    public void test_toString_fmt1() throws Exception {
        XDateTimeFormatter f;
        XDateTime d;
        //
        f = new XDateTimeFormatterImpl("dd/MMMM/yy");
        System.out.println(f.toString(new XDateTimeImpl()));
        //
        f = new XDateTimeFormatterImpl("dd/MMMM/yy", Locale.US);
        System.out.println(f.toString(new XDateTimeImpl()));
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy");
        d = f.parse("23/10/1994");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy mm SSS");
        d = f.parse("23/10/1994 25 001");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("dd/MM/yyyy ss");
        d = f.parse("23/10/1994 25");
        System.out.println(d);
        //
        f = new XDateTimeFormatterImpl("yyyy-MM-dd HH:mm:ss.S[SS]");
        d = f.parse("2012-11-30 22:23:24.1");
        System.out.println(d);


    }

    @Test
    public void test_fromString_fmt1() throws Exception {
        XDateTime d;
        //
        d = new XDateTimeImpl("1994-12-23");
        System.out.println(d);
        //
        d = new XDateTimeImpl("1994-12-23T12:13:14");
        System.out.println(d);
        //
        d = new XDateTimeImpl("1994-12-23T12:13:14.567");
        System.out.println(d);
        //
    }

    @Test
    public void test_toZone() throws Exception {
        XDateTime t = new XDateTimeImpl();
        System.out.println(t.toZone(ZoneId.of("UTC")));
    }

    @Test
    public void test_mills() throws Exception {
        long a = System.currentTimeMillis();
        System.out.println(new XDateTimeImpl(a));
        System.out.println(new XDateTimeImpl(a, ZoneId.of("UTC")));
    }

    @Test
    public void test_fromJavaOldDate() throws Exception {
        Date z = new Date();
        System.out.println(z);
        XDateTime d = new XDateTimeImpl(z);
        System.out.println(d);
    }

    //////

    @Test
    public void test_toJava() throws Exception {
        XDateTime d = UtDateTime.create("2010-11-12T13:14:15");
        assertEquals(d.toJavaLocalDateTime().toString(), "2010-11-12T13:14:15");
        assertEquals(d.toJavaZonedDateTime().toString().startsWith("2010-11-12T13:14:15+"), true);
        assertEquals(d.toJavaZonedDateTime(ZoneId.of("UTC")).toString(), "2010-11-12T13:14:15Z[UTC]");
        assertEquals(d.toJavaDate().toString().startsWith("Fri Nov 12 13:14:15 "), true);
        assertEquals(d.toJavaDate(ZoneId.of("UTC")).toString().startsWith("Fri Nov 12 19:14:15 "), true);
    }

}
