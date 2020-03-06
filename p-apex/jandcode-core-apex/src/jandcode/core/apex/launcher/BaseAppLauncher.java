package jandcode.core.apex.launcher;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;
import jandcode.commons.version.*;
import jandcode.core.*;
import org.slf4j.*;

import java.util.*;

/**
 * Базовый предок для запускалок приложений.
 * Содержит инфраструктуру и сервисные методы.
 */
public abstract class BaseAppLauncher {

    private App app;
    private String appFile = AppConsts.FILE_APP_CONF;
    private String appDir;
    private CliArgs cli;
    private boolean verbose;
    private List<String> logConfigFiles = new ArrayList<>();
    private boolean logOn;

    public BaseAppLauncher() {
        this.logConfigFiles.add("_logback.xml");
        this.logConfigFiles.add("logback.xml");
        setCli(null);
    }

    ////// run

    /**
     * Запустить приложение с аргументами.
     * При ошибке - она будет показана в консоле.
     * Точка входа для использования в методе main.
     *
     * @param args аргументы, переданные в метод main.
     */
    public void run(String[] args) {
        try {
            run1(args);
        } catch (Exception e) {
            showError(e);
            System.exit(1);
        }
    }

    /**
     * Запустить приложение с аргументами.
     * При ошибке - генерируется исключение.
     * Точка входа,которая собержит собственно код приложения.
     * Может быть вызвана в тестах.
     *
     * @param args аргументы, переданные в метод main.
     */
    public void run1(String[] args) throws Exception {
        //
        UtLog.logOff();

        //
        setCli(args);

        //
        onRun();
    }

    /**
     * Реализация запуска приложения.
     * Среда настроена, логгирование отключено, аргументы командной строки доступны.
     */
    protected abstract void onRun() throws Exception;


    ////// cli

    /**
     * Аргументы командной строки, которые были переданы в метод main.
     */
    public CliArgs getCli() {
        return cli;
    }

    /**
     * Установить аргументы командной строки.
     */
    public void setCli(String[] args) {
        this.cli = UtCli.createArgs(args);
    }

    ////// app

    /**
     * Файл с конфигурацией приложения.
     * По умолчанию {@link AppConsts#FILE_APP_CONF} в каталоге приложения.
     */
    public String getAppFile() {
        return appFile;
    }

    public void setAppFile(String appFile) {
        this.appFile = appFile;
    }

    /**
     * Каталог приложения.
     */
    public String getAppDir() {
        if (app != null) {
            return app.getAppdir();
        }
        if (appDir == null) {
            this.appDir = AppConsts.resolveAppdir(UtFile.path(getAppFile()));
        }
        return appDir;
    }

    /**
     * Экземпляр приложения.
     * Приложение загружается при первом вызове.
     */
    public App getApp() throws Exception {
        if (app == null) {
            this.app = AppLoader.load(AppConsts.FILE_APP_CONF);
        }
        return app;
    }

    //////

    /**
     * true - режим с более подробным выводом сообщений
     */
    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    ////// log

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
            String f1 = UtFile.join(getAppDir(), f);
            if (UtFile.exists(f1)) {
                UtLog.logOn(f1);
                LoggerFactory.getLogger(App.class).info("load log config from: " + f1);
                return;
            }
        }

        // файлов с конфигами не найдено - включаем по умолчанию
        UtLog.logOn();
        LoggerFactory.getLogger(App.class).info("load default log config");
    }

    //////

    /**
     * Версия приложения.
     * По умолчанию - версия пакета в котором находится класс.
     */
    public String getVersion() {
        return new VersionInfo(getClass().getPackageName()).getVersion();
    }

    /**
     * Заголовок для вывода в помощи.
     * Обычно содержит имя приложения, его версию.
     */
    public String getHelpHeader() {
        return getClass().getName() + " Version " + getVersion();
    }

    //////

    /**
     * Показать ошибку в консоле
     */
    public void showError(Throwable e) {
        ErrorFormatter ef = new ErrorFormatterDefault(
                isVerbose() || isLogOn(),
                isVerbose() || isLogOn(),
                isLogOn()
        );
        System.out.println(ef.getMessage(e));
    }

}
