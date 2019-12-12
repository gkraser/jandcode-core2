package jandcode.commons.error.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import org.xml.sax.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Конвертор по умолчанию
 */
public class ErrorConvertorDefault implements ErrorConvertor {

    protected List<String> filter = new ArrayList<String>();

    public ErrorConvertorDefault() {
        filter.add("sun."); //NON-NLS
        filter.add("java."); //NON-NLS
        filter.add("org.junit."); //NON-NLS
        filter.add("com.intellij."); //NON-NLS
        filter.add("jandcode.commons.test.Utils_Test");
        filter.add("org.apache.commons."); //NON-NLS
        filter.add("org.apache.catalina"); //NON-NLS
        filter.add("org.apache.coyote"); //NON-NLS
        filter.add("org.apache.tomcat"); //NON-NLS
        filter.add("javax.servlet"); //NON-NLS
        filter.add("oracle.jdbc"); //NON-NLS
        filter.add("groovy.lang"); //NON-NLS
        filter.add("org.codehaus.groovy"); //NON-NLS
        filter.add("io.undertow"); //NON-NLS
    }

    public String getText(Throwable e) {
        Class ec = e.getClass();
        if (ec == ClassNotFoundException.class) {
            return UtLang.t("Класс не найден: {0}", e.getMessage());
        } else if (ec == NullPointerException.class) {
            String m = "Null pointer";
            String s = e.getMessage();
            if (!UtString.empty(s)) {
                m = m + ": " + s;
            }
            return m;
        } else if (ec == StackOverflowError.class) {
            return "Stack overflow";
        } else if (ec == FileNotFoundException.class) {
            return UtLang.t("Файл не найден: {0}", e.getMessage());
        } else if (ec == SAXParseException.class) {
            SAXParseException e1 = (SAXParseException) e;
            return e1.getMessage() + " (col:" + e1.getColumnNumber() +  //NON-NLS
                    ", row:" + e1.getLineNumber() + ")";
        } else if (ec == NoClassDefFoundError.class) {
            return UtLang.t("Определение класса не найдено: {0}", e.getMessage());
        } else if (ec == UnsupportedEncodingException.class) {
            return UtLang.t("Не поддерживаемая кодировка для: {0}", e.getMessage());
        } else if (ec == InstantiationException.class) {
            return UtLang.t("Не возможно создать экземпляр класса: {0}", e.getMessage());
        }
        //
        String s = e.getMessage();
        if (UtString.empty(s)) {
            s = e.toString();
        }
        return s;
    }

    public Throwable getReal(Throwable e) {
        if (e.getCause() == e) {
            return e;
        }
        String cn = e.getClass().getName();
        if (e.getClass() == XErrorWrap.class ||
                cn.equals("org.codehaus.groovy.runtime.InvokerInvocationException")) {
            if (e.getCause() != null) {
                return UtError.getErrorConvertor().getReal(e.getCause());
            }
        } else if (e.getClass() == Exception.class ||
                e.getClass() == RuntimeException.class) {
            if (e.getCause() != null) {
                String myMsg = e.getMessage();
                String caMsg = e.getCause().getClass().getName() + ": " + e.getCause().getMessage();
                if (myMsg.equals(caMsg)) {
                    return UtError.getErrorConvertor().getReal(e.getCause());
                }
            }
        } else if (e.getClass() == InvocationTargetException.class) {
            InvocationTargetException e1 = (InvocationTargetException) e;
            if (e1.getTargetException() != null) {
                return UtError.getErrorConvertor().getReal(e1.getTargetException());
            }
        }
        return e;
    }

    public Throwable getNext(Throwable e) {
        return e.getCause();
    }

    public Boolean isMark(Throwable e) {
        return e instanceof IErrorMark;
    }

    public List<ErrorSource> getErrorSource(Throwable e) {
        return null;
    }

    public boolean isFiltered(String className) {
        if (className.contains("$$")) {
            return true; // сильно внутренний класс
        }
        for (String s : filter) {
            if (className.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

}