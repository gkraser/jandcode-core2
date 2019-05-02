package jandcode.core.test;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.event.*;
import jandcode.commons.stopwatch.*;
import jandcode.commons.test.*;
import jandcode.core.*;
import jandcode.core.impl.*;

import java.util.*;

/**
 * Расширение для тестов: поддержка приложения
 */
public class AppTestSvc extends BaseTestSvc implements App {

    public static String FILE_TEST_CONF = "test.cfx";

    private static HashMap<String, App> _cacheApp = new HashMap<>();

    protected App app;

    protected UtilsTestSvc utils;
    protected StopwatchTestSvc stopwatch;

    //////

    public void setUp() throws Exception {
        this.utils = testSvc(UtilsTestSvc.class);
        this.stopwatch = testSvc(StopwatchTestSvc.class);
        // активируем приложение
        activateApp();
    }

    public void activateApp(String appConfFile) throws Exception {
        App res;
        String pt = utils.getTestPath();
        String ptn = UtFile.join(pt, appConfFile);
        res = _cacheApp.get(ptn);
        if (res == null) {
            String fn = UtFile.findFileUp(appConfFile, pt);
            if (fn == null) {
                throw new XError(String.format("***** Не найден файл %s начиная с каталога %s и вверх", appConfFile, pt));
            } else {
                res = _cacheApp.get(fn);
                if (res == null) {
                    Stopwatch sw = stopwatch.get("load-app");
                    sw.start();
                    System.out.println("***** [load app: " + fn + " ]***************");
                    res = new AppImpl(fn, true);
                    _cacheApp.put(fn, res);
                    _cacheApp.put(ptn, res);
                    sw.stop();
                }
            }
        }
        app = res;
    }

    /**
     * Активизация контекста по умолчанию: начиная с каталога тестов и вверх ищется файл
     * {@link AppTestSvc#FILE_TEST_CONF}
     */
    public void activateApp() throws Exception {
        activateApp(FILE_TEST_CONF);
    }

    /**
     * Записать текущее состояние {@link IConfLink#getConf()}
     */
    public void saveAppConf() throws Exception {
        UtFile.mkdirs("temp");
        String fn;

        fn = UtFile.abs(utils.replaceTestName("temp/#-&.cfx"));
        System.out.println("save file: " + fn);
        UtConf.save(getApp().getConf()).toFile(fn);
    }

    //////

    /**
     * Текущее приложение
     */
    public App getApp() {
        return app;
    }

    ////// App interface

    public ModuleHolder getModules() {
        return getApp().getModules();
    }

    public String getAppdir() {
        return getApp().getAppdir();
    }

    public String getWorkdir() {
        return getApp().getWorkdir();
    }

    public String getAppConfFile() {
        return getApp().getAppConfFile();
    }

    public boolean isDebug() {
        return getApp().isDebug();
    }

    public boolean isTest() {
        return getApp().isTest();
    }

    public Conf getConf() {
        return getApp().getConf();
    }

    public BeanFactory getBeanFactory() {
        return getApp().getBeanFactory();
    }

    public String getAppName() {
        return getApp().getAppName();
    }

    public EventBus getEventBus() {
        return getApp().getEventBus();
    }

}
