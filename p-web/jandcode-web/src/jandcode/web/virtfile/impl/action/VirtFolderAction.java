package jandcode.web.virtfile.impl.action;

import jandcode.commons.*;
import jandcode.web.*;
import jandcode.web.action.*;
import jandcode.web.gsp.*;
import jandcode.web.virtfile.*;

/**
 * Возвращает индекс-файл из папки
 */
public class VirtFolderAction extends BaseAction {

    protected void onExec() throws Exception {
        WebService svc = getReq().getWebService();
        //
        String path = UtVDir.normalize(getReq().getActionPathInfo());
        if (!UtString.empty(path)) {

            // для не корневой ищем папку

            VirtFile f = svc.findFile(path);

            if (f == null) {
                throw new HttpError(404);
            }

            if (!f.isFolder()) {
                throw new HttpError(404);
            }

            path = f.getPath();
        }

        // ищем index

        //
        VirtFile fidx = svc.findFile(path + "/" + WebConsts.FILE_INDEX_GSP);
        if (fidx != null && fidx.isFile()) {
            // gsp as index
            getReq().renderGsp(fidx.getPath());
            return;
        }

        //
        fidx = svc.findFile(path + "/" + WebConsts.FILE_INDEX_HTML);
        if (fidx != null && fidx.isFile()) {
            // gsp as html

            // если не изменился и не шаблон, возвращаем 304
            if (!fidx.isTmlBased()) {
                getReq().checkETag("" + fidx.getLastModTime());
            }

            getReq().render(fidx);

            return;
        }

        //
        if (UtString.empty(path)) {
            // не найдено, но тут корень
            getReq().render(new GspTemplate("root"));
            return;
        }

        throw new HttpError(404);
    }

}
