package jandcode.commons.env;

/**
 * Реализация {@link Env} по умолчанию.
 */
public class DefaultEnv implements Env {

    private boolean prod;
    private boolean test;

    /**
     * Среда prod=true, test=false
     */
    public DefaultEnv() {
        this(true, false);
    }

    /**
     * Среда prod, test=false
     */
    public DefaultEnv(boolean prod) {
        this(prod, false);
    }

    /**
     * Среда prod, test
     */
    public DefaultEnv(boolean prod, boolean test) {
        this.prod = prod;
        this.test = test;
    }

    public boolean isDev() {
        return !prod;
    }

    public boolean isProd() {
        return prod;
    }

    public boolean isTest() {
        return test;
    }

}
