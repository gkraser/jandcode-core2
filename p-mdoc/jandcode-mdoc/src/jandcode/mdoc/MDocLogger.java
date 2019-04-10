package jandcode.mdoc;

import jandcode.commons.*;
import jandcode.mdoc.builder.*;
import org.slf4j.*;

/**
 * Логгер для mdoc
 */
public class MDocLogger {

    /**
     * Логгер для jc
     */
    public static Logger jcLog = LoggerFactory.getLogger("jc.console");

    private static MDocLogger _inst = new MDocLogger();

    public static MDocLogger getInst() {
        return _inst;
    }

    //////

    public void info(String msg) {
        info(null, msg);
    }

    public void warn(String msg) {
        warn(null, msg);
    }

    public void warn(Object marker, String msg) {
        jcLog.warn(makeMsg(marker, msg));
    }

    public void info(Object marker, String msg) {
        jcLog.info(makeMsg(marker, msg));
    }

    //////

    /**
     * Формирование маркера для текста сообщения
     *
     * @param marker какой-то объект, связанный с сообщением
     */
    private String markerToText(Object marker) {
        if (marker == null) {
            return "";
        } else if (marker instanceof OutFile) {
            OutFile z = (OutFile) marker;
            if (z.getTopic() != null && z.getTopic().getSourceFile() != null) {
                return z.getTopic().getSourceFile().getPath();
            }
            if (z.getSourceFile() != null) {
                return z.getSourceFile().getPath();
            }
            return z.getPath();
        } else {
            return marker.toString();
        }
    }

    /**
     * Создать сообщение с маркером
     */
    private String makeMsg(Object marker, String msg) {
        if (msg == null) {
            msg = "";
        }
        String m = markerToText(marker);
        if (UtString.empty(m)) {
            return msg;
        }
        return msg + " (файл: " + m + ")";
    }

}
