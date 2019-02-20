package jandcode.jc.impl;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.event.*;
import jandcode.jc.*;
import jandcode.jc.impl.cm.*;
import jandcode.jc.impl.just.*;
import jandcode.jc.impl.version.*;

import java.util.*;

public class ProjectImpl implements Project {

    protected Ctx ctx;
    private Vars vars = new VarsImpl();
    private Map<String, IProjectScript> includedScripts = new LinkedHashMap<String, IProjectScript>();
    private String name;
    private Dir wd;
    private CmHolder cmHolder;
    private EventHolder eventHolder;
    private String projectFile;
    private IVersion version = new DummyVersion();
    private Project rootProject;

    public ProjectImpl(Ctx ctx, String projectFile) {
        this.ctx = ctx;
        this.projectFile = projectFile;
        wd = new DirImpl(UtFile.path(UtFile.abs(projectFile)));
        name = wd.getName();
        cmHolder =  new CmHolderImpl(this);
        eventHolder = new EventHolder(ctx, this);
    }

    public Ctx getCtx() {
        return ctx;
    }

    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (UtString.empty(name)) {
            name = wd.getName();
        }
        this.name = name;
    }

    public Vars getVars() {
        return vars;
    }

    public IVersion getVersion() {
        if (!this.version.isDummy()) {
            return this.version;
        }
        Project root = getRootProject();
        if (root != null) {
            return root.getVersion();
        }
        return this.version;
    }

    public void setVersion(Object version) {
        if (version == null) {
            this.version = new DummyVersion();

        } else if (version instanceof CharSequence) {
            this.version = new TextVersion(version.toString());

        } else if (version instanceof IVersion) {
            this.version = (IVersion) version;

        } else if (version instanceof Project) {
            this.version = new ProjectVersion((Project) version);

        } else {
            this.version = new DummyVersion();
        }
    }

    public Dir getWd() {
        return wd;
    }

    public String wd(String path) {
        return getWd().join(path);
    }

    public String wd() {
        return getWd().getPath();
    }

    public String getProjectFile() {
        return projectFile;
    }

    public Project getRootProject() {
        if (rootProject != null) {
            return rootProject;
        }
        Project p = getCtx().getRootProject(getWd().getPath());
        if (p != null && p == this) {
            p = null; // я сам себе не могу мыть корневым
        }
        this.rootProject = p;
        return this.rootProject;
    }

    public CmHolder getCm() {
        return cmHolder;
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

    public void afterLoad(Closure handler) {
        onEvent(JcConsts.Event_ProjectAfterLoad.class, handler);
    }

    public void afterLoadAll(Closure handler) {
        onEvent(JcConsts.Event_ProjectAfterLoadAll.class, handler);
    }

    //////

    public IProjectScript include(String scriptName) {
        Class scriptClass = ctx.getClassProjectScript(scriptName, this);
        return include(scriptClass);
    }

    public IProjectScript getIncluded(String scriptName) {
        Class scriptClass = ctx.getClassProjectScript(scriptName, this);
        return includedScripts.get(scriptClass.getName());
    }

    @SuppressWarnings("unchecked")
    public <A extends IProjectScript> A include(Class<A> scriptClass) {
        IProjectScript script = includedScripts.get(scriptClass.getName());
        if (script != null) {
            return (A) script;
        }
        script = ctx.createProjectScript(scriptClass);
        includedScripts.put(scriptClass.getName(), script);
        if (script instanceof ProjectScriptImpl) {
            ((ProjectScriptImpl) script).setProject(this);
            ((ProjectScriptImpl) script).doCreateThis();
            ((ProjectScriptImpl) script).doIncludeThis();
        } else {
            throw new XError("Not implemented ProjectScriptImpl");
        }
        return (A) script;
    }

    @SuppressWarnings("unchecked")
    public <A extends IProjectScript> A getIncluded(Class<A> scriptClass) {
        return (A) getIncluded(scriptClass.getName());
    }

    @SuppressWarnings("unchecked")
    public <A extends Object> List<A> impl(Class<A> clazz) {
        List<A> res = new ArrayList<A>();
        for (Object it : includedScripts.values()) {
            if (clazz.isAssignableFrom(it.getClass())) {
                res.add((A) it);
            }
        }
        return res;
    }

    //////

    @SuppressWarnings("unchecked")
    public <A extends IProjectScript> A create(Class<A> scriptClass) {
        return (A) create(scriptClass.getName());
    }

    public IProjectScript create(String scriptName) {
        Class scriptClass = ctx.getClassProjectScript(scriptName, this);
        IProjectScript script = ctx.createProjectScript(scriptClass);
        if (script instanceof ProjectScriptImpl) {
            ((ProjectScriptImpl) script).setProject(this);
            ((ProjectScriptImpl) script).doCreateThis();
        } else {
            throw new XError("Not implemented ProjectScriptImpl");
        }
        return script;
    }

}
