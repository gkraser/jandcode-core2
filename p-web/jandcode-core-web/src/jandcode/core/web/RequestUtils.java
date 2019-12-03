package jandcode.core.web;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.web.action.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.impl.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Враппер для {@link Request} с набором утилит.
 * Реализует интерфейсы IVariantNamed, IVariantNamedDefault через прокси к getParams().
 * Т.е. getString("name") эквивалентно getParams().getString("name").
 */
public class RequestUtils extends RequestWrapper implements IVariantNamed, IVariantNamedDefault {

    public RequestUtils(Request request) {
        super(request);
    }

    //////

    /**
     * Ссылка на {@link WebService}
     */
    public WebService getWebService() {
        return getApp().bean(WebService.class);
    }

    ////// render

    /**
     * Рендерить gsp
     */
    public void renderGsp(String gspName) {
        render(new GspTemplate(gspName));
    }

    /**
     * Рендерить gsp
     */
    public void renderGsp(String gspName, Map args) {
        render(new GspTemplate(gspName, args));
    }

    /**
     * Рендерить gsp
     */
    public void renderGsp(String gspName, Map args, String contentType) {
        render(new GspTemplate(gspName, args, contentType));
    }

    ////// attrs

    /**
     * Имя запрашиваемого метода action.
     * Берется из атрибута {@link WebConsts#a_actionMethod}.
     * Если отсутсвует - возвращает 'index'.
     */
    public String getActionMethod() {
        String methodName = getAttrs().getString(WebConsts.a_actionMethod);
        if (UtString.empty(methodName)) {
            methodName = "index";
        }
        return methodName;
    }

    /**
     * pathInfo для метода action.
     * Берется из атрибута {@link WebConsts#a_actionMethodPathInfo}.
     */
    public String getActionMethodPathInfo() {
        return getAttrs().getString(WebConsts.a_actionMethodPathInfo);
    }

    /**
     * pathInfo для action.
     * Берется из атрибута {@link WebConsts#a_actionPathInfo}.
     */
    public String getActionPathInfo() {
        return getAttrs().getString(WebConsts.a_actionPathInfo);
    }

    ////// methods

    /**
     * Выполнить метод action
     *
     * @param inst для какого экземпляра
     * @param name имя метода
     */
    public void execActionMethod(Object inst, String name) throws Exception {
        Method m = findActionMethod(inst, name);
        if (m == null) {
            throw new HttpError(404, "action method not found: " + name);
        }

        Class[] prms = m.getParameterTypes();
        if (prms.length == 1 && ActionMethodWrapper.class.isAssignableFrom(prms[0])) {
            // есть wrapper
            ActionMethodWrapper wrap = (ActionMethodWrapper) getApp().create(prms[0]);
            wrap.execActionMethod(inst, m);
        } else {
            Object res = m.invoke(inst);
            if (m.getReturnType() != void.class) {
                // если метод может возвратить значение
                // то безусловно рендерим то, что метод возвратил
                render(res);
            }
        }
    }

    /**
     * Поиск метода, который можно определить как метод action
     *
     * @param inst где искать
     * @param name имя метода
     */
    public Method findActionMethod(Object inst, String name) {
        Class cur = inst.getClass();
        while (cur != null && cur != BaseAction.class && cur != Object.class) {
            Method[] mts = cur.getDeclaredMethods();
            for (Method mt : mts) {
                int md = mt.getModifiers();
                if (!Modifier.isPublic(md)) {
                    continue;
                }
                Class[] prms = mt.getParameterTypes();
                if (prms.length > 0) {
                    if (!(prms.length == 1 && ActionMethodWrapper.class.isAssignableFrom(prms[0]))) {
                        continue;
                    }
                }
                if (!mt.getName().equalsIgnoreCase(name)) {
                    continue;
                }
                return mt;
            }
            cur = cur.getSuperclass();
        }
        return null;
    }

    ////// params

    public Object getValue(String name) {
        return getParams().getValue(name);
    }

}
