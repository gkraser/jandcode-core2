package jandcode.web.gsp.impl.render;

import jandcode.web.*;
import jandcode.web.gsp.*;
import jandcode.web.virtfile.*;
import jandcode.web.virtfile.impl.render.*;

/**
 * render для gsp
 */
public class GspDefaultVirtFileRender extends BaseVirtFileRender {

    protected void onRenderFile(VirtFile f, FileType fileType, FileType contentFileType) throws Exception {
        WebService svc = getWebService();

        GspContext ctx = svc.createGspContext();
        ITextBuffer buf = ctx.render(f.getPath());

        String s = prepareBuffer(buf);

        getRequest().setContentType(contentFileType.getMime());
        getRequest().getOutWriter().write(s);
    }

    protected String prepareBuffer(ITextBuffer buf) {
        return buf.toString();
    }

}
