package jandcode.core.apx.web.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.apx.web.gsp.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.std.gsp.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

public class WebpackIndexGspContextImpl implements WebpackIndexGspContext, IAppLink {

    private GspContext gspContext;
    private String srcPath = "public";
    private String entrypointsManifestPath = "entrypoints-manifest.json";
    private String libraryName = "JcEntry";
    private Map<String, Object> entrypointsManifest = null;

    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    public App getApp() {
        return this.gspContext.getApp();
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.entrypointsManifest = null;
        this.srcPath = srcPath;
    }

    public String getEntrypointsManifestPath() {
        return entrypointsManifestPath;
    }

    public void setEntrypointsManifestPath(String entrypointsManifestPath) {
        this.entrypointsManifest = null;
        this.entrypointsManifestPath = entrypointsManifestPath;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void addLink(String entryPoint) {
        Map<String, Object> m = getEntrypointsManifest();
        List<String> links = getEntrypointLinks(entryPoint);
        JsIndexGspContext jsIndex = gspContext.inst(JsIndexGspContext.class);
        for (String link : links) {
            jsIndex.addLink(UtVDir.join(getSrcPath(), link));
        }
    }

    //////

    protected Map<String, Object> getEntrypointsManifest() {
        if (entrypointsManifest == null) {
            String path = UtVDir.join(getSrcPath(), getEntrypointsManifestPath());
            WebService webSvc = getApp().bean(WebService.class);
            VirtFile f = webSvc.getFile(path);
            String s = f.loadText();
            Map<String, Object> tmp = (Map<String, Object>) UtJson.fromJson(s);
            entrypointsManifest = tmp;
        }
        return entrypointsManifest;
    }

    protected List<String> getEntrypointLinks(String entrypoint) {
        Map<String, Object> m = getEntrypointsManifest();
        Object m1 = m.get(entrypoint);
        if (!(m1 instanceof Map)) {
            throw new XError("Not found key [{0}] in [{1}/{2}]", entrypoint, getSrcPath(), getEntrypointsManifestPath());
        }
        Object m2 = ((Map) m1).get("assets");
        if (!(m2 instanceof Map)) {
            throw new XError("Not found key [{0}] in [{1}/{2}] for entrypoint [{3}]", "assets", getSrcPath(), getEntrypointsManifestPath(), entrypoint);
        }
        //
        List<String> res = new ArrayList<>();
        Object m3;

        m3 = ((Map) m2).get("js");
        if (m3 instanceof Collection) {
            res.addAll((Collection) m3);
        }
        m3 = ((Map) m2).get("css");
        if (m3 instanceof Collection) {
            res.addAll((Collection) m3);
        }

        return res;
    }

}
