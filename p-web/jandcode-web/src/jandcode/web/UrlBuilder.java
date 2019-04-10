package jandcode.web;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Простой построитель url по запчастям
 */
public class UrlBuilder {

    private String prefix = "";
    private StringBuilder path = new StringBuilder();
    private LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
    private boolean root;

    protected String normalizePath(String path) {
        if (path == null) {
            return "";
        }
        if (path.indexOf("..") != -1) {
            path = path.replace("..", "");
        }
        if (path.indexOf('\\') != -1) {
            path = path.replace("\\", "/");
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * Добавить стрку к пути.
     * Допустимы строки вида:
     * 'http://ddddd', 'ddd/fff', 'ddd/ff?dd=55&ddd=55', 'ddd=44&dddd=5'
     *
     * @param s
     */
    public void append(String s) {
        if (UtString.empty(s)) {
            return;
        }

        String p = "";
        int q = s.indexOf('?');
        if (q != -1) {
            p = s.substring(q + 1);
            s = s.substring(0, q);
        } else {
            if (s.indexOf('=') != -1) {
                appendParams(s);
                return;
            }
        }

        if (s.length() > 0) {
            s = normalizePath(s);
            if (UtWeb.isAbsoluteUrl(s)) {
                prefix = s;
            } else {
                if (path.length() > 0) {
                    path.append('/');
                }
                path.append(s);
            }
        }

        if (p.length() > 0) {
            appendParams(p);
        }
    }

    public void appendParams(String params) {
        if (UtString.empty(params)) {
            return;
        }
        try {
            StringTokenizer st1 = new StringTokenizer(params, "&");
            while (st1.hasMoreTokens()) {
                String tk = st1.nextToken();
                StringTokenizer st2 = new StringTokenizer(tk, "=");
                try {
                    String tk2p = st2.nextToken();
                    String tk2v = st2.nextToken();
                    this.params.put(tk2p,
                            URLDecoder.decode(tk2v, UtString.UTF8));
                } catch (NoSuchElementException e) {
                    this.params.put(tk, "");
                }
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    /**
     * добавить параметр
     *
     * @param name  имя
     * @param value значение
     */
    public void append(Object name, Object value) {
        params.put(UtString.toString(name), UtString.toString(value));
    }

    /**
     * добавить параметры
     */
    public void append(Map params) {
        if (params == null) {
            return;
        }
        for (Object o : params.keySet()) {
            append(o, params.get(o));
        }
    }

    /**
     * Про установке значения true, url будет начинаться с '/'
     */
    public void setRoot(boolean root) {
        this.root = root;
    }

    /**
     * Получить текст url
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (prefix.length() > 0) {
            sb.append(prefix);
        }
        if (path.length() > 0) {
            if (sb.length() > 0 || root) {
                sb.append('/');
            }
            sb.append(path);
        } else {
            if (root && sb.length() == 0) {
                sb.append("/");
            }
        }
        if (params.size() > 0) {
            sb.append('?');
            boolean first = true;
            try {
                for (Map.Entry<String, String> en : params.entrySet()) {
                    if (!first) {
                        sb.append('&');
                    }
                    sb.append(URLEncoder.encode(en.getKey(), UtString.UTF8));
                    if (!UtString.empty(en.getValue())) {
                        sb.append("=").append(URLEncoder.encode(en.getValue(), UtString.UTF8));
                    }
                    first = false;
                }
            } catch (UnsupportedEncodingException e) {
                throw new XErrorWrap(e);
            }
        }
        return sb.toString();
    }

    /**
     * Текущий набор параметров. Можно изменять.
     * Формируется при вызове append, если строка содержит параметры.
     */
    public LinkedHashMap<String, String> getParams() {
        return params;
    }
}
