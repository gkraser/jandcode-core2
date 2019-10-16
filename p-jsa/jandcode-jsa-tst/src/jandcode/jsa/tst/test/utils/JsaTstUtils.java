package jandcode.jsa.tst.test.utils;

import jandcode.commons.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.virtfile.*;

/**
 * Утилиты для использования внутри gsp в tst
 */
public class JsaTstUtils implements IGspContextLinkSet {

    private GspContext gspCtx;

    public void setGspContext(GspContext gspContext) {
        this.gspCtx = gspContext;
    }

    /**
     * Возвращает файл с именем findFn начиная с каталога from.
     *
     * @param from   может быть каталогом или файлом
     * @param findFn искомый файл
     * @return null, если не найден
     */
    public String findFileUp(String from, String findFn) {
        VirtFile f1 = gspCtx.getRootGsp().getWebService().findFile(from);
        if (f1 == null) {
            return null;
        }
        String cp;
        if (f1.isFolder()) {
            cp = f1.getPath();
        } else {
            cp = f1.getFolderPath();
        }
        while (!UtString.empty(cp)) {
            String fn = UtVDir.join(cp, findFn);
            VirtFile ft = gspCtx.getRootGsp().getWebService().findFile(fn);
            if (ft != null) {
                return ft.getPath();
            }
            cp = UtFile.path(cp);
        }
        return null;
    }
}
