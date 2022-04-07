package jandcode.commons.log;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.joran.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import org.slf4j.*;

import java.io.*;

/**
 * Конфигуратор для logback.
 */
public class LogbackConfigurator {

    private String logOffConfig = "" +
            "<configuration>\n" +
            "    <appender name=\"jc.console\" class=\"jandcode.commons.log.SimpleConsoleAppender\">\n" +
            "    </appender>\n" +
            "    <root level=\"off\">\n" +
            "        <appender-ref ref=\"jc.console\"/>\n" +
            "    </root>\n" +
            "    <logger name=\"jc.console\" level=\"info\"/>\n" +
            "</configuration>"; //NON-NLS

    private String logOnConfig = "" +
            "<configuration>\n" +
            "    <appender name=\"CONSOLE\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" +
            "        <encoder>\n" +
            "            <pattern>%d{HH:mm:ss,SSS} %-6p %-15c{1} - %m%n</pattern>\n" +
            "        </encoder>\n" +
            "    </appender>\n" +
            "    <root level=\"info\">\n" +
            "        <appender-ref ref=\"CONSOLE\"/>\n" +
            "    </root>\n" +
            "    <logger name=\"org.apache\" level=\"OFF\"/>\n" +
            "</configuration>\n"; //NON-NLS

    /**
     * Последний использованный конфиг
     */
    private String lastConfig;

    private Boolean checkLogbackResult;

    /**
     * Проверка, что Logback имеется
     */
    private Boolean checkLogback() {
        if (checkLogbackResult == null) {
            try {
                UtClass.getClass("ch.qos.logback.classic.joran.JoranConfigurator");
                checkLogbackResult = true;
            } catch (Exception e) {
                checkLogbackResult = false;
            }
        }
        return checkLogbackResult;
    }

    /**
     * Загрузить указанный конфиг
     *
     * @param cfg либо имя локального файла, либо строка с xml-конфигурацией.
     */
    public void applyConfig(String cfg) {
        if (!checkLogback()) {
            return;
        }
        if (UtString.empty(cfg)) {
            cfg = logOffConfig;
        }
        if (cfg.equals(lastConfig)) {
            return; // уже использован такой
        }

        //
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();

        try {
            if (cfg.startsWith("<")) {
                // xml
                try (InputStream stream = new ByteArrayInputStream(cfg.getBytes(UtString.UTF8))) {
                    configurator.doConfigure(stream);
                }
            } else {
                // filename
                configurator.doConfigure(new File(cfg));
            }

            lastConfig = cfg;

        } catch (Exception e) {
            throw new XErrorWrap(e);
        }

    }

    /**
     * Конфиг для отключенного логгирования по умолчанию
     */
    public String getLogOffConfig() {
        return logOffConfig;
    }

    /**
     * Конфиг для включенного логгирования по умолчанию
     */
    public String getLogOnConfig() {
        return logOnConfig;
    }

}
