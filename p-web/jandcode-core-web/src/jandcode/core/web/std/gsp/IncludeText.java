package jandcode.core.web.std.gsp;

import jandcode.commons.io.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.virtfile.*;

/**
 * Вставка текста без преобразований
 *
 * @arg path какой файл, может быть относительным путем
 */
public class IncludeText extends BaseGsp {

    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);
        String path = ar.getPath("path", true);
        VirtFile f = getWebService().getFile(path);
        //
        StringLoader ldr = new StringLoader();
        ldr.load().fromStream(f.getInputStream());
        out(ldr.getResult());
    }

}
