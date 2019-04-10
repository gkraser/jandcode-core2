package jandcode.commons.error.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;

/**
 * Стандартное форматирование ошибок для показа на консоле
 */
public class ErrorFormatterDefault implements ErrorFormatter {

    protected boolean showSource = false;
    protected boolean showStack = false;
    protected boolean showFullStack = false;

    public ErrorFormatterDefault() {
    }

    public ErrorFormatterDefault(boolean showSource, boolean showStack, boolean showFullStack) {
        this.showSource = showSource;
        this.showStack = showStack;
        this.showFullStack = showFullStack;
    }

    public void setShowSource(boolean showSource) {
        this.showSource = showSource;
    }

    public void setShowStack(boolean showStack) {
        this.showStack = showStack;
    }

    public void setShowFullStack(boolean showFullStack) {
        this.showFullStack = showFullStack;
    }

    public String getMessage(ErrorInfo e) {
        int len = 76;
        StringBuilder sb = new StringBuilder();
        sb.append(UtString.delim("ERROR", "=", len)).append("\n"); //NON-NLS
        sb.append(e.getText()).append("\n");
        if (showSource) {
            String s = e.getTextErrorSource();
            if (s.length() > 0) {
                sb.append("\n").append(UtString.delim("SOURCE", "-", len)).append("\n"); //NON-NLS
                sb.append(s).append("\n");
            }
        }
        if (showStack) {
            sb.append("\n").append(UtString.delim("STACK (filtered)", "-", len)).append("\n"); //NON-NLS
            sb.append(e.getTextStack(true)).append("\n");
        }
        if (showFullStack) {
            sb.append("\n").append(UtString.delim("STACK (full)", "-", len)).append("\n"); //NON-NLS
            sb.append(e.getTextStack(false)).append("\n");
        }
        sb.append(UtString.delim(null, "=", len));
        return sb.toString();
    }

    public String getMessage(Throwable e) {
        ErrorInfo ei = UtError.createErrorInfo(e);
        return getMessage(ei);
    }

}
