package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.commons.moduledef.*;
import jandcode.jc.*;
import jandcode.jc.std.*;

import java.util.*;

/**
 * Библиотека, полученная из JavaProject
 */
public class LibJavaProject extends LibCustom {

    private JavaProject javaProject;
    private List<String> modules;

    public LibJavaProject(JavaProject javaProject) {
        super(javaProject.getCtx());
        this.javaProject = javaProject;
    }

    public String getName() {
        if (UtString.empty(name)) {
            name = javaProject.getProject().getName();
        }
        return name;
    }

    public Project getSourceProject() {
        return javaProject.getProject();
    }

    protected LibDepends createLibDepends() {
        // просто ссылка на depends из java-проекта
        return javaProject.getDepends();
    }

    public String getClasspath() {
        javaProject.recompileClasses();
        return javaProject.getDirCompiled();
    }

    public String getJar() {
        javaProject.recompileJar();
        return javaProject.getFileJar();
    }

    public String getSrc() {
        javaProject.recompileSrczip();
        return javaProject.getFileSrczip();
    }

    public String getVersion() {
        if (version == null) {
            version = javaProject.getProject().getVersion();
        }
        return version.getText();
    }

    public List<String> getModules() {
        if (modules == null) {
            List<String> tmp = new ArrayList<>();
            for (ModuleDef md : javaProject.getModuleDefs()) {
                tmp.add(md.getName());
            }
            modules = Collections.unmodifiableList(tmp);
        }
        return modules;
    }

    public String getArtifactId() {
        return javaProject.getName();
    }

    public String getGroupId() {
        return javaProject.getGroupId();
    }

}
