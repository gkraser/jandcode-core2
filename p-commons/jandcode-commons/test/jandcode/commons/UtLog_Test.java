package jandcode.commons;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;

public class UtLog_Test extends Utils_Test {

    @Test
    public void logConsole() throws Exception {
        UtLog.logOff();
        //
        Logger log = UtLog.getLogConsole();
        Logger log2 = LoggerFactory.getLogger(getClass().getName());
        // должно работать даже при отключенном логировании
        log.info("hello привет!");
        // это НЕ должно работать при отключенном логировании
        log2.info("hello привет! this not show");
    }


}
