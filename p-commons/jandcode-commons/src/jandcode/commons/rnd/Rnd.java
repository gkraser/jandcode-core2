package jandcode.commons.rnd;

import jandcode.commons.*;

import java.math.*;
import java.util.*;

/**
 * Набор утилит для генерации случайных данных.
 */
@SuppressWarnings("unchecked")
public class Rnd implements IRnd {

    public static final String E_CHARS = "qwertyuiopasdfghjklzxcvbnm";
    public static final String R_CHARS = "йцукенгшщзхъфывапролджэячсмитьбю";
    public static final String N_CHARS = "0123456789";
    public static final String ER_CHARS = E_CHARS + R_CHARS;
    public static final String ERN_CHARS = E_CHARS + R_CHARS + N_CHARS;

    //////

    private Random random;
    private Map<Class, RndExt> exts;

    public Rnd() {
    }

    public Rnd(long seed) {
        setSeed(seed);
    }

    /**
     * Текущий используемый random.
     * Если явно не установлен, то будет автоматически создан.
     */
    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    /**
     * Установить другой random для использования
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    public void setSeed(long seed) {
        getRandom().setSeed(seed);
    }

    public <A extends RndExt> A getExt(Class<A> cls) {
        if (exts == null) {
            exts = new HashMap<>();
        }
        RndExt res = exts.get(cls);
        if (res == null) {
            res = createExt(cls);
            exts.put(cls, res);
        }
        return (A) res;
    }

    public <A extends RndExt> A createExt(Class<A> cls) {
        RndExt res = (RndExt) UtClass.createInst(cls);
        res.setRnd(this);
        return (A) res;
    }

    //////

    public boolean bool() {
        return num(0, 1) == 1;
    }

    public boolean bool(int t, int f) {
        return num(1, t + f) <= t;
    }

    public boolean bool(int t) {
        return bool(t, 1);
    }

    public int num(int min, int max) {
        int cnt = max - min + 1;
        int v = getRandom().nextInt(cnt);
        return v + min;
    }

    public double doub(double min, double max, int scale) {
        double d = (max - min) * getRandom().nextDouble() + min;
        if (scale == 0) {
            return (int) d;
        } else if (scale > 0) {
            BigDecimal decimal = new BigDecimal(d);
            decimal = decimal.setScale(scale, BigDecimal.ROUND_DOWN);
            return decimal.doubleValue();
        }
        return d;
    }

    public char choice(CharSequence chars) {
        if (chars == null) {
            return 0;
        }
        return chars.charAt(num(0, chars.length() - 1));
    }

    public Object choice(List items) {
        if (items == null) {
            return null;
        }
        return items.get(num(0, items.size() - 1));
    }

    public String choice(String[] items) {
        if (items == null) {
            return "";
        }
        return items[num(0, items.length - 1)];
    }

    ////// text

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

}
