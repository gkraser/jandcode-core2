package jandcode.jc.impl.log;

import jandcode.jc.*;
import org.slf4j.*;

public class LogImpl implements Log {

    protected static Logger log = LoggerFactory.getLogger(JcConsts.LOGGER_CONSOLE);

    private Ctx ctx;
    private boolean verbose;

    public LogImpl(Ctx ctx) {
        this.ctx = ctx;
    }

    public void info(Object msg) {
        log.info("" + msg);

    }

    public void debug(Object msg) {
        log.debug("" + msg);

    }

    public void warn(Object msg) {
        log.warn("" + msg);
    }

    ///

    public void call(Object msg) {
        info(msg);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
