package jandcode.core.web.std.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.web.cfg.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.std.gsp.*;

public class JsIndexGspContextImpl implements JsIndexGspContext {

    private GspContext gspContext;
    private String title;
    private Conf cfg;


    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Conf getCfg() {
        if (this.cfg == null) {
            ClientCfgService cfgSvc = gspContext.getApp().bean(ClientCfgService.class);
            this.cfg = cfgSvc.grabClientCfg();
        }
        return this.cfg;
    }

    public String getCfgJson() {
        return UtJson.toJson(getCfg());
    }
}
