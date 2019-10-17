package jandcode.core.web.test.dummy;


import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

@SuppressWarnings({"deprecation"})
public class HttpSessionDummy implements HttpSession {

    protected HashMap<String, Object> attrs = new HashMap<String, Object>();

    public long getCreationTime() {
        return 0;
    }

    public String getId() {
        return null;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public void setMaxInactiveInterval(int i) {

    }

    public int getMaxInactiveInterval() {
        return 0;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getAttribute(String s) {
        return attrs.get(s);
    }

    public Object getValue(String s) {
        return null;
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public String[] getValueNames() {
        return new String[0];
    }

    public void setAttribute(String s, Object o) {
        attrs.put(s, o);
    }

    public void putValue(String s, Object o) {

    }

    public void removeAttribute(String s) {

    }

    public void removeValue(String s) {

    }

    public void invalidate() {

    }

    public boolean isNew() {
        return false;
    }
}
