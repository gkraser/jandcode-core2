package jandcode.jc.impl.log;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.spi.*;
import ch.qos.logback.core.*;
import jandcode.commons.ansifer.*;

public class LogbackJcConsoleAppender extends AppenderBase<ILoggingEvent> {

    private static String pfxInfo = " (i) ";
    private static String pfxDebg = " [D] ";
    private static String pfxWarn = " [W] ";
    private static String pfxErro = " [E] ";

    protected void out(Object msg, String pfx, String msgColor, String pfxColor) {
        System.out.print(Ansifer.getInst().style(pfxColor, pfx));
        System.out.println(Ansifer.getInst().style(msgColor, "" + msg));
    }

    protected void append(ILoggingEvent ev) {
        String msgColor = "log-info";
        String pfxColor = "log-info-pfx";
        String pfx = pfxInfo;

        if (ev.getLevel() == Level.DEBUG) {
            pfx = pfxDebg;
            msgColor = "log-debug";
            pfxColor = "log-debug-pfx";

        } else if (ev.getLevel() == Level.WARN) {
            pfx = pfxWarn;
            msgColor = "log-warn";
            pfxColor = "log-warn-pfx";

        } else if (ev.getLevel() == Level.ERROR) {
            pfx = pfxErro;
            msgColor = "log-warn";
            pfxColor = "log-warn-pfx";
        }

        out(ev.getMessage(), pfx, msgColor, pfxColor);
    }

}
