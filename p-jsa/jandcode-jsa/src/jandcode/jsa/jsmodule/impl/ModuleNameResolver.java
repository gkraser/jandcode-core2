package jandcode.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;
import jandcode.jsa.jsmodule.*;

import java.util.*;

public class ModuleNameResolver {

    private JsModuleService svc;
    private WebService webSvc;
    private List<String> searchPaths = new ArrayList<>();
    private List<String> moduleExts = new ArrayList<>();

    public ModuleNameResolver(JsModuleService svc) {
        this.svc = svc;
        this.webSvc = svc.getApp().bean(WebService.class);
        this.searchPaths.add("_jsa/_node_modules");
        //todo пока явно, возможно нужно будет настроить. Порядок важен (приоритет)
        this.moduleExts.add("js");
        this.moduleExts.add("vue");
    }

    public String resolveModuleName(String name) {
        // todo кешировать этот метод. Пересоздавать resolver когда сбрасывается кеш JsModuleService
        return doResolve(name);
    }

    private String doResolve(String name) {
        // нормализуем
        name = UtVDir.normalize(name);

        boolean isModule = false;
        // модуль?
        Module mod = svc.getApp().getModules().find(name);
        if (mod != null) {
            // вариант: 'jandcode.jsa', превращаем в путь
            name = name.replace('.', '/');
            isModule = true;
        } else {
            int a = name.indexOf('/');
            if (a != -1) {
                String mn = name.substring(0, a);
                mod = svc.getApp().getModules().find(mn);
                if (mod != null) {
                    // вариант: 'jandcode.jsa/path', превращаем в путь
                    name = mn.replace('.', '/') + name.substring(a);
                    isModule = true;
                }
            }
        }

        String fn = resolveFile(name);
        if (fn != null) {
            return fn;
        }
        if (isModule) {
            // был заказан модуль, но он не найден
            return name;
        }

        // не нашли напрямую, пробуем в путях поиска (для node_modules актуально)
        for (String sp : this.searchPaths) {
            String nm1 = UtFile.join(sp, name);
            String fn1 = resolveFile(nm1);
            if (fn1 != null) {
                return fn1;
            }
        }

        // вообще ничего не нашли
        return name;
    }

    private String resolveFile(String name) {
        VirtFile f, fjs;

        f = webSvc.findFile(name);
        if (f != null && f.isFile()) {
            return f.getPath();
        }

        // файл возможно без расширения, пробуем известные
        for (String ext : this.moduleExts) {
            fjs = webSvc.findFile(name + "." + ext);
            if (fjs != null && fjs.isFile()) {
                return fjs.getPath();
            }
        }

        if (f != null) {
            // это папка и она существует
            return resolveFolder(name);

        } else {
            return null;
        }

    }

    private String resolveFolder(String name) {
        String fn;
        VirtFile f;

        fn = UtVDir.join(name, "package.json");
        f = webSvc.findFile(fn);
        if (f != null) {
            // есть package.json
            try {
                String pst = f.loadText();
                Object m = UtJson.fromJson(pst);
                if (m instanceof Map) {
                    String mf = UtCnv.toString(((Map) m).get("main"));
                    if (!UtString.empty(mf)) {
                        String fn1;
                        if (mf.startsWith(".")) {
                            fn1 = UtVDir.expandRelPath(name, mf);
                        } else {
                            fn1 = UtVDir.join(name, mf);
                        }
                        String ext = UtFile.ext(fn1);
                        if (UtString.empty(ext)) {
                            fn1 = fn1 + ".js";
                        }
                        f = webSvc.findFile(fn1);
                        if (f.isExists()) {
                            return f.getPath();
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }

        fn = UtVDir.join(name, "index.js");
        f = webSvc.findFile(fn);
        return f == null ? null : f.getPath();
    }

}
