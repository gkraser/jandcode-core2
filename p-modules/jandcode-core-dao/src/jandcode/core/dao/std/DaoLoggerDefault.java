package jandcode.core.dao.std;

import jandcode.core.dao.*;
import org.slf4j.*;

public class DaoLoggerDefault implements DaoLogger {

    protected static Logger log = LoggerFactory.getLogger(Dao.class);

    public String toString(DaoFilterParams p) {
        DaoMethodDef m = p.getDaoMethodDef();
        return m.getMethod().toString();
    }

    public void logStart(DaoFilterParams p) {
        if (!log.isErrorEnabled()) {
            return;
        }
        log.info("DAO start: " + toString(p));
    }

    public void logStop(DaoFilterParams p) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        long tmms = System.currentTimeMillis() - p.getStartTime();
        String tm = String.format("%.3f sec", tmms / 1000.0);

        log.info("DAO stop [" + tm + "]: " + toString(p));
    }

}
