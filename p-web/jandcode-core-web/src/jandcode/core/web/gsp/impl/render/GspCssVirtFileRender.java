package jandcode.core.web.gsp.impl.render;

import jandcode.core.web.gsp.*;

/**
 * render для css.gsp
 */
public class GspCssVirtFileRender extends GspDefaultVirtFileRender {
    protected String prepareBuffer(ITextBuffer buf) {
        String s = buf.toString();
        // удаляем теги style
        s = s.replaceAll("<style.*?>", "").replaceAll("</style>", "");
        return s;
    }
}
