package jandcode.wax.web;

import jandcode.commons.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.web.gsp.*;

import java.util.*;

/**
 * Сервисные методы для использования в index.gsp и аналогичных файлах,
 * которые формируют стартовый html-файл приложения.
 */
public class WaxIndexGspContext implements IGspContextLinkSet {

    private GspContext gspContext;
    private String title;
    private String cfgJson;
    private String themeName;
    private String env = "jandcode/wax/web/index.js";
    private String main;

    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
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
     * Имя темы
     */
    public String getThemeName() {
        if (themeName == null) {
            return gspContext.getApp().bean(WaxThemeService.class).getDefaultThemeName();
        }
        return themeName;
    }

    public void setThemeName(String theme) {
        this.themeName = theme;
    }

    /**
     * js-файл с темой
     */
    public String getTheme() {
        return gspContext.getApp().bean(WaxThemeService.class).getThemeFile(getThemeName());
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
        res.add(getEnv());
        res.add(getTheme());
        if (!UtString.empty(getMain())) {
            res.add(getMain());
        }
        return res;
    }

    //////

    /**
     * Конфигурация для клиента в виде json-строки
     */
    public String getCfgJson() {
        if (this.cfgJson == null) {
            WaxClientCfgService cfgSvc = gspContext.getApp().bean(WaxClientCfgService.class);
            this.cfgJson = UtJson.toJson(cfgSvc.grabClientCfg());
        }
        return this.cfgJson;
    }

    //////

    private String resolveModule(String mod) {
        return gspContext.getApp().bean(JsModuleService.class).resolveModuleName(mod);
    }

}
