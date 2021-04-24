package jandcode.core.web.std.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.env.*;
import jandcode.core.*;
import jandcode.core.web.cfg.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.std.gsp.*;

import java.util.*;

public class JsIndexGspContextImpl implements JsIndexGspContext, IAppLink {

    private GspContext gspContext;
    private String title;
    private Conf cfg;
    private List<Link> links = new ArrayList<>();

    class Link {
        String path;
        boolean js;

        public Link(String path, boolean js) {
            this.path = path;
            this.js = js;
        }
    }

    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    public App getApp() {
        return this.gspContext.getApp();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Conf getCfg() {
        if (this.cfg == null) {
            ClientCfgService cfgSvc = getApp().bean(ClientCfgService.class);
            Env env = getApp().getEnv();

            Conf tmpBase = UtConf.create();

            // базовые умолчания
            tmpBase.setValue("baseUrl", gspContext.getRootGsp().ref("/"));
            if (env.isDev()) {
                tmpBase.setValue("envDev", true);
            }
            if (env.isSource()) {
                tmpBase.setValue("envSource", true);
            }

            // собираем
            Conf tmpGrab = cfgSvc.grabClientCfg();


            this.cfg = UtConf.create();
            // сначала base
            this.cfg.join(tmpBase);
            // потом собранную
            this.cfg.join(tmpGrab);
            // и в конце снова базовую, что бы никто это не перекрыл по глупости
            this.cfg.join(tmpBase);

        }
        return this.cfg;
    }

    public void addLink(String path) {
        String ext = UtFile.ext(path);
        boolean isCss = ext.equalsIgnoreCase("css")
                || ext.equalsIgnoreCase("less")
                || ext.equalsIgnoreCase("scss")
                || ext.equalsIgnoreCase("sass");
        this.links.add(new Link(path, !isCss));
    }

    public void addLink(String path, boolean isJs) {
        this.links.add(new Link(path, isJs));
    }

    public void outLinks() {
        BaseGsp gsp = gspContext.getCurrentGsp();
        outSystemScript(gsp);

        // сначала css
        for (var link : this.links) {
            if (link.js) {
                continue;
            }
            outLinkCss(gsp, link);
        }

        // потом js
        for (var link : this.links) {
            if (!link.js) {
                continue;
            }
            outLinkJs(gsp, link);
        }

    }

    protected void outSystemScript(BaseGsp gsp) {
        boolean dev = getApp().getEnv().isDev();
        gsp.out("<script>");
        if (dev) {
            gsp.out("\n");
        }
        gsp.out("window.Jc_cfg=");
        gsp.out(UtJson.toJson(getCfg(), dev));
        if (dev) {
            gsp.out("\n");
        }
        gsp.out("</script>\n");
    }

    protected void outLinkJs(BaseGsp gsp, Link link) {
        String icref = gsp.ref(link.path);
        gsp.out("<script src=\"");
        gsp.out(icref);
        gsp.out("\"></script>\n");
    }

    protected void outLinkCss(BaseGsp gsp, Link link) {
        String icref = gsp.ref(link.path);
        gsp.out("<link rel=\"stylesheet\" href=\"");
        gsp.out(icref);
        gsp.out("\"/>\n");
    }

}
