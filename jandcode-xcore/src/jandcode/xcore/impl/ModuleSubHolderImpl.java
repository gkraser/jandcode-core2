package jandcode.xcore.impl;

import jandcode.commons.named.*;
import jandcode.xcore.*;

import java.util.*;

public class ModuleSubHolderImpl implements ModuleSubHolder {

    protected ModuleHolder own;
    protected HashSet<String> used = new HashSet<>();
    protected NamedList<Module> items = new DefaultNamedList<>();

    public ModuleSubHolderImpl(ModuleHolder own) {
        this.own = own;
    }

    public void add(String moduleName) {
        if (used.contains(moduleName)) {
            return; // модуль уже добавлен
        }
        //
        used.add(moduleName);
        //
        Module m = own.get(moduleName);

        for (String dep : m.getDepends()) {
            add(dep);
        }

        //
        items.add(m);
    }

    public void remove(String moduleName) {
        used.remove(moduleName);
        items.remove(moduleName);
    }

    public Iterator<Module> iterator() {
        return items.iterator();
    }

    public Module find(String name) {
        return items.find(name);
    }

    public ModuleSubHolder createSubHolder() {
        return new ModuleSubHolderImpl(this.own);
    }

}
