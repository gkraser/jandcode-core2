package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.verdb.*;

public class VerdbModuleDefImpl extends Named implements VerdbModuleDef {

    private Model model;
    private Conf conf;
    private String path;
    private String moduleName;

    public VerdbModuleDefImpl(Model model, Conf conf) {
        this.model = model;
        this.conf = conf;
        setName(conf.getName());
        UtReflect.getUtils().setProps(this, conf);
        validate();
    }

    private void validate() {
        //
        if (UtString.empty(this.moduleName)) {
            this.moduleName = getModel().getModelDef().getInstanceOf().getName();
        }
        //
        if (UtString.empty(this.path)) {
            throw new XError("Для verdb-module [{0}] не указан path: {1}",
                    getName(), this.conf.origin());
        }
        //
        this.path = UtApp.getFileObject(getModel().getApp(), this.path).toString();
        if (!UtFile.existsFileObject(this.path)) {
            throw new XError("Для verdb-module [{0}] указан не существующий путь [{1}]: {2}",
                    getName(), this.path, this.conf.origin());
        }
    }

    public Model getModel() {
        return model;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public VerdbModule createInst() {
        VerdbModuleImpl res = new VerdbModuleImpl(getModel(), getPath(), getModuleName());
        res.getDirs();  // сразу грузим каталоги
        return res;
    }
}
