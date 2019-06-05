package jandcode.jc.impl.utils;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.jc.*;

/**
 * Форматирование ошибок для показа на консоле с ansi-расцветкой
 */
public class ErrorFormatterJc extends ErrorFormatterDefault {

    public ErrorFormatterJc(boolean showSource, boolean showStack, boolean showFullStack) {
        super(showSource, showStack, showFullStack);
    }

    public String getMessage(ErrorInfo e) {
        Ansifer ansi = UtAnsifer.getAnsifer();
        int len = JcConsts.DELIM_LEN;
        StringBuilder sb = new StringBuilder();
        sb.append(ansi.color("error-delim", UtString.delim("ERROR", "=", len))).append("\n"); //NON-NLS
        sb.append(ansi.color("error-text", e.getText())).append("\n");
        if (showSource) {
            String s = e.getTextErrorSource();
            if (s.length() > 0) {
                sb.append("\n").append(ansi.color("error-delim", UtString.delim("SOURCE", "-", len))).append("\n"); //NON-NLS
                sb.append(ansi.color("error-stack", s)).append("\n");
            }
        }
        if (showStack) {
            sb.append("\n").append(ansi.color("error-delim", UtString.delim("STACK (filtered)", "-", len))).append("\n"); //NON-NLS
            sb.append(ansi.color("error-stack", e.getTextStack(true))).append("\n");
        }
        if (showFullStack) {
            sb.append("\n").append(ansi.color("error-delim", UtString.delim("STACK (full)", "-", len))).append("\n"); //NON-NLS
            sb.append(ansi.color("error-stack", e.getTextStack(false))).append("\n");
        }
        sb.append(ansi.color("error-delim", UtString.delim(null, "=", len)));
        return sb.toString();
    }

}
