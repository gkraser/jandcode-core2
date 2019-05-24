package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.jc.*;
import jandcode.jc.std.*;

public class LibModuleInfoImpl extends Named implements LibModuleInfo {

    private Ctx ctx;
    private Lib lib;
    private Conf conf;

    public LibModuleInfoImpl(Ctx ctx, Lib lib, String name) {
        this.ctx = ctx;
        setName(name);
        this.lib = lib;
    }

    public Conf getConf() {
        if (conf == null) {
            conf = loadConf();
        }
        return conf;
    }

    private Conf loadConf() {
        try {
            if (lib.getSourceProject() != null) {
                return loadConfSrc();
            } else {
                return loadConfJar();
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    private Conf loadConfSrc() throws Exception {
        JavaProject jp = lib.getSourceProject().getIncluded(JavaProject.class);
        if (jp == null) {
            throw new XError("No JavaProject in " + getName());
        }
        ModuleDefInfo mdi = jp.getModuleDefInfos().get(getName());
        if (mdi == null) {
            throw new XError("No moduleDefInfo for " + getName());
        }
        //
        Conf conf = UtConf.create();
        UtConf.load(conf).fromFileObject(mdi.getModuleDef().getModuleFile());
        //
        return conf;
    }

    private Conf loadConfJar() throws Exception {
        String mf = "jar:file:///" + lib.getJar() + "!/" + getName().replace('.', '/') + '/' + ModuleDefConsts.FILE_MODULE_CONF;
        Conf conf = UtConf.create();
        UtConf.load(conf).fromFileObject(mf);
        //
        return conf;
    }

}
