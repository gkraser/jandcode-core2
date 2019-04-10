package jandcode.commons.groovy.impl;

import jandcode.commons.error.*;
import org.codehaus.groovy.control.*;
import org.codehaus.groovy.control.messages.*;

import java.util.*;

public class ErrorConvertorGroovy implements ErrorConvertor {

    private GroovyManager gmanager;

    public ErrorConvertorGroovy(GroovyManager gmanager) {
        this.gmanager = gmanager;
    }

    public List<ErrorSource> getErrorSource(Throwable e) {
        List<ErrorSource> res = new ArrayList<ErrorSource>();
        //
        if (e instanceof MultipleCompilationErrorsException) {
            // ошибки компиляции
            MultipleCompilationErrorsException eg = (MultipleCompilationErrorsException) e;
            for (Object ee : eg.getErrorCollector().getErrors()) {
                Message mm = (Message) ee;
                if (mm instanceof SyntaxErrorMessage) {
                    SyntaxErrorMessage sm = (SyntaxErrorMessage) mm;
                    int line = sm.getCause().getLine();
                    ErrorSource es = gmanager.getErrorSource(e.toString(), line);
                    if (es != null) {
                        res.add(es);
                    }
                }
            }
        } else {
            // остальные - ошибки выполнения
            for (StackTraceElement st : e.getStackTrace()) {
                ErrorSource es = gmanager.getErrorSource(st);
                if (es != null) {
                    res.add(es);
                }
            }
        }
        if (res.size() == 0) {
            return null;
        }
        return res;
    }

    public String getText(Throwable e) {
        return null;
    }

    public Throwable getReal(Throwable e) {
        return null;
    }

    public Throwable getNext(Throwable e) {
        return null;
    }

    public Boolean isMark(Throwable e) {
        return null;
    }

    public boolean isFiltered(String className) {
        return false;
    }

}
