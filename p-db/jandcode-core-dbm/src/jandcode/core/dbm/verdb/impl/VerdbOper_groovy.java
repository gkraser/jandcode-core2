package jandcode.core.dbm.verdb.impl;

import jandcode.commons.groovy.*;
import jandcode.core.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.verdb.*;
import jandcode.core.groovy.*;

import java.lang.reflect.*;

public class VerdbOper_groovy extends BaseVerdbOper {

    private GroovyClazz cls;

    public VerdbOper_groovy(VerdbFile file, long versionNum3, String text) {
        super(file, versionNum3);
        setText(text);
    }

    public void exec(Mdb mdb) throws Exception {
        if (cls == null) {
            App app = getFile().getDir().getModule().getModel().getApp();
            GroovyCompiler compiler = app.bean(GroovyService.class).getGroovyCompiler(VerdbModule.class.getName());
            this.cls = compiler.getClazz(Object.class, GroovyCompiler.SIGN_CLASS, getText(), false);
        }
        Object inst = this.cls.createInst();
        Method method = inst.getClass().getMethod("exec", Mdb.class);
        method.invoke(inst, mdb);
    }
}
