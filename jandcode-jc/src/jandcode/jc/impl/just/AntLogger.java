package jandcode.jc.impl.just;

import jandcode.commons.ansifer.*;
import org.apache.tools.ant.*;

import java.io.*;
import java.util.regex.*;

/**
 * logger для ant с подсветкой ansi
 */
public class AntLogger extends DefaultLogger {

    private Pattern p1 = Pattern.compile("(\\[.+?\\])(.+)", Pattern.MULTILINE | Pattern.DOTALL);

    protected void printMessage(String message, PrintStream stream, int priority) {
        Matcher m = p1.matcher(message);
        if (m.find()) {
            String clr = "ant-info";
            if (priority == Project.MSG_ERR) {
                clr = "ant-error";
            }
            if (priority == Project.MSG_WARN) {
                clr = "ant-warn";
            }
            message = m.replaceFirst(Ansifer.getInst().style(clr + "-pfx", "$1") + Ansifer.getInst().style(clr, "$2"));
        } else {
            message = Ansifer.getInst().style("ant-info", message);
        }
        super.printMessage(message, stream, priority);
    }
}
