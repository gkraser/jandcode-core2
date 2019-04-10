package jandcode.web.gsp;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.web.*;

/**
 * Расширенный доступ к аргументам gsp.
 * Использование (внутри gsp):
 * <pre>{@code
 * GspArgsUtils ar = new GspArgsUtils(this);
 * ar.getPath('path1', true);
 * ...
 * }</pre>
 */
public class GspArgsUtils extends VariantMapWrap {

    protected BaseGsp gsp;

    /**
     * Ошибка: аргумент обязателен
     */
    public class ErrorRequired extends XError {
        public ErrorRequired(String key) {
            super("Аргумент [{0}] обязателен для gsp [{1}]", key, gsp.getName());
        }
    }

    public GspArgsUtils(BaseGsp gsp) {
        this.gsp = gsp;
        this.setMap(gsp.getArgs());
    }

    public Object get(Object key) {
        Object a = super.get(key);
        if (a != null && a instanceof CharSequence) {
            return gsp.substVar(a.toString());
        } else {
            return a;
        }
    }

    //////

    /**
     * Аргумент-строка с проверкой обязательности.
     *
     * @param name имя аргумента
     * @param req  обязательность аргумента
     */
    public String getString(String name, boolean req) {
        String res = getString(name);
        if (req && UtString.empty(res)) {
            throw new ErrorRequired(name);
        }
        return res;
    }

    //////


    /**
     * Для указанного пути возвращает true, если он относительный.
     * Это определяется по наличию './' в строке.
     */
    public boolean isRelPath(String p) {
        if (UtString.empty(p)) {
            return false;
        }
        if (UtWeb.isAbsoluteUrl(p)) {
            return false;
        }
        return UtVDir.isRelPath(p);
    }

    /**
     * Расширение файла
     *
     * @param s имя файла или url (в этом случае часть строки после '?' игнорируется)
     * @return пустая строка, если нет расширения
     */
    public String ext(String s) {
        if (UtString.empty(s)) {
            return "";
        }
        int a = s.indexOf('?');
        if (a != -1) {
            s = s.substring(0, a);
        }
        return UtFile.ext(s);
    }

    /**
     * Аргумент с указанным именем как путь.
     * Если путь относительный (cодержит './'), то он превращается в абсолютный
     *
     * @param name имя аргумента
     * @param req  обязательность аргумента
     */
    public String getPath(String name, boolean req) {
        String path = UtCnv.toString(get(name));

        if (isRelPath(path)) {
            path = gsp.path(path);
        }

        if (req && UtString.empty(path)) {
            throw new ErrorRequired(name);
        }

        return path;
    }

    /**
     * см. {@link GspArgsUtils#getPath(java.lang.String, boolean)},
     * где req=false
     */
    public String getPath(String name) {
        return getPath(name, false);
    }

}
