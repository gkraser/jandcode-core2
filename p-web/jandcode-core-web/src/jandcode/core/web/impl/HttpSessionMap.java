package jandcode.core.web.impl;

import jandcode.commons.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Представление HttpSession как Map
 */
@SuppressWarnings("unchecked")
public class HttpSessionMap implements Map<String, Object> {

    private HttpSession httpSession;

    public HttpSessionMap(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    ////// map

    public int size() {
        int res = 0;
        Enumeration itr = getHttpSession().getAttributeNames();
        while (itr.hasMoreElements()) {
            res++;
            itr.nextElement();
        }
        return res;
    }

    public boolean isEmpty() {
        Enumeration itr = getHttpSession().getAttributeNames();
        while (itr.hasMoreElements()) {
            return false;
        }
        return true;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue");
    }

    public Object get(Object key) {
        return httpSession.getAttribute(UtString.toString(key));
    }

    public Object put(String key, Object value) {
        httpSession.setAttribute(key, value);
        return null;
    }

    public Object remove(Object key) {
        httpSession.removeAttribute(UtString.toString(key));
        return null;
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        if (m != null) {
            for (String s : m.keySet()) {
                put(s, m.get(s));
            }
        }
    }

    public void clear() {
    }

    public Set<String> keySet() {
        Set<String> res = new HashSet<String>();
        Enumeration itr = httpSession.getAttributeNames();
        while (itr.hasMoreElements()) {
            res.add(UtString.toString(itr.nextElement()));
        }
        return res;
    }

    public Collection<Object> values() {
        List res = new ArrayList();
        Enumeration itr = httpSession.getAttributeNames();
        while (itr.hasMoreElements()) {
            String an = UtString.toString(itr.nextElement());
            res.add(httpSession.getAttribute(an));
        }
        return res;
    }

    public Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }
}
