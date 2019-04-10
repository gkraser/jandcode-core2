package jandcode.web.gsp;

import jandcode.commons.*;
import jandcode.web.*;
import jandcode.web.gsp.impl.*;
import org.apache.commons.io.*;

import java.util.*;

/**
 * Базовый предок для реализаций Gsp.
 * Включает в дополнение к реализации Gsp набор утилитных методов.
 */
public abstract class BaseGsp extends GspImpl {

    /**
     * Реализация вывода шаблона
     */
    protected abstract void onRender() throws Exception;

    //////

    /**
     * Ссылка на {@link WebService}
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }

    /**
     * Запрос
     */
    public Request getRequest() {
        return getWebService().getRequest();
    }

    ////// text utils

    /**
     * Экранирование html символов
     */
    public String escapeHtml(Object text) {
        return UtString.xmlEscape(UtString.toString(text));
    }

    /**
     * Из map делает строку для использования в качестве атрибутов тега
     */
    public String htmlAttrs(Map attrs) {
        if (attrs == null || attrs.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object key : attrs.keySet()) {
            sb.append(' ');
            sb.append(key.toString());
            sb.append("=\"");
            sb.append(escapeHtml(attrs.get(key)));
            sb.append("\"");
        }
        return sb.toString();
    }

    ////// ref

    /**
     * Формирование ссылки.
     * см: {@link Request#ref(java.lang.String, boolean, java.util.Map)}
     */
    public String ref(String url, boolean virtualRoot, Map params) {
        return getRequest().ref(url, virtualRoot, params);
    }

    /**
     * см: {@link BaseGsp#ref(java.lang.String, boolean, java.util.Map)}
     */
    public String ref(String url, Map params) {
        return ref(url, true, params);
    }

    /**
     * см: {@link BaseGsp#ref(java.lang.String, boolean, java.util.Map)}
     */
    public String ref(String url) {
        return ref(url, true, null);
    }

    /**
     * см: {@link BaseGsp#ref(java.lang.String, boolean, java.util.Map)}
     */
    public String ref(Map params) {
        return ref(null, true, params);
    }

    ////// path

    /**
     * Возвращает полный путь для ресурса по пути, относительно пути шаблона.
     * <p>
     * Например, шаблон находится в файле '/jandcode/web/tst/template.gsp', тогда:
     * path('a.css') => '/jandcode/web/tst/a.css'
     * path('b/a.css') => '/jandcode/web/tst/b/a.css'
     * path('../a.css') => '/jandcode/web/a.css'
     *
     * @return параметр relPath, если не известен путь
     */
    public String path(String relPath) {
        String s = getWebService().getGspPath(getName());
        if (UtString.empty(s)) {
            BaseGsp own = (BaseGsp) getOwner();
            if (own == null) {
                return relPath;
            } else {
                return own.path(relPath);
            }
        }

        s = UtFile.path(s);
        s = s + "/" + relPath;
        s = FilenameUtils.normalize(s, true);
        if (UtString.empty(s)) {
            return relPath;
        }

        if (UtFile.isAbsolute(s)) {
            return s;
        }

        return UtVDir.normalize(s);
    }

    ////// context

    /**
     * Раскрытие подстановок <code>#{x}</code>. См {@link GspContext#substVar(java.lang.String)}.
     */
    public String substVar(String s) {
        return getContext().substVar(s);
    }

    /**
     * см {@link GspContext#create(java.lang.Class)}
     */
    public <A> A create(Class<A> cls) {
        return getContext().create(cls);
    }

    /**
     * см {@link BaseGsp#inst(java.lang.Class)}
     */
    public <A> A inst(Class<A> cls) {
        return getContext().inst(cls);
    }

}
