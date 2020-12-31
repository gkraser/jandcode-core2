package jandcode.jc.impl;

import groovy.ant.*;
import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.event.*;
import jandcode.jc.*;
import jandcode.jc.impl.cm.*;
import jandcode.jc.impl.just.*;
import jandcode.jc.std.*;

import java.util.*;

public class ProjectScriptImpl implements Project, IProjectScript {

    private Vars vars = new VarsImpl();
    private Project project;
    private Ut ut;
    private boolean doIncludeExecuted;
    private boolean doCreateExecuted;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        if (this.project != null) {
            throw new XError("Property [project] is read only");
        }
        if (project == null) {
            throw new XError("Value for property [project] is null");
        }
        this.project = project;
    }

    public Vars getVars() {
        return vars;
    }

    ////// project delegate

    public Ctx getCtx() {
        return getProject().getCtx();
    }

    public String getName() {
        return getProject().getName();
    }

    public void setName(String name) {
        getProject().setName(name);
    }

    public IVersion getVersion() {
        return getProject().getVersion();
    }

    public void setVersion(Object v) {
        getProject().setVersion(v);
    }

    public Dir getWd() {
        return getProject().getWd();
    }

    public String wd(String path) {
        return getProject().wd(path);
    }

    public String wd() {
        return getProject().wd();
    }

    public String getProjectFile() {
        return getProject().getProjectFile();
    }

    public Project getRootProject() {
        return getProject().getRootProject();
    }

    public CmHolder getCm() {
        return getProject().getCm();
    }

    ////// IEvents

    public void onEvent(Class eventClass, Closure handler) {
        getProject().onEvent(eventClass, handler);
    }

    public <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler) {
        getProject().onEvent(eventClass, handler);
    }

    public <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler) {
        getProject().unEvent(eventClass, handler);
    }

    public void fireEvent(Event e) {
        getProject().fireEvent(e);
    }

    public void afterLoad(Closure handler) {
        getProject().afterLoad(handler);
    }

    public void afterLoadAll(Closure handler) {
        getProject().afterLoadAll(handler);
    }

    //////

    public IProjectScript include(String scriptName) {
        return getProject().include(scriptName);
    }

    public <A extends IProjectScript> A include(Class<A> scriptClass) {
        return getProject().include(scriptClass);
    }

    public IProjectScript getIncluded(String scriptName) {
        return getProject().getIncluded(scriptName);
    }

    public <A extends IProjectScript> A getIncluded(Class<A> scriptClass) {
        return getProject().getIncluded(scriptClass);
    }

    public <A extends Object> List<A> impl(Class<A> clazz) {
        return getProject().impl(clazz);
    }

    //////

    public IProjectScript create(String scriptName) {
        return getProject().create(scriptName);
    }

    public <A extends IProjectScript> A create(Class<A> scriptClass) {
        return getProject().create(scriptClass);
    }

    ////// log

    public Log getLog() {
        return getCtx().getLog();
    }

    public void log(Object msg) {
        getLog().info(msg);
    }

    public void debug(Object msg) {
        getLog().debug(msg);
    }

    public void warn(Object msg) {
        getLog().warn(msg);
    }

    ////// script

    public void error(Object msg) {
        throw new XError(UtString.toString(msg));
    }

    protected final void doIncludeThis() {
        if (doIncludeExecuted) {
            throw new XError("doIncludeThis для скрипта можно выполнить только 1 раз");
        }
        doIncludeExecuted = true;
        try {
            ((CmHolderImpl) getCm()).addFromAnnotations(this);
            onInclude();
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    /**
     * Реализация включения
     */
    protected void onInclude() throws Exception {
    }

    protected final void doCreateThis() {
        if (doCreateExecuted) {
            throw new XError("doCreateThis для скрипта можно выполнить только 1 раз");
        }
        doCreateExecuted = true;
        try {
            onCreate();
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    /**
     * Реализация создания
     */
    protected void onCreate() throws Exception {
    }

    public Project load(String projectPath) {
        return load(projectPath, true);
    }

    public Project load(String projectPath, boolean require) {
        String pf = getCtx().resolveProjectFile(getWd().getPath(), projectPath);
        if (pf == null) {
            if (!require) {
                return null;
            }
            throw new XError("Не найден файл проекта {0} относительно каталога проекта {1}", projectPath, getWd().getPath());
        }
        return getCtx().load(pf);
    }

    public AntBuilder getAnt() {
        return getCtx().getAnt(getWd());
    }

    public Ut getUt() {
        if (ut == null) {
            ut = new Ut(getProject());
        }
        return ut;
    }

    //////

    public String getScriptDir() {
        String f = getCtx().getFileProjectScript(getClass());
        if (f == null) {
            return wd("");
        } else {
            return UtFile.path(f);
        }
    }

    //////

    public void classpath(Object libs) {
        getCtx().classpath(libs);

        // помечаем добавленные
        include(ClasspathUsed.class).addClasspathUsed(libs);
    }

}


