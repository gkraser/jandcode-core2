package jandcode.jc.impl.gsp;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.variant.*;
import jandcode.jc.*;
import org.codehaus.groovy.runtime.*;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class GspScriptImpl extends ProjectScript implements IGspScript {

    private String charset = UtString.UTF8;
    private GenContext genContext;
    private GspScriptImpl ownerGspScript;
    private boolean doGenerateExecuted;

    public void generate(String outFileName, Map args) {
        if (genContext != null) {
            throw new XError("Нельзя использовать метод generate в процессе генерации");
        }
        String basedir = UtFile.path(outFileName);
        if (UtString.empty(basedir)) {
            throw new XError("Для генерации нужно указывать полное имя файла");
        }
        String fn = UtFile.filename(outFileName);
        this.genContext = new GenContext(basedir, getCharset());
        if (args != null) {
            this.genContext.getArgs().putAll(args);
        }
        try {
            pushFile(fn);
            doGenerate();

        } catch (Exception e) {
            throw new XErrorWrap(e);

        } finally {
            this.genContext.close();
            this.genContext = null;
        }
    }

    public void generate(String outFileName) {
        generate(outFileName, null);
    }

    protected void doGenerate() throws Exception {
        this.doGenerateExecuted = true;
        onGenerate();
    }

    protected abstract void onGenerate() throws Exception;


    protected GenContext getGenContext() {
        if (genContext == null) {
            if (ownerGspScript != null) {
                return ownerGspScript.getGenContext();
            }
            throw new XError("Генерация не запущена");
        }
        return genContext;
    }

    ////// inject

    public IProjectScript create(String scriptName) {
        // если создается GspScript, назначаем ему себя как хозяина и котекст вывода соотвественно
        IProjectScript ps = super.create(scriptName);
        if (ps instanceof GspScript) {
            // это gsp-скрипт
            ((GspScriptImpl) ps).ownerGspScript = this;
        }
        return ps;
    }

    public Vars getVars() {
        // если vars вызывается в дочернем шаблоне, то нужно его фейково отрендерить
        if (this.ownerGspScript != null && !this.doGenerateExecuted) {
            // это дочерний и генерацию еще не делали, т.е. vars не заполнен функциями
            GenContext saveGenContext = this.genContext;
            this.genContext = new GenContext(getGenContext().getBasedir(), true);
            try {
                this.genContext.pushFile("__fakemode__", false);
                doGenerate();

            } catch (Exception e) {
                throw new XErrorWrap(e);

            } finally {
                this.genContext = saveGenContext;
            }
        }
        return super.getVars();
    }

    ////// IGspGen

    public IVariantMap getArgs() {
        if (this.ownerGspScript != null) {
            return this.ownerGspScript.getArgs();
        }
        return getGenContext().getArgs();
    }

    public void changeFile(String filename, boolean append) {
        getGenContext().popFile();
        getGenContext().pushFile(filename, append);
    }

    public void changeFile(String filename) {
        changeFile(filename, false);
    }

    public void pushFile(String filename, boolean append) {
        getGenContext().pushFile(filename, append);
    }

    public void pushFile(String filename) {
        pushFile(filename, false);
    }

    public void popFile() {
        if (getGenContext().getStackSize() < 2) {
            throw new XError("popFile без pushFile");
        }
        getGenContext().popFile();
    }

    public IndentWriter getWriter() {
        return getGenContext().getCurrentFile().getWriter();
    }

    public String getCurrentFile() {
        return getGenContext().getCurrentFile().getFilename();
    }

    public String getOutDir() {
        return getGenContext().getBasedir();
    }

    //////

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = UtString.empty(charset) ? UtString.UTF8 : charset;
    }

    public void out(Object s) throws Exception {
        IndentWriter w = getGenContext().getCurrentFile().getWriter();
        //
        if (s != null) {
            if (s instanceof CharSequence) {
                w.write(s.toString());

            } else if (s instanceof Map) {
                w.write(DefaultGroovyMethods.toMapString((Map) s));

            } else if (s instanceof Collection) {
                w.write(DefaultGroovyMethods.toListString((Collection) s));

            } else if (s instanceof GspScriptImpl) {
                GspScriptImpl gs = ((GspScriptImpl) s);
                if (gs.getGenContext() != getGenContext()) {
                    throw new XError("out для GspScript, который не создан внутри выводимого GspScript");
                }
                gs.doGenerate();

            } else {
                w.write(s.toString());
            }
        }
        //
    }

}
