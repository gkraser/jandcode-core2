package jandcode.core.web.virtfile.impl.render;

import jandcode.core.web.*;
import jandcode.core.web.render.*;
import jandcode.core.web.virtfile.*;

/**
 * Базовый класс для render специфических типов файлов.
 * Экземпляры этого класса не используются как независимые render,
 * а создаются и вызываются из render {@link VirtFileRender}.
 */
public abstract class BaseVirtFileRender extends BaseRender {

    protected void onRender(Object data) throws Exception {
        WebService svc = getWebService();
        VirtFile f = (VirtFile) data;
        FileType ftyp = svc.findFileType(f.getFileType());
        FileType ctyp = svc.findFileType(f.getContentFileType());
        onRenderFile(f, ftyp, ctyp);
    }

    /**
     * Процесс рендеринга файла
     */
    protected abstract void onRenderFile(VirtFile f, FileType fileType,
            FileType contentFileType) throws Exception;

}
