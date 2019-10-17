package jandcode.core.web.logger;

import jandcode.commons.*;
import jandcode.commons.error.impl.*;
import jandcode.core.web.*;
import org.slf4j.*;

import javax.servlet.http.*;


/**
 * Логгер для запроса для debug-режима.
 * Выводит в log для класса {@link Request}
 * Требует некоторой настройки в log:
 * <pre>{@code
 * PATTERN=%d{HH:mm:ss,SSS} %-5p %-15c{1} - %X{request}%m%n
 * }</pre>
 * Здесь %X{request} - это отступ и идентификатор запроса.
 */
public class RequestLoggerDebug implements RequestLogger {

    protected static Logger log = LoggerFactory.getLogger(Request.class);

    private int numRequest = 0;
    private byte[] slots = new byte[255];

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
        int thisLevel = 0;
        int thisNumRequest = 0;
        synchronized (this) {
            numRequest++;
            if (numRequest > 0xFFFF) {
                numRequest = 1;
            }
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] == 0) {
                    slots[i] = 1;
                    thisLevel = i;
                    break;
                }
            }
            thisNumRequest = numRequest;
        }

        MDC.put("request.level", UtString.toString(thisLevel));
        String sNumRequest = UtString.padLeft(Integer.toHexString(thisNumRequest), 4, '0');
        String indent = UtString.repeat("      ", thisLevel);

        MDC.put("request", indent);
        log.info("------");

        MDC.put("request", indent + "{" + sNumRequest + "} => ");
        log.info("START : " + toString(request));

    }

    public void logStop(Request request) {
        if (!log.isErrorEnabled()) {
            return;
        }
        //
        int thisLevel = UtCnv.toInt(MDC.get("request.level"));
        synchronized (this) {
            slots[thisLevel] = 0;
        }

        long tmms = System.currentTimeMillis() - request.getStartTime();
        String tm = String.format("%.3f sec.", tmms / 1000.0);

        log.info("STOP  : " + tm);

        MDC.remove("request.level");
        MDC.remove("request");
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
