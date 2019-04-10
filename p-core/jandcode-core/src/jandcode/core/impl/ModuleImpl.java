package jandcode.core.impl;

import jandcode.commons.conf.*;
import jandcode.commons.moduledef.*;
import jandcode.core.*;

import java.util.*;

public class ModuleImpl implements Module {

    private App app;
    private ModuleDef moduleDef;
    private Conf conf;
    protected Map<String, Object> props = new LinkedHashMap<>();
    private List<String> depends;

    public ModuleImpl(App app, ModuleDef moduleDef, ModuleDefConfig moduleDefConfig) {
        this.app = app;
        this.moduleDef = moduleDef;
        this.conf = moduleDefConfig.getConf();
        //
        Set<String> tmpDep = new LinkedHashSet<>(moduleDef.getDepends());
        tmpDep.addAll(moduleDefConfig.getDepends());
        this.depends = new ArrayList<>(tmpDep);
    }

    public App getApp() {
        return app;
    }

    public String toString() {
        return moduleDef.toString();
    }

    public String getName() {
        return moduleDef.getName();
    }

    public String getPath() {
        return moduleDef.getPath();
    }

    public String getVPath() {
        return moduleDef.getPackageRoot().replace('.', '/');
    }

    public String getPackageRoot() {
        return moduleDef.getPackageRoot();
    }

    public Conf getConf() {
        return conf;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public List<String> getDepends() {
        return depends;
    }

    public ModuleDefSourceInfo getSourceInfo() {
        return moduleDef.getSourceInfo();
    }
}
