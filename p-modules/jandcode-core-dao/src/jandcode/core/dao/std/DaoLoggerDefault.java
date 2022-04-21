package jandcode.core.dao.std;

import jandcode.core.dao.*;
import org.slf4j.*;

public class DaoLoggerDefault implements DaoLogger {

    protected static Logger log = LoggerFactory.getLogger(Dao.class);

    public String toString(DaoContext ctx) {
        DaoMethodDef m = ctx.getDaoMethodDef();
        return m.getClassDef().getCls().getSimpleName() + "." + m.getMethod().getName() + " => " + m.getMethod().toString();
    }

    public void logStart(DaoContext ctx) {
        if (!log.isErrorEnabled()) {
            return;
        }
        log.info("DAO start: " + toString(ctx));
    }

    public void logStop(DaoContext ctx) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        long tmms = System.currentTimeMillis() - ctx.getStartTime();
        String tm = String.format("%.3f sec", tmms / 1000.0);

        log.info("DAO stop [" + tm + "]: " + toString(ctx));
    }

}
