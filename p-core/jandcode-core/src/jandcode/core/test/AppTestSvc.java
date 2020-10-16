package jandcode.core.test;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.env.*;
import jandcode.commons.error.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.moduledef.impl.*;
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
                    String fn1 = fn;
                    List<String> sourceFn = findCompiledFileInTestSource(fn);
                    if (sourceFn.size() == 0) {
                        System.out.println("WARNING: Не найден файл в исходниках тестов для файла [" + fn + "]");
                    } else if (sourceFn.size() > 1) {
                        System.out.println("WARNING: Найдено несколько файлов в исходниках тестов для файла [" + fn + "]");
                    } else {
                        fn1 = sourceFn.get(0);
                    }
                    Stopwatch sw = stopwatch.get("load-app");
                    sw.start();
                    System.out.println("***** [load app: " + fn1 + " ]***************");
                    res = AppLoader.load(fn1, true);
                    _cacheApp.put(fn, res);
                    _cacheApp.put(fn1, res);
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

    /**
     * Ищет файл в исходниках тестов по файлу, который лежит в скомпилированных
     * ресурсах.
     *
     * @param compiledFile полное имя файла в скомпилированных ресурсах
     * @return список подходяжих файлов в исходниках тестов
     */
    public List<String> findCompiledFileInTestSource(String compiledFile) throws Exception {
        List<String> res = new ArrayList<>();

        String appdir = AppConsts.resolveAppdir(UtFile.getWorkdir());
        String regFile = UtFile.join(appdir, ModuleDefConsts.FILE_REGISTRY_MODULE_DEF);
        if (!UtFile.exists(regFile)) {
            System.out.println("WARNING: Необходимо выполнить jc prepare!\n" +
                    "Не найден файл [" + regFile + "]");
            return res;
        }
        List<ModuleDef> moduleDefs = ModuleDefUtilsImpl.loadModuleDefsFromConfFile(
                regFile
        );
        String cf = compiledFile.replace("\\", "/");
        for (ModuleDef md : moduleDefs) {
            String mvp = "/" + md.getName().replace('.', '/') + "/";
            int a = cf.indexOf(mvp);
            if (a != -1) {
                String vp = cf.substring(a + mvp.length());
                for (String testroot : md.getSourceInfo().getTestPaths()) {
                    String f1 = UtFile.abs(testroot + mvp + "/" + vp);
                    if (UtFile.exists(f1)) {
                        res.add(f1);
                    }
                }
            }
        }

        return res;
    }

    //////

    /**
     * Текущее приложение
     */
    public App getApp() {
        if (app == null) {
            try {
                activateApp();
            } catch (Exception e) {
                throw new XErrorMark(e, "activateApp");
            }
        }
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

    public Env getEnv() {
        return getApp().getEnv();
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

    public void startup() {
        getApp().startup();
    }

    public void shutdown() {
        getApp().shutdown();
    }

}
