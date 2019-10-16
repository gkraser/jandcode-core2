package jandcode.core.web.tst;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.virtfile.*;

/**
 * Если pathInfo указывает на папку с tst, переходим в нее
 */
public class TstActionFactory extends BaseComp implements IActionFactory {

    public static final String ACTION_TST = "tst";

    public IAction createAction(Request request) {

        String pi = request.getPathInfo();
        if (UtString.empty(pi)) {
            return null; // нет пути
        }

        VirtFile f = getApp().bean(WebService.class).findFile(pi);
        if (f == null || !f.isFolder()) {
            return null; // не папка
        }

        // папка
        request.redirect("_tst", UtCnv.toMap("path", pi));
        return null;
    }


}
