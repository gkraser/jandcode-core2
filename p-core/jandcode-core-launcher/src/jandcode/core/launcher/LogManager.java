package jandcode.core.launcher;

import jandcode.commons.*;
import jandcode.core.*;
import org.slf4j.*;

import java.util.*;

/**
 * Управление логированием
 */
public class LogManager {

    private String appDir;
    private List<String> logConfigFiles = new ArrayList<>();
    private boolean logOn;


    public LogManager(String appDir) {
        this.appDir = appDir;
        this.logConfigFiles.add("_logback.xml");
        this.logConfigFiles.add("logback.xml");
    }

    /**
     * Установить имя файла с конфигураций логирования
     */
    public void setLogConfigFile(String file) {
        this.logConfigFiles.clear();
        this.logConfigFiles.add(file);
    }

    /**
     * Включено ли логирование
     */
    public boolean isLogOn() {
        return logOn;
    }

    /**
     * Включить логирование.
     * Отрабатывает только один раз.
     */
    public void logOn() {
        if (this.logOn) {
            return;
        }
        this.logOn = true;

        for (String f : this.logConfigFiles) {
            String f1 = UtFile.join(appDir, f);
            if (UtFile.exists(f1)) {
                UtLog.logOn(f1);
                LoggerFactory.getLogger(Launcher.class).info("load log config from: " + f1);
                return;
            }
        }

        // файлов с конфигами не найдено - включаем по умолчанию
        UtLog.logOn();
        LoggerFactory.getLogger(App.class).info("load default log config");
    }

}
