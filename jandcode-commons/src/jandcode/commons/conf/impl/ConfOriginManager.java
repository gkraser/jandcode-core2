package jandcode.commons.conf.impl;

import jandcode.commons.conf.*;

public class ConfOriginManager {

    private static ConfOriginManager inst = new ConfOriginManager();

    public static ConfOriginManager getInst() {
        return inst;
    }

    public void addOrigin(Conf conf, String prop, String fn, int lineNum) {
        if (conf == null) {
            return;
        }
        if (!(conf instanceof ConfImpl)) {
            return;
        }
        if (fn == null) {
            return;
        }
        ConfImpl ci = (ConfImpl) conf;
        if (ci.origin == null) {
            ci.origin = new ConfOriginImpl();
        }
        if (prop == null) {
            // для самого объекта
            ci.origin.addPos(fn, lineNum);
        } else {
            Object v = ci.getValue(prop);
            if (v instanceof ConfImpl) {
                addOrigin((ConfImpl) v, null, fn, lineNum);
            } else {
                ci.origin.addPosProp(prop, fn, lineNum);
            }
        }
    }


}
