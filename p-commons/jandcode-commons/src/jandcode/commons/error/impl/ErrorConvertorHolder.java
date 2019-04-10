package jandcode.commons.error.impl;

import jandcode.commons.error.*;

import java.util.*;

public class ErrorConvertorHolder implements ErrorConvertor {

    private List<ErrorConvertor> items = new ArrayList<ErrorConvertor>();

    public List<ErrorConvertor> getItems() {
        return items;
    }

    public String getText(Throwable e) {
        for (ErrorConvertor q : items) {
            String s = q.getText(e);
            if (s != null) {
                return s;
            }
        }
        return e.toString();
    }

    public Throwable getReal(Throwable e) {
        for (ErrorConvertor q : items) {
            Throwable s = q.getReal(e);
            if (s != null) {
                return s;
            }
        }
        return e;
    }

    public Throwable getNext(Throwable e) {
        for (ErrorConvertor q : items) {
            Throwable s = q.getNext(e);
            if (s != null) {
                return s;
            }
        }
        return e;
    }

    public Boolean isMark(Throwable e) {
        for (ErrorConvertor q : items) {
            if (q.isMark(e)) {
                return true;
            }
        }
        return false;
    }

    public List<ErrorSource> getErrorSource(Throwable e) {
        List<ErrorSource> res = new ArrayList<ErrorSource>();
        for (ErrorConvertor q : items) {
            List<ErrorSource> s = q.getErrorSource(e);
            if (s != null) {
                res.addAll(s);
            }
        }
        if (res.size() == 0) {
            return null;
        }
        return res;
    }

    public boolean isFiltered(String className) {
        for (ErrorConvertor q : items) {
            if (q.isFiltered(className)) {
                return true;
            }
        }
        return false;
    }

}
