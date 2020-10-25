package jandcode.core.web.virtfile.impl.action;

import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.virtfile.*;

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
            FileCacheControl fcc = svc.findFileCacheControl(f.getPath());
            if (fcc == null || "etag".equals(fcc.getCacheControl())) {
                getReq().checkETag("" + f.getLastModTime());
            } else {
                getReq().setHeader("Cache-Control", fcc.getCacheControl());
            }
        }

        getReq().render(f);

    }

}
