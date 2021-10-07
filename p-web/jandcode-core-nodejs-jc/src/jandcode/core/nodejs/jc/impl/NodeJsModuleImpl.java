package jandcode.core.nodejs.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.core.nodejs.jc.*;

import java.util.*;

public class NodeJsModuleImpl implements NodeJsModule {

    private Project ownerProject;
    private String name;
    private String version;
    private String path;
    private Map<String, String> dependencies = new LinkedHashMap<>();
    private Map<String, String> devDependencies = new LinkedHashMap<>();
    private Map<String, String> optionalDependencies = new LinkedHashMap<>();
    private Map<String, String> allDependencies = new LinkedHashMap<>();
    private Map<String, Object> packageJson = new LinkedHashMap<>();

    public NodeJsModuleImpl(String packageJsonFile, Project ownerProject) {
        this.ownerProject = ownerProject;
        try {
            loadFromPackageJson(packageJsonFile);
        } catch (Exception e) {
            throw new XErrorMark(e, packageJsonFile);
        }
    }

    public NodeJsModuleImpl(String packageJsonFile) {
        this(packageJsonFile, null);
    }

    public Project getOwnerProject() {
        return ownerProject;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getPackageJson() {
        return packageJson;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public Map<String, String> getDevDependencies() {
        return devDependencies;
    }

    public Map<String, String> getOptionalDependencies() {
        return optionalDependencies;
    }

    public Map<String, String> getAllDependencies() {
        return allDependencies;
    }

    public String toString() {
        return name + "=" + version;
    }

    //////

    public void loadFromPackageJson(String filename) throws Exception {
        this.path = UtFile.path(filename);
        String s = UtFile.loadString(filename);
        this.packageJson.putAll((Map) UtJson.fromJson(s));
        this.name = UtCnv.toString(this.packageJson.get("name"));
        this.version = UtCnv.toString(this.packageJson.get("version"));
        //
        fillMapFrom(this.dependencies, this.packageJson, "dependencies");
        fillMapFrom(this.devDependencies, this.packageJson, "devDependencies");
        fillMapFrom(this.optionalDependencies, this.packageJson, "optionalDependencies");
        this.allDependencies.putAll(this.devDependencies);
        this.allDependencies.putAll(this.dependencies);
        this.allDependencies.putAll(this.optionalDependencies);
    }

    private void fillMapFrom(Map<String, String> dest, Map from, String fromKey) {
        Object o1 = from.get(fromKey);
        if (o1 instanceof Map) {
            Map m = (Map) o1;
            for (Object key : m.keySet()) {
                dest.put(UtCnv.toString(key), UtCnv.toString(m.get(key)));
            }
        }
    }

}
