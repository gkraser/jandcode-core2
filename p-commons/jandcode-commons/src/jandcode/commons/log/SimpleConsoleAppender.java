package jandcode.commons.log;

import ch.qos.logback.classic.spi.*;
import ch.qos.logback.core.*;

/**
 * Простеший logback appender. Просто выподит сообщение на консоль.
 */
public class SimpleConsoleAppender extends AppenderBase<ILoggingEvent> {

    protected void append(ILoggingEvent ev) {
        System.out.println(ev.getMessage());
    }

}
