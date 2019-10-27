package jandcode.core.jsa.gsp;

import jandcode.commons.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.theme.*;
import jandcode.core.web.gsp.*;

import java.util.*;

/**
 * Сервисные методы для использования в index.gsp и аналогичных файлах,
 * которые формируют стартовый html-файл приложения.
 */
public class JsaIndexGspContext implements IGspContextLinkSet {

    private GspContext gspContext;
    private String title;
    private String env;
    private String main;
    private String cfgJson;
    private String theme;

    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;

        //
        JsaCfg jsaCfg = gspContext.getApp().bean(JsaCfg.class);
        this.env = jsaCfg.getEnvModule();
        this.theme = jsaCfg.getDefaultTheme();
    }

    //////

    /**
     * Заголовок для страницы
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //////

    /**
     * Среда запуска. js-файл, который должен быть загружен перед тем,
     * как загрузится тема и приложение.
     */
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = resolveModule(env);
    }

    //////

    /**
     * Главный модуль приложения
     */
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = resolveModule(main);
    }

    //////

    /**
     * Список модулей, которые нужно включить в linkModule
     */
    public List<String> getModules() {
        List<String> res = new ArrayList<>();
        String s;

        s = getEnv();
        if (!UtString.empty(s)) {
            res.add(s);
        }

        s = getTheme();
        if (!UtString.empty(s)) {
            res.add(s);
        }

        s = getMain();
        if (!UtString.empty(s)) {
            res.add(s);
        }

        return res;
    }

    //////

    /**
     * Конфигурация для клиента в виде json-строки
     */
    public String getCfgJson() {
        if (this.cfgJson == null) {
            JsaClientCfgService cfgSvc = gspContext.getApp().bean(JsaClientCfgService.class);
            this.cfgJson = UtJson.toJson(cfgSvc.grabClientCfg());
        }
        return this.cfgJson;
    }

    //////

    /**
     * Установить тему как имя темы или файл с темой
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * js-файл с темой
     */
    public String getTheme() {
        if (UtString.empty(theme)) {
            return null;
        }
        if (theme.indexOf('/') != -1) {
            return theme;
        }
        return gspContext.getApp().bean(JsaThemeService.class).findThemeFile(theme);
    }

    //////

    private String resolveModule(String mod) {
        return gspContext.getApp().bean(JsModuleService.class).resolveModuleName(mod);
    }

}
