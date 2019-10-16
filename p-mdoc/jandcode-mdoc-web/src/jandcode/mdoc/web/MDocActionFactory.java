package jandcode.mdoc.web;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.mdoc.builder.*;

/**
 * Если в pathInfo присутствует расширение, считает что возможно это файл,
 * принадлежащий документу. Если это так - показываем его.
 */
public class MDocActionFactory extends BaseComp implements IActionFactory {

    public static final String ACTION_MDOC = "mdoc";

    public IAction createAction(Request request) {
        WebService svc = getApp().bean(WebService.class);

        String pi = request.getPathInfo();
        if (UtString.empty(pi)) {
            return null;
        }

        String ext = UtFile.ext(pi);
        if (UtString.empty(ext)) {
            return null; // нет расширения
        }

        OutBuilder builder = getApp().bean(WebMDocService.class).getBuilder("default");

        OutFile f = builder.getOutFiles().findByPath(pi);
        if (f == null) {
            if (!pi.startsWith("-debug/")) {
                return null;
            }
        }

        // это файл из документации
        IAction res = svc.createAction(ACTION_MDOC);
        request.getAttrs().put(WebConsts.a_actionMethod, "out");
        request.getAttrs().put(WebConsts.a_actionMethodPathInfo, pi);
        return res;
    }


}
