package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.jc.*;
import jandcode.jc.impl.depends.*;
import jandcode.jc.impl.version.*;

import java.util.*;

/**
 * Базовый предок для библиотек
 */
public abstract class LibCustom implements Lib {

    protected List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>());

    private Ctx ctx;
    protected String name;
    protected IVersion version;
    protected String jar;
    protected String src;
    protected LibDepends depends;
    protected String artifactId;
    protected String groupId;

    public LibCustom(Ctx ctx) {
        assert ctx != null;
        this.ctx = ctx;
    }

    protected Ctx getCtx() {
        return ctx;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName() + ":" + getVersion();
    }

    public Project getSourceProject() {
        return null;
    }

    public LibDepends getDepends() {
        if (depends == null) {
            depends = createLibDepends();
        }
        return depends;
    }

    /**
     * Создание экземпляра depends
     */
    protected LibDepends createLibDepends() {
        return new LibDependsImpl(getCtx(), this);
    }

    public String getClasspath() {
        return getJar();
    }

    public String getJar() {
        if (jar == null) {
            jar = "";
        }
        return jar;
    }

    public String getSrc() {
        if (src == null) {
            src = "";
        }
        return src;
    }

    public String getVersion() {
        if (version == null) {
            version = new DummyVersion();
        }
        return version.getText();
    }

    public boolean isSys() {
        return false;
    }

    public List<String> getModules() {
        return EMPTY_LIST;
    }

    public String getArtifactId() {
        if (UtString.empty(artifactId)) {
            return getName();
        }
        return artifactId;
    }

    public String getGroupId() {
        if (UtString.empty(groupId)) {
            return getArtifactId();
        }
        return groupId;
    }

}
