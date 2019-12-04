package jandcode.core.web.std.render;

import jandcode.commons.*;
import jandcode.core.web.render.*;
import jandcode.core.web.virtfile.*;

/**
 * простой render в json
 */
public class JsonRender extends BaseRender {

    protected void onRender(Object data) throws Exception {
        FileType ft = getWebService().findFileType("json");
        getRequest().setContentType(ft.getMime());
        //
        String json = UtJson.toJson(data);
        getRequest().getOutWriter().write(json);
    }

}
