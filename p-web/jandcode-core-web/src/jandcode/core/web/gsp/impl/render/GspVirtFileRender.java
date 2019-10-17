package jandcode.core.web.gsp.impl.render;

import jandcode.commons.error.*;
import jandcode.core.web.*;
import jandcode.core.web.render.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.render.*;

/**
 * render для gsp
 */
public class GspVirtFileRender extends BaseVirtFileRender {

    public static final String RENDER_PREFIX = "virtfile.gsp.";
    public static final String RENDER_DEFAULT = RENDER_PREFIX + "default";

    protected void onRenderFile(VirtFile f, FileType fileType, FileType contentFileType) throws Exception {
        WebService rsvc = getWebService();

        RenderDef rdef;

        // возможно имеется render для типа контента файла
        rdef = rsvc.getRenders().find(RENDER_PREFIX + contentFileType.getName());

        if (rdef == null) {
            // ничего нет, используем по умолчанию
            rdef = rsvc.getRenders().find(RENDER_DEFAULT);

            if (rdef == null) {
                throw new XError("Render not found: {0}", RENDER_DEFAULT);
            }

        }

        // делегируем
        IRender rnd = rdef.createInst();
        rnd.render(f, getRequest());

    }

}
