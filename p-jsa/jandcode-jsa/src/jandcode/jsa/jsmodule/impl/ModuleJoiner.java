package jandcode.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.jsa.jsmodule.*;

import java.util.*;

/**
 * Выявление общих частей набора модулей.
 * Используется в gsp, для формирования ссылок и в action, для выдачи модулей клиенту
 */
public class ModuleJoiner {

    private JsModuleService svc;
    private boolean dirty = true;
    private List<JsModule> modules = new ArrayList<>();
    private Set<JsModule> all;
    private Set<JsModule> required;
    private List<JsModule> top;

    public ModuleJoiner(JsModuleService svc) {
        this.svc = svc;
    }

    /**
     * Добавить модуль
     *
     * @param moduleName имена модулей через ','
     */
    public void addModule(String moduleName) {
        if (UtString.empty(moduleName)) {
            return;
        }
        this.dirty = true;
        modules.addAll(svc.getModules(moduleName));
    }

    /**
     * Добавить модуль
     *
     * @param module модуль
     */
    public void addModule(JsModule module) {
        if (module == null) {
            return;
        }
        this.dirty = true;
        modules.add(module);
    }

    /**
     * Все явно добавленные модули
     */
    public List<JsModule> getModules() {
        return this.modules;
    }

    /**
     * Все модули, включая required и добавленные
     */
    public Set<JsModule> getAll() {
        prepare();
        return this.all;
    }

    /**
     * Все модули, которые требуются другим модулям
     */
    public Set<JsModule> getRequired() {
        prepare();
        return this.required;
    }

    /**
     * Все модули верхнего уровня
     */
    public List<JsModule> getTop() {
        prepare();
        return this.top;
    }

    protected void prepare() {
        if (!this.dirty) {
            return;
        }

        this.all = new LinkedHashSet<>();
        this.required = new LinkedHashSet<>();
        for (JsModule m : this.modules) {
            required.addAll(m.getRequiresExpanded());
            all.addAll(m.getRequiresExpanded());
            all.add(m);
        }

        this.top = new ArrayList<>();
        for (JsModule m : all) {
            if (!required.contains(m)) {
                top.add(m);
            }
        }

        this.dirty = false;
    }

}
