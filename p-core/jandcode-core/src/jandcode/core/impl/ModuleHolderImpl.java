package jandcode.core.impl;

import jandcode.commons.event.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

public class ModuleHolderImpl implements ModuleHolder {

    /**
     * Событие возникает при загрузке конфигурации модуля
     */
    public static class Event_ModuleConfLoaded implements Event {
        ModuleDef moduleDef;
        ModuleDefConfig moduleDefConfig;

        public Event_ModuleConfLoaded(ModuleDef moduleDef, ModuleDefConfig moduleDefConfig) {
            this.moduleDef = moduleDef;
            this.moduleDefConfig = moduleDefConfig;
        }

        public ModuleDef getModuleDef() {
            return moduleDef;
        }

        public ModuleDefConfig getModuleDefConfig() {
            return moduleDefConfig;
        }
    }

    protected App app;
    protected HashSet<String> used = new HashSet<>();
    protected NamedList<ModuleInst> items = new DefaultNamedList<>();
    protected List<String> files = new ArrayList<>();
    protected ModuleDefResolver moduleDefResolver;
    private EventBus eventBus = new DefaultEventBus();
    private Map<String, String> vars;

    public ModuleHolderImpl(App app, ModuleDefResolver moduleDefResolver, Map<String, String> vars) {
        this.app = app;
        this.moduleDefResolver = moduleDefResolver;
        this.vars = vars;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Добавление модуля в хранилище. Если модуль зависим от других модулей,
     * они тоже добавляются
     */
    public ModuleInst addModule(ModuleDef moduleDef) throws Exception {
        if (used.contains(moduleDef.getName())) {
            return null; // модуль уже  загружен
        }
        //
        used.add(moduleDef.getName());

        // загружаем конфигурацию модуля
        ModuleDefConfig mConf = UtModuleDef.loadModuleDefConfig(moduleDef, moduleDefResolver, vars);
        getEventBus().fireEvent(new Event_ModuleConfLoaded(moduleDef, mConf));

        // загружаем зависимости из ModuleDef
        for (String dep : moduleDef.getDepends()) {
            ModuleDef m1 = moduleDefResolver.getModuleDef(dep);
            addModule(m1);
        }

        // загружаем зависимости из конфигурации
        for (String dep : mConf.getDepends()) {
            ModuleDef m1 = moduleDefResolver.getModuleDef(dep);
            addModule(m1);
        }

        // запоминаем файлы
        files.addAll(mConf.getFiles());

        // создаем и регистрируем экземпляр модуля
        ModuleInst m = new ModuleImpl(app, moduleDef, mConf);
        items.add(m);
        return m;
    }

    /**
     * Загруженный комплект файлов
     */
    public List<String> getFiles() {
        return files;
    }

    public Iterator<ModuleInst> iterator() {
        return items.iterator();
    }

    public ModuleInst find(String name) {
        return items.find(name);
    }

    public ModuleSubHolder createSubHolder() {
        return new ModuleSubHolderImpl(this);
    }
}
