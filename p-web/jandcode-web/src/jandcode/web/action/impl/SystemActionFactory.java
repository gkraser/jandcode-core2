package jandcode.web.action.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.action.*;
import jandcode.web.virtfile.*;

/**
 * Стандартная фабрика для action.
 * Считает начало uri как имя action.
 * Если action по uri не найдена, то пытаемся найти такую папку. Если папка имеется,
 * то создает action 'virtfolder'
 */
public class SystemActionFactory extends BaseComp implements IActionFactory {

    public static final String ACTION_ROOT = "root";
    public static final String ACTION_VIRTFOLDER = "virtfolder";

    //////

    public IAction createAction(Request request) {
        WebService svc = getApp().bean(WebService.class);
        NamedList<ActionDef> actions = svc.getActions();
        IVariantMap attrs = request.getAttrs();

        // lowercase!
        String pi = request.getPathInfo().toLowerCase();

        if (UtString.empty(pi)) {
            attrs.put(WebConsts.a_actionPathInfo, request.getPathInfo());
            attrs.put(WebConsts.a_actionMethod, "");
            attrs.put(WebConsts.a_actionMethodPathInfo, "");
            return svc.createAction(ACTION_ROOT);
        }

        // ищем, какой action совпадает с началом пути
        ActionDef act = null;
        String s = pi;
        while (true) {
            act = actions.find(s);
            if (act != null) {
                break;
            }
            // отрезаем последний фасет
            int a = s.lastIndexOf('/');
            if (a == -1) {
                break;
            }
            s = s.substring(0, a);
        }


        // уже не lowercase
        pi = request.getPathInfo();

        IAction res = null;
        if (act != null) {
            // создаем экземпляр
            res = act.createInst();
            // убираем имя action из пути
            int actNameLen = act.getName().length();
            String p_pathInfo = "";
            if (pi.length() > actNameLen) {
                p_pathInfo = pi.substring(actNameLen + 1); //исключаем '/'
            }
            // устанавливаем параметр pathInfo (часть после имени action)
            attrs.put(WebConsts.a_actionPathInfo, p_pathInfo);

            // разбираем pathInfo как methodName/methodPathInfo
            if (p_pathInfo.length() > 0) {
                int a = p_pathInfo.indexOf('/');
                if (a == -1) {
                    attrs.put(WebConsts.a_actionMethod, p_pathInfo);
                    attrs.put(WebConsts.a_actionMethodPathInfo, "");
                } else {
                    attrs.put(WebConsts.a_actionMethod, p_pathInfo.substring(0, a));
                    attrs.put(WebConsts.a_actionMethodPathInfo, p_pathInfo.substring(a + 1));
                }
            } else {
                attrs.put(WebConsts.a_actionMethod, "");
                attrs.put(WebConsts.a_actionMethodPathInfo, "");
            }

        }

        //
        if (res == null) {
            // action не найден. Может это папка с индексными файлами?
            VirtFile folder = svc.findFile(pi);
            if (folder != null && folder.isFolder()) {
                String pp = folder.getPath() + "/";
                if (svc.findFile(pp + WebConsts.FILE_INDEX_GSP) != null) {
                    attrs.put(WebConsts.a_actionPathInfo, request.getPathInfo());
                    attrs.put(WebConsts.a_actionMethod, "");
                    attrs.put(WebConsts.a_actionMethodPathInfo, "");
                    return svc.createAction(ACTION_VIRTFOLDER);
                }
                if (svc.findFile(pp + WebConsts.FILE_INDEX_HTML) != null) {
                    request.redirect(pp + WebConsts.FILE_INDEX_HTML, request.getParams());
                }
            }
        }

        return res;
    }


}
