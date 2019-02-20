package jandcode.commons.rnd;

import java.util.*;

/**
 * Расширение для {@link Rnd}.
 */
public abstract class RndExt implements IRnd {

    private Rnd rnd;

    public Rnd getRnd() {
        if (rnd == null) {
            rnd = new Rnd();
        }
        return rnd;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    ////// IRnd

    public void setSeed(long seed) {
        getRnd().setSeed(seed);
    }

    public <A extends RndExt> A getExt(Class<A> cls) {
        return getRnd().getExt(cls);
    }

    public <A extends RndExt> A createExt(Class<A> cls) {
        return getRnd().createExt(cls);
    }

    public boolean bool() {
        return getRnd().bool();
    }

    public boolean bool(int t, int f) {
        return getRnd().bool(t, f);
    }

    public boolean bool(int t) {
        return getRnd().bool(t);
    }

    public int num(int min, int max) {
        return getRnd().num(min, max);
    }

    public double doub(double min, double max, int scale) {
        return getRnd().doub(min, max, scale);
    }

    public char choice(CharSequence chars) {
        return getRnd().choice(chars);
    }

    public Object choice(List items) {
        return getRnd().choice(items);
    }

    public String choice(String[] items) {
        return getRnd().choice(items);
    }

    public String text(String chars, int min, int max, int wordSize) {
        return getRnd().text(chars, min, max, wordSize);
    }

}
