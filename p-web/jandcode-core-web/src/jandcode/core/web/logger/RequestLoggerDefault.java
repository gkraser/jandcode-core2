package jandcode.core.web.logger;

import jandcode.commons.*;
import jandcode.commons.error.impl.*;
import jandcode.core.web.*;
import org.slf4j.*;

import javax.servlet.http.*;


/**
 * Логгер для запроса по умолчанию.
 * Выводит в log для класса {@link Request}
 */
public class RequestLoggerDefault implements RequestLogger {

    protected static Logger log = LoggerFactory.getLogger(Request.class);

    public String toString(Request request) {
        HttpServletRequest h = request.getHttpRequest();
        String s = h.getRequestURI();
        String qs = request.getHttpRequest().getQueryString();
        if (!UtString.empty(qs)) {
            s = s + "?" + qs;
        }
        return s;
    }

    public void logStart(Request request) {
        if (!log.isErrorEnabled()) {
            return;
        }
        log.info("START : " + toString(request));
    }

    public void logStop(Request request) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        long tmms = System.currentTimeMillis() - request.getStartTime();
        String tm = String.format("%.3f sec.", tmms / 1000.0);

        log.info("STOP  : " + tm);
    }

    public void logError(Request request, Throwable e) {
        if (e == null || !log.isErrorEnabled()) {
            return;
        }
        //
        String msg = new ErrorFormatterDefault(true, true, false).
                getMessage(UtError.createErrorInfo(e));
        log.error("ERROR =>\n\n" + msg + "\n");
    }

    public void logHttpError(Request request, HttpError e) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        String s = "" + e.getCode();
        if (!UtString.empty(e.getMessage())) {
            s = s + " - " + e.getMessage();
        }
        log.info("sendError: " + s);
    }

    public void logHttpRedirect(Request request, HttpRedirect e) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        log.info("sendRedirect: " + e.getUrl());
    }

}
