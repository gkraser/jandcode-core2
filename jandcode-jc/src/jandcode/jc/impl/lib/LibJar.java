package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;
import jandcode.jc.impl.version.*;

import java.text.*;
import java.util.*;
import java.util.jar.*;

/**
 * Реализация Lib для jar
 */
public class LibJar extends LibCustom {

    private Manifest manifest;
    private List<String> modules;

    /**
     * @param jar пустая строка, если jar точно нет
     * @param src пустая строка, если src точно нет
     */
    public LibJar(Ctx ctx, String name, String version, LibDepends depends,
            String jar, String src, String artifactId, String groupId) {
        super(ctx);
        this.name = name;
        this.version = new TextVersion(version);
        this.jar = jar;
        this.src = src;
        this.artifactId = artifactId;
        this.groupId = groupId;
        if (depends != null) {
            this.depends = depends;
        }
    }

    public String getVersion() {
        if (version.isDummy()) {
            version = new TextVersion(LibUtils.getJarVersion(getManifest()));
        }
        return version.getText();
    }

    //////

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

    //////

    protected LibDepends createLibDepends() {
        return LibUtils.getJarLibDepends(getCtx(), getName(), getManifest());
    }

    protected Manifest getManifest() {
        if (manifest == null) {
            try {
                String jarPath = getJar();
                if (!UtString.empty(jarPath)) {
                    JarFile jf = new JarFile(jarPath);
                    try {
                        manifest = jf.getManifest();
                    } finally {
                        jf.close();
                    }
                }
            } catch (Exception e) {
                ErrorInfo ei = UtError.createErrorInfo(e);
                getCtx().debug(MessageFormat.format("Error getManifest for [{0}]: {1}", getJar(), ei.getText()));
            }
        }
        return manifest;
    }

    public List<String> getModules() {
        if (modules == null) {
            modules = Collections.unmodifiableList(LibUtils.getJarModules(getManifest()));
        }
        return modules;
    }


}
