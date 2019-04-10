package jandcode.web.gsp.impl;

import groovy.lang.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.gsp.*;

import java.util.*;

public abstract class GspImpl extends BaseComp implements Gsp {

    private GspContextImpl context;
    private IVariantMap args = new VariantMap();
    private Gsp owner;
    private boolean removeFromPool;

    ////// body

    public abstract class Body {
        public abstract void render();
    }

    public class BodyRunnable extends Body {
        Runnable runnable;

        public BodyRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public void render() {
            runnable.run();
        }
    }

    public class BodyClosure extends Body {
        Closure closure;

        public BodyClosure(Closure closure) {
            this.closure = closure;
        }

        public void render() {
            closure.call();
        }
    }

    public class BodyCharSequence extends Body {
        CharSequence s;

        public BodyCharSequence(CharSequence s) {
            this.s = s;
        }

        public void render() {
            out(s);
        }
    }

    //////

    void bindContext(GspContextImpl context) {
        this.context = context;
    }

    void bindOwner(Gsp owner) {
        this.owner = owner;
    }

    void bindArgs(IVariantMap args) {
        this.args = args;
    }

    /**
     * Установить делегата. После этого вызова объект будет вести себя как delegate,
     * но может иметь дополнительные методы. Используется для создания утилит-расширений
     * для Gsp.
     */
    protected void setDelegate(Gsp delegate) {
        this.setApp(delegate.getApp());
        this.setName(delegate.getName());
        this.context = ((GspImpl) delegate).context;
        this.args = ((GspImpl) delegate).args;
        this.owner = ((GspImpl) delegate).owner;
    }

    //////

    public void renderTo(GspContext gspContext) throws Exception {
        if (gspContext != context) {
            throw new XError("gsp создан не в том контексте, в который собирается выводится");
        }
        onRender();
    }

    /**
     * Реализация render
     */
    protected abstract void onRender() throws Exception;

    public Gsp getOwner() {
        return owner;
    }

    public void out(Object s) {
        context.out(s);
    }

    ////// out tag

    public void outTag(String name, Map args, Closure cls) throws Exception {
        context.outGsp(name, args, cls);
    }

    public void outTag(Map args, String name, Closure cls) throws Exception {
        context.outGsp(name, args, cls);
    }

    public void outTag(Map args, String name) throws Exception {
        context.outGsp(name, args, null);
    }

    public void outTag(String name) throws Exception {
        context.outGsp(name, null, null);
    }

    public void outTag(String name, Map args) throws Exception {
        context.outGsp(name, args, null);
    }

    public void outTag(String name, Closure cls) throws Exception {
        context.outGsp(name, null, cls);
    }

    //////

    public ITextBuffer pushBuffer() {
        return context.pushBuffer();
    }

    public ITextBuffer popBuffer() {
        return context.popBuffer();
    }

    public ITextBuffer curBuffer() {
        return context.curBuffer();
    }

    public IVariantMap getArgs() {
        return args;
    }

    protected Body convertToBody(Object z) {
        if (z instanceof Body) {
            return (Body) z;
        } else if (z instanceof Closure) {
            return new BodyClosure((Closure) z);
        } else if (z instanceof Runnable) {
            return new BodyRunnable((Runnable) z);
        } else if (z instanceof CharSequence) {
            return new BodyCharSequence((CharSequence) z);
        } else {
            return null; // не поддерживается
        }
    }

    protected Object getBody() {
        Object a = getArgs().get(ARG_BODY);
        if (a == null) {
            return null;
        }
        if (a instanceof Body) {
            return a;
        }
        Body b = convertToBody(a);
        if (b != null) {
            getArgs().put(ARG_BODY, b);
        }
        return b;
    }

    public boolean hasBody() {
        return getBody() != null;
    }

    public void outBody() {
        Body b = (Body) getBody();
        if (b == null) {
            return;
        }
        b.render();
    }

    public ITextBuffer grabBody() {
        Body b = (Body) getBody();
        if (b == null) {
            return new TextBufferImpl();
        }
        ITextBuffer buf = pushBuffer();
        try {
            b.render();
        } finally {
            popBuffer();
        }
        return buf;
    }

    public void include(String gspName) throws Exception {
        context.includeGsp(this, gspName);
    }

    public void include(Gsp to, String gspName) throws Exception {
        context.includeGsp(to, gspName);
    }

    //////

    public GspContext getContext() {
        return context;
    }

    //////


    public boolean isRemoveFromPool() {
        return removeFromPool;
    }

    public void setRemoveFromPool(boolean removeFromPool) {
        this.removeFromPool = removeFromPool;
    }

}
