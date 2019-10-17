package jandcode.core.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

public class JsModuleBuilderImpl implements JsModuleBuilder {

    private JsModuleService svc;
    private JsModule module;
    private Conf conf;
    private String text;
    private List<VirtFile> modifyDepends = new ArrayList<>();
    private List<RequireItem> requires = new ArrayList<>();
    private boolean dynamic;

    public JsModuleBuilderImpl(JsModule module, JsModuleService svc, Conf conf) {
        this.module = module;
        this.svc = svc;
        this.conf = conf;
        addModifyDepend(module.getName());
    }

    public JsModule getModule() {
        return module;
    }

    public Conf getConf() {
        if (this.conf == null) {
            this.conf = UtConf.create();
        }
        return conf;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text == null ? "" : this.text;
    }

    public void addModifyDepend(String path) {
        WebService webSvc = svc.getApp().bean(WebService.class);
        VirtFile f = webSvc.findFile(path);
        if (f != null) {
            this.modifyDepends.add(f);
        }
    }

    public List<VirtFile> getModifyDepends() {
        return modifyDepends;
    }

    public void addRequire(String path) {
        String folderPath = getModule().getFile().getFolderPath();
        List<String> r = this.svc.resolveRequire(folderPath, path);
        RequireItem ri = new RequireItemImpl(path, r);
        this.requires.add(ri);
    }

    public List<RequireItem> getRequires() {
        return requires;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public VirtFile findCompiled(String suff) {
        String fn = "_jsa/_compiled/" + getModule().getName() + "--compiled";
        if (!UtString.empty(suff)) {
            fn = fn + "-" + suff;
        }
        return svc.getApp().bean(WebService.class).findFile(fn);
    }

}
