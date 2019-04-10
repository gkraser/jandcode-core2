package jandcode.core.impl;

import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.core.*;

import java.util.*;

public class ModuleHolderImpl implements ModuleHolder {

    protected App app;
    protected HashSet<String> used = new HashSet<>();
    protected NamedList<Module> items = new DefaultNamedList<>();
    protected List<String> files = new ArrayList<>();
    protected ModuleDefResolver moduleDefResolver;

    public ModuleHolderImpl(App app, ModuleDefResolver moduleDefResolver) {
        this.app = app;
        this.moduleDefResolver = moduleDefResolver;
    }

    /**
     * Добавление модуля в хранилище. Если модуль зависим от других модулей,
     * они тоже добавляются
     */
    public Module addModule(ModuleDef moduleDef) throws Exception {
        if (used.contains(moduleDef.getName())) {
            return null; // модуль уже  загружен
        }
        //
        used.add(moduleDef.getName());

        // загружаем конфигурацию модуля
        ModuleDefConfig mConf = UtModuleDef.loadModuleDefConfig(moduleDef, moduleDefResolver);

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
        Module m = new ModuleImpl(app, moduleDef, mConf);
        items.add(m);
        return m;
    }

    /**
     * Загруженный комплект файлов
     */
    public List<String> getFiles() {
        return files;
    }

    public Iterator<Module> iterator() {
        return items.iterator();
    }

    public Module find(String name) {
        return items.find(name);
    }

    public ModuleSubHolder createSubHolder() {
        return new ModuleSubHolderImpl(this);
    }
}
