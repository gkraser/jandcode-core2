package jandcode.web.virtfile.impl.action;

import jandcode.web.*;
import jandcode.web.action.*;
import jandcode.web.virtfile.*;

/**
 * Возвращает статический файл
 */
public class VirtFileAction extends BaseAction {

    protected void onExec() throws Exception {
        WebService svc = getReq().getWebService();
        //
        VirtFile f = svc.findFile(getReq().getActionPathInfo());

        if (f == null) {
            throw new HttpError(404);
        }

        if (f.isFolder()) {
            throw new HttpError(404);
        }

        if (f.isPrivate()) {
            throw new HttpError(404);
        }

        FileType ft = svc.findFileType(f.getContentFileType());
        if (ft.isPrivate()) {
            throw new HttpError(404);
        }

        // если не изменился и не шаблон, возвращаем 304
        if (!f.isTmlBased()) {
            getReq().checkETag("" + f.getLastModTime());
        }

        getReq().render(f);

    }

}
