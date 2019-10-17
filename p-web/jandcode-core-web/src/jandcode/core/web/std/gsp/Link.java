package jandcode.core.web.std.gsp;

import jandcode.commons.error.*;
import jandcode.core.web.gsp.*;

import java.util.*;

/**
 * Тег {@code <link/>} на css или <script/> на javascript
 *
 * @arg path на какой файл ссылка (может быть относительным путем или полным url)
 * @arg params:Map параметры ссылки (для формирования url на файл)
 */
public class Link extends BaseGsp {

    protected void onRender() throws Exception {
        //
        GspArgsUtils ar = new GspArgsUtils(this);
        String path = ar.getPath("path", true);
        Map params = (Map) ar.get("params");

        String ext = ar.ext(path);

        if ("css".equals(ext)) {
            String icref = ref(path, params);
            out("<link rel=\"stylesheet\" href=\"");
            out(icref);
            out("\"/>");

        } else if ("js".equals(ext)) {
            String icref = ref(path, params);
            out("<script src=\"");
            out(icref);
            out("\"></script>");

        } else {
            throw new XError("Параметр path может иметь расширение css,js");
        }

    }

}