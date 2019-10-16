package jandcode.core.web.gsp.impl.render;

import jandcode.core.web.gsp.*;


/**
 * render для js.gsp
 */
public class GspJsVirtFileRender extends GspDefaultVirtFileRender {
    protected String prepareBuffer(ITextBuffer buf) {
        String s = buf.toString();
        // удаляем теги script
        s = s.replaceAll("<script.*?>", "").replaceAll("</script>", "");
        return s;
    }
}
