package jandcode.jsa.jsmodule.std;


import jandcode.commons.*;
import jandcode.core.*;
import jandcode.jsa.jsmodule.*;

import java.util.*;

/**
 * Зависимости.
 * Для указанных модулей возвращает json-массив c id модулей, которые необходимы
 */
public class ModuleTextRequires extends ModuleTextCustom {

    private String paths;

    public ModuleTextRequires(App app, String paths) {
        setApp(app);
        this.paths = paths;
    }

    protected String makeText() {
        JsModuleService svc = getApp().bean(JsModuleService.class);

        Set<JsModule> lst = new LinkedHashSet<>();

        // собираем уникальный список нужных модулей
        List<JsModule> mods = svc.getModules(this.paths);
        for (JsModule mod : mods) {
            lst.addAll(mod.getRequiresExpanded());
            lst.add(mod);
        }


        // формируем json

        List<String> res = new ArrayList<>();

        for (JsModule mod : lst) {
            res.add(mod.getId());
        }

        return UtJson.toJson(res);
    }

}
