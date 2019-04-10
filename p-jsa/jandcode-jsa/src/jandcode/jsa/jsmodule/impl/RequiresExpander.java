package jandcode.jsa.jsmodule.impl;

import jandcode.commons.error.*;
import jandcode.jsa.jsmodule.*;

import java.util.*;

/**
 * Раскрытие requires для jsmodule
 */
public class RequiresExpander {

    private JsModuleService svc;
    protected HashSet<String> used = new HashSet<>();
    protected List<JsModule> items = new ArrayList<>();

    public RequiresExpander(JsModuleService svc) {
        this.svc = svc;
    }

    /**
     * Раскрытые зависимости
     */
    public List<JsModule> getItems() {
        return items;
    }

    /**
     * Добавить зависимость
     */
    public void addRequire(JsModule forModule, String requireName) {
        JsModule module = svc.findModule(requireName);
        if (module == null) {
            throw new XError("Не найдена зависимость [{0}] для [{1}]", requireName, forModule.getName());
        }
        addModule(module);
    }

    //////

    /**
     * Добавить модуль в коллекцию вместе с зависимостями
     */
    protected void addModule(JsModule module) {
        String p = module.getName();
        if (used.contains(p)) {
            return;
        }
        //
        used.add(p);
        //
        for (JsModule a1 : module.getRequiresExpanded()) {
            addModule(a1);
        }
        //
        items.add(module);
    }

}
