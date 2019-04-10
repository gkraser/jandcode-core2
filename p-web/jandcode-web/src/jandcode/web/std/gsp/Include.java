package jandcode.web.std.gsp;

import jandcode.web.gsp.*;

/**
 * Включение шаблона в контексте текущего шаблона. Внутри шаблона gspName
 * args будет такой же, как и у того, откуда включили. Сининим для
 * метода {@link Gsp#include(java.lang.String)}.
 *
 * @arg path путь до включаемого шаблона, может быть относительным
 */
public class Include extends BaseGsp {

    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);
        String path = ar.getPath("path", true);
        include(this.getOwner(), path);
    }

}
