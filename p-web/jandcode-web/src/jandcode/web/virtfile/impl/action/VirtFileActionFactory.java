package jandcode.web.virtfile.impl.action;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.action.*;
import jandcode.web.virtfile.*;

/**
 * Если в pathInfo присутствует расширение, считает что это файл.
 * Если такой файл имеется и он не приватный, то будем его показывать.
 */
public class VirtFileActionFactory extends BaseComp implements IActionFactory {

    public static final String ACTION_VIRTFILE = "virtfile";

    public IAction createAction(Request request) {

        String pi = request.getPathInfo();
        if (UtString.empty(pi)) {
            return null; // нет пути
        }

        String ext = UtFile.ext(pi);
        if (UtString.empty(ext)) {
            return null; // нет расширения
        }

        WebService svc = getApp().bean(WebService.class);
        FileType contentType = svc.findFileType(ext);
        if (contentType.isPrivate()) {
            return null; // приватное расширение файла, смысла искать нету
        }

        VirtFile f = svc.findFile(pi);
        if (f == null || f.isFolder() || f.isPrivate()) {
            return null;
        }

        // это файл
        IAction res = svc.createAction(ACTION_VIRTFILE);
        request.getAttrs().put(WebConsts.a_actionPathInfo, pi);
        return res;
    }


}
