package jandcode.core.web.gsp.impl;

import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;
import org.apache.commons.vfs2.*;

@SuppressWarnings("unchecked")
public abstract class CustomGspDefFileObject extends BaseComp implements GspDef {

    protected abstract FileObject getFileObject();

    private GroovyClazz groovyClazz;

    protected GroovyClazz getGroovyClazz() {
        if (groovyClazz == null) {
            synchronized (this) {
                if (groovyClazz == null) {
                    WebService svc = getApp().bean(WebService.class);
                    try {
                        groovyClazz = svc.compileGsp(getFileObject());
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                }
            }
        }
        return groovyClazz;
    }

    public Gsp createInst() {
        Gsp z = (Gsp) getApp().create(getGroovyClazz().getClazz());
        z.setName(getName());
        return z;
    }

    public String getGspSourceText() {
        return getGroovyClazz().getSourceOriginal();
    }

    public String getGspClassText() {
        return getGroovyClazz().getSourceClazz();
    }

}
