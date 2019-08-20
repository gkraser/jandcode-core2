package jandcode.jc.impl;

import groovy.lang.*;
import groovy.util.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.event.*;
import jandcode.jc.*;
import jandcode.jc.impl.just.*;
import jandcode.jc.impl.lib.*;
import jandcode.jc.impl.log.*;

import java.text.*;
import java.util.*;

public class CtxImpl implements Ctx {

    private JcConfig config;
    private boolean configApplyExecuted;
    private ScriptHolder scriptHolder;
    private AntHolder antHolder;
    private ProjectHolder projectHolder;
    private LogImpl log;
    private LibHolder libHolder;
    private EventHolder eventHolder;
    private TempdirHolder tempdirHolder;
    private ServiceHolder serviceHolder;
    private boolean runtimeCtx;
    private EnvImpl env;


    public CtxImpl() {
        this.config = new JcConfigImpl();
        this.eventHolder = new EventHolder(this, null);
        this.log = new LogImpl(this);
        this.tempdirHolder = new TempdirHolder();
        this.serviceHolder = new ServiceHolder(this);
        this.env = new EnvImpl(this);
        //
        this.scriptHolder = new ScriptHolder(this);
        this.antHolder = new AntHolder(this);
        this.projectHolder = new ProjectHolder(this);
        this.libHolder = new LibHolder(this);
    }

    public CtxImpl(boolean runtimeCtx) {
        this();
        this.runtimeCtx = runtimeCtx;
    }

    public boolean isRuntimeCtx() {
        return runtimeCtx;
    }

    ////// config

    public JcConfig getConfig() {
        return config;
    }

    public void applyConfig(JcConfig config) {
        if (configApplyExecuted) {
            return;
        }
        this.config = config;
        this.configApplyExecuted = true;

        // загружаем

        // грузим корневой проект
        String s = config.getAppdir();
        if (!UtString.empty(s)) {
            String pf = resolveProjectFile(s, JcConsts.PROJECT_FILE);
            if (pf != null) {
                load(pf);
            }
        }

        // обрабатываем JC_PATH
        for (String pp1 : config.getAutoLoadProjects()) {
            String pp2 = resolveProjectFile(UtFile.getWorkdir(), pp1);
            if (pp2 != null) {
                try {
                    debug("load project from " + JcConsts.ENV_JC_PATH + " variable: " + pp2);
                    load(pp2);
                } catch (Exception e) {
                    ErrorInfo ei = UtError.createErrorInfo(e);
                    String msg = MessageFormat.format("Ошибка при загрузке проекта [{0}], указанного в переменной {1}: {2}",
                            pp2, JcConsts.ENV_JC_PATH, ei.getText());
                    warn(msg);
                }
            }
        }

        // готово
    }

    ////// ILog

    public Log getLog() {
        return log;
    }

    public void log(Object msg) {
        log.info(msg);
    }

    public void debug(Object msg) {
        log.debug(msg);
    }

    public void warn(Object msg) {
        log.warn(msg);
    }

    ////// IEvents

    public void onEvent(Class eventClass, Closure handler) {
        eventHolder.onEvent(eventClass, handler);
    }

    public <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler) {
        eventHolder.onEvent(eventClass, handler);
    }

    public <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler) {
        eventHolder.unEvent(eventClass, handler);
    }

    public void fireEvent(Event e) {
        eventHolder.fireEvent(e);
    }

    ////// IScripts

    public Class getClassProjectScript(String scriptName, Project project) {
        return scriptHolder.getClassProjectScript(scriptName, project);
    }

    public IProjectScript createProjectScript(Class cls) {
        return scriptHolder.createProjectScript(cls);
    }

    public String getFileProjectScript(Class cls) {
        return scriptHolder.getFileProjectScript(cls);
    }

    public String getBeforeLoadProjectScript(String filename) {
        return scriptHolder.getBeforeLoadProjectScript(filename);
    }

    ////// IProjects

    public Project load(String projectPath) {
        return projectHolder.load(projectPath);
    }

    public Collection<Project> getProjects() {
        return projectHolder.getProjects();
    }

    public String resolveProjectFile(String basePath, String projectPath) {
        return projectHolder.resolveProjectFile(basePath, projectPath);
    }

    public Project getRootProject(String path) {
        return projectHolder.getRootProject(path);
    }

    ////// IAnts

    public AntBuilder getAnt(String basedir) {
        return antHolder.getAnt(basedir);
    }

    public AntBuilder getAnt(Dir dir) {
        return antHolder.getAnt(dir);
    }

    ////// ILibs

    public void addLibProvider(ILibProvider p) {
        libHolder.addLibProvider(p);
    }

    public List<ILibProvider> getLibsProviders() {
        return libHolder.getLibsProviders();
    }

    public Lib findLib(String name) {
        return libHolder.findLib(name);
    }

    public Lib getLib(String name) {
        return libHolder.getLib(name);
    }

    public ListLib getLibs(Object libNames) {
        return libHolder.getLibs(libNames);
    }

    public ListLib getLibs(Object libNames, String groupNames) {
        return libHolder.getLibs(libNames, groupNames);
    }

    public ListLib getLibs() {
        return libHolder.getLibs();
    }

    public void classpath(Object libs) {
        libHolder.classpath(libs);
    }

    public ListLib loadLibs(String path) {
        return libHolder.loadLibs(path);
    }

    public void addLibDir(String path) {
        libHolder.addLibDir(path);
    }

    public Lib findLibForModule(String moduleName) {
        return libHolder.findLibForModule(moduleName);
    }

    public ListLib getClasspathLibs() {
        return libHolder.getClasspathLibs();
    }

    public LibModuleInfo findModuleInfo(String moduleName) {
        return libHolder.findModuleInfo(moduleName);
    }

    // ITempdir

    public String getTempdirRoot() {
        return tempdirHolder.getTempdirRoot();
    }

    public String getTempdir(String localPath) {
        return tempdirHolder.getTempdir(localPath);
    }

    public String getTempdirCommon(String localPath) {
        return tempdirHolder.getTempdirCommon(localPath);
    }

    ////// IServices

    public <A extends ICtxService> A service(Class<A> serviceClass) {
        return serviceHolder.service(serviceClass);
    }

    ////// IEnv

    public Env getEnv() {
        return env;
    }

}
