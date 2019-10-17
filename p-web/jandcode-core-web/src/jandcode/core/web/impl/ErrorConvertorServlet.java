package jandcode.core.web.impl;

import jandcode.commons.error.*;

import javax.servlet.*;
import java.util.*;

public class ErrorConvertorServlet implements ErrorConvertor {

    public String getText(Throwable e) {
        return null;
    }

    public Throwable getReal(Throwable e) {
        if (e instanceof ServletException) {
            return ((ServletException) e).getRootCause();
        }
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
        return false;
    }

}
