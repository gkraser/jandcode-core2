package jandcode.core.launcher.impl;

import jandcode.commons.*;
import jandcode.core.launcher.*;

public class OptBuilderImpl implements OptBuilder {

    private OptImpl inst;

    public OptBuilderImpl(OptImpl inst) {
        this.inst = inst;
    }

    public OptBuilder desc(String v) {
        inst.setDesc(v);
        return this;
    }

    public OptBuilder arg(String v) {
        inst.setArgName(v);
        return this;
    }

    public OptBuilder arg(boolean v) {
        if (v) {
            inst.setArgName("ARG");
        } else {
            inst.setArgName("");
        }
        return this;
    }

    public OptBuilder defaultArgValue(Object v) {
        String s = UtCnv.toString(v);
        inst.setDefaultArgValue(s);
        if (!inst.hasArg()) {
            arg(true);
        }
        return this;
    }

}
