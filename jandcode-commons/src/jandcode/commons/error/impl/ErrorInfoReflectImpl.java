package jandcode.commons.error.impl;

import jandcode.commons.error.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Реализация ErrorInfo для получения ErrorInfo из другого ClassLoader
 */
@SuppressWarnings("unchecked")
public class ErrorInfoReflectImpl implements ErrorInfo {

    private Object originalErrorInfo;

    public ErrorInfoReflectImpl(Object originalErrorInfo) {
        this.originalErrorInfo = originalErrorInfo;
    }

    public Throwable getException() {
        Class cls = originalErrorInfo.getClass();
        try {
            Method m = cls.getMethod("getException");
            return (Throwable) m.invoke(originalErrorInfo);
        } catch (Exception e) {
            return e;
        }
    }

    public String getText() {
        Class cls = originalErrorInfo.getClass();
        try {
            Method m = cls.getMethod("getText");
            return (String) m.invoke(originalErrorInfo);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getTextStack(boolean filtered) {
        Class cls = originalErrorInfo.getClass();
        try {
            Method m = cls.getMethod("getTextStack", new Class[]{boolean.class});
            return (String) m.invoke(originalErrorInfo, filtered);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getTextErrorSource() {
        Class cls = originalErrorInfo.getClass();
        try {
            Method m = cls.getMethod("getTextErrorSource");
            return (String) m.invoke(originalErrorInfo);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<Throwable> getExceptions() {
        Class cls = originalErrorInfo.getClass();
        try {
            Method m = cls.getMethod("getExceptions");
            return (List<Throwable>) m.invoke(originalErrorInfo);
        } catch (Exception e) {
            return new ArrayList<Throwable>();
        }
    }
}
