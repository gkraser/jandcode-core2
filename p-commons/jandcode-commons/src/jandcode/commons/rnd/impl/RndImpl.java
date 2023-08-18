package jandcode.commons.rnd.impl;

import jandcode.commons.datetime.*;
import jandcode.commons.rnd.*;

import java.math.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class RndImpl implements Rnd {

    private Random random;

    public RndImpl() {
    }

    public RndImpl(long seed) {
        setSeed(seed);
    }

    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setSeed(long seed) {
        getRandom().setSeed(seed);
    }

    //////

    public boolean bool() {
        return num(0, 1) == 1;
    }

    public boolean bool(int chanceTrue, int chanceFalse) {
        return num(1, chanceTrue + chanceFalse) <= chanceTrue;
    }

    //////

    public int num(int min, int max) {
        int cnt = max - min + 1;
        int v = getRandom().nextInt(cnt);
        return v + min;
    }

    public long numLong(long min, long max) {
        long cnt = max - min + 1;
        long v = getRandom().nextLong(cnt);
        return v + min;
    }

    public double doub(double min, double max, int scale) {
        double d = (max - min) * getRandom().nextDouble() + min;
        if (scale == 0) {
            return (int) d;
        } else if (scale > 0) {
            BigDecimal decimal = new BigDecimal(d);
            decimal = decimal.setScale(scale, RoundingMode.DOWN);
            return decimal.doubleValue();
        }
        return d;
    }

    //////

    public Object nullValue(Object value, int chanceNull, int chanceNotNull) {
        boolean flag = bool(chanceNull, chanceNotNull);
        return flag ? null : value;
    }

    public Object nullValue(Object value) {
        return nullValue(value, 50, 50);
    }

    //////

    public char choice(CharSequence chars) {
        if (chars == null || chars.length() == 0) {
            return 0;
        }
        return chars.charAt(num(0, chars.length() - 1));
    }

    public Object choice(List items) {
        if (items == null || items.size() == 0) {
            return null;
        }
        return items.get(num(0, items.size() - 1));
    }

    public Object choice(Object[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        return items[num(0, items.length - 1)];
    }

    //////

    public String text(String chars, int min, int max, int wordSize) {
        int len = num(min, max);
        StringBuilder sb = new StringBuilder();
        int curWS = 0;
        for (int i = 0; i < len; i++) {
            char c = choice(chars);
            if (wordSize > 0 && i < len - 1 && curWS > wordSize) {
                int a = num(0, 2);
                if (a == 0) {
                    c = ' ';
                    curWS = 0;
                }
            }
            curWS++;
            sb.append(c);
        }
        return sb.toString();
    }

    //////


    public XDate date(XDate min, XDate max) {
        if (min.equals(max)) {
            return min;
        }
        int days = max.diffDays(min);
        return min.addDays(num(0, days));
    }

    public XDateTime datetime(XDateTime min, XDateTime max) {
        if (min.equals(max)) {
            return min;
        }
        long msecDiff = max.diffMSec(min);
        long msecAdd = numLong(0, msecDiff);
        return min.addMSec(msecAdd).clearMSec();
    }
    
}
