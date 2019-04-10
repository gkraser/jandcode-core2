package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;

import java.util.*;

public class ModuleDefImpl implements ModuleDef {

    protected String name;
    protected String path;
    protected String moduleFile;
    protected String packageRoot;
    protected Map<String, Object> props = new LinkedHashMap<>();
    protected ModuleDefSourceInfo sourceInfo;
    protected List<String> depends = new ArrayList<>();

    public ModuleDefImpl(String name, String path, String packageRoot, String moduleFile) {
        this.name = name;
        this.path = UtFile.getFileObject(path).toString();
        this.packageRoot = packageRoot;
        this.moduleFile = moduleFile;
        if (this.moduleFile == null) {
            this.moduleFile = this.path + "/" + ModuleDefConsts.FILE_MODULE_CONF;
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getModuleFile() {
        return moduleFile;
    }

    public String getPackageRoot() {
        if (packageRoot == null) {
            return getName();
        }
        return packageRoot;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public ModuleDefSourceInfo getSourceInfo() {
        if (sourceInfo == null) {
            synchronized (this) {
                if (sourceInfo == null) {
                    sourceInfo = new ModuleDefSourceInfoImpl(this);
                }
            }
        }
        return sourceInfo;
    }

    public List<String> getDepends() {
        return depends;
    }

    public String toString() {
        return "" + getName() + " [" + getPath() + "]";
    }

}
