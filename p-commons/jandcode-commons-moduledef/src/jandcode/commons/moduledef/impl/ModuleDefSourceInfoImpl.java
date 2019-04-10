package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;

import java.util.*;

public class ModuleDefSourceInfoImpl implements ModuleDefSourceInfo {

    private ModuleDef moduleDef;
    private boolean source;
    private String projectPath;
    private List<String> srcPaths = new ArrayList<>();
    private List<String> srcGenPaths = new ArrayList<>();
    private List<String> testPaths = new ArrayList<>();
    private List<String> testGenPaths = new ArrayList<>();

    public ModuleDefSourceInfoImpl(ModuleDef m) {
        this.moduleDef = m;
        Map<String, Object> p = m.getProps();
        projectPath = UtCnv.toString(p.get("path.project"));
        if (UtString.empty(projectPath)) {
            return;
        }
        source = true;
        for (String key : p.keySet()) {
            String v = UtCnv.toString(p.get(key));
            if (key.startsWith("path.src.")) {
                srcPaths.add(v);
            } else if (key.startsWith("path.srcgen.")) {
                srcGenPaths.add(v);
            } else if (key.startsWith("path.test.")) {
                testPaths.add(v);
            } else if (key.startsWith("path.testgen.")) {
                testGenPaths.add(v);
            }
        }
    }

    public ModuleDef getModuleDef() {
        return moduleDef;
    }

    public boolean isSource() {
        return source;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public List<String> getSrcPaths() {
        return srcPaths;
    }

    public List<String> getSrcGenPaths() {
        return srcGenPaths;
    }

    public List<String> getTestPaths() {
        return testPaths;
    }

    public List<String> getTestGenPaths() {
        return testGenPaths;
    }

}
