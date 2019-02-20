package jandcode.xcore.impl;

import jandcode.commons.error.*;

import java.util.*;

/**
 * Конвертор и фильтр ошибок для связанных с app классов.
 */
public class ErrorConvertorApp implements ErrorConvertor {

    protected List<String> filter = new ArrayList<>();

    public ErrorConvertorApp() {
        filter.add("BeanFactoryImpl");
        filter.add("BeanFactoryOwner");
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

    public List<ErrorSource> getErrorSource(Throwable e) {
        return null;
    }

    public boolean isFiltered(String className) {
        for (String s : filter) {
            if (className.indexOf(s) != -1) {
                return true;
            }
        }
        return false;
    }

}
