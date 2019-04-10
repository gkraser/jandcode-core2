package jandcode.web.virtfile.impl.render;

import jandcode.commons.error.*;
import jandcode.web.*;
import jandcode.web.render.*;
import jandcode.web.virtfile.FileType;
import jandcode.web.virtfile.*;
import org.apache.commons.vfs2.*;

/**
 * render для статических файлов {@link VirtFile}.
 * Перенаправляет процесс рендеринга в специфический render, определяемый по расширению
 * файла.
 */
public class VirtFileRender extends BaseRender {

    public static final String RENDER_PREFIX = "virtfile.";
    public static final String RENDER_DEFAULT = RENDER_PREFIX + "default";

    protected void onRender(Object data) throws Exception {
        WebService svc = getWebService();

        if (data instanceof FileObject) {
            // Для FileObject создаем wrapper
            data = svc.wrapFile((FileObject) data);
        }

        //

        VirtFile f = (VirtFile) data;
        FileType ftyp = svc.findFileType(f.getFileType());

        if (!f.isExists()) {
            throw new XError("File not exists: {0}", f.getPath());
        }

        if (f.isFolder()) {
            throw new XError("File is folder: {0}", f.getPath());
        }


        RenderDef rdef;

        // возможно имеется render для типа файла
        rdef = svc.getRenders().find(RENDER_PREFIX + ftyp.getName());

        if (rdef == null) {
            // ничего нет, используем по умолчанию
            rdef = svc.getRenders().find(RENDER_DEFAULT);

            if (rdef == null) {
                throw new XError("Render not found: {0}", RENDER_DEFAULT);
            }

        }

        // делегируем
        IRender rnd = rdef.createInst();
        rnd.render(f, getRequest());
    }

}
