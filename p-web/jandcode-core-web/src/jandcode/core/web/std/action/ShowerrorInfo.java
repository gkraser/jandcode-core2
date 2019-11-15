package jandcode.core.web.std.action;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.web.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Представление ошибки в удобном виде
 */
public class ShowerrorInfo {

    public static final int DEFAULT_CODE = 500;

    private Request request;
    private Throwable exception;
    private String uri;
    private int code;
    private String text;
    private ErrorInfo errorInfo;

    private String scriptText;
    private String stackText;
    private String stackFullText;

    private boolean ajax;

    public ShowerrorInfo(Request request) {
        this.request = request;
        //
        HttpServletRequest hr = request.getHttpRequest();
        this.exception = (Throwable) hr.getAttribute("javax.servlet.error.exception");
        this.uri = (String) hr.getAttribute("javax.servlet.error.request_uri");
        Integer code = (Integer) hr.getAttribute("javax.servlet.error.status_code");
        if (code == null) {
            this.code = DEFAULT_CODE;
        } else {
            this.code = code;
        }
        this.text = (String) hr.getAttribute("javax.servlet.error.message");
        this.ajax = request.isAjax();
    }

    /**
     * request, который привел к ошибке
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Разобранная информация об ошибке.
     * Может быть null, если простая ошибка http (404 например)
     */
    public ErrorInfo getErrorInfo() {
        if (errorInfo == null) {
            if (exception != null) {
                errorInfo = UtError.createErrorInfo(exception);
            }
        }
        return errorInfo;
    }

    /**
     * true - работаем в отладочном режиме
     */
    public boolean isDebug() {
        return getRequest().getApp().getEnv().isDev();
    }

    /**
     * uri ошибки. Может быть null
     */
    public String getUri() {
        return uri;
    }

    /**
     * Код ошибки http. В случае exception: 500.
     */
    public int getCode() {
        return code;
    }

    /**
     * Текст ошибки
     */
    public String getText() {
        ErrorInfo ei = getErrorInfo();
        if (ei != null) {
            return ei.getText();
        }
        if (text == null) {
            return "";
        }
        return text;
    }

    /**
     * Текст со скриптами, где произошла ошибка.
     * Пустая строка, если нет скриптов
     */
    public String getScriptText() {
        if (!isDebug()) {
            return "";
        }
        if (scriptText == null) {
            ErrorInfo ei = getErrorInfo();
            if (ei != null) {
                scriptText = UtString.normalizeIndent(ei.getTextErrorSource());
            } else {
                scriptText = "";
            }
        }
        return scriptText;
    }

    /**
     * Текст с фильтрованным стеком ошибки.
     * Пустая строка, если нет стека
     */
    public String getStackText() {
        if (!isDebug()) {
            return "";
        }
        if (stackText == null) {
            ErrorInfo ei = getErrorInfo();
            if (ei != null) {
                stackText = UtString.normalizeIndent(ei.getTextStack(true));
            } else {
                stackText = "";
            }
        }
        return stackText;
    }

    /**
     * Текст с полным стеком ошибки.
     * Пустая строка, если нет стека
     */
    public String getStackFullText() {
        if (!isDebug()) {
            return "";
        }
        if (stackFullText == null) {
            ErrorInfo ei = getErrorInfo();
            if (ei != null) {
                stackFullText = UtString.normalizeIndent(ei.getTextStack(false));
            } else {
                stackFullText = "";
            }
        }
        return stackFullText;
    }

    /**
     * Конвертирует в map, например для передачи клиенту.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> res = new LinkedHashMap<>();

        res.put("code", getCode());
        res.put("text", getText());
        if (isDebug()) {
            res.put("debug", isDebug());

            String s;

            s = getScriptText();
            if (!UtString.empty(s)) {
                res.put("scriptText", s);
            }

            s = getStackText();
            if (!UtString.empty(s)) {
                res.put("stackText", s);
            }

            s = getStackFullText();
            if (!UtString.empty(s)) {
                res.put("stackFullText", s);
            }
        }

        return res;
    }

    /**
     * true - ошибка возникла при ajax-запросе
     */
    public boolean isAjax() {
        return ajax;
    }
}
