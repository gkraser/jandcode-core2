package jandcode.core.web.std.gsp;

import jandcode.commons.groovy.*;
import jandcode.core.web.gsp.*;

/**
 * Включение шаблона в собственном контексте. Все аргументы, включая body,
 * передаются в выводимую gsp. Синоним для метода {@link IGspTemplate#outTag(java.lang.String, java.util.Map)}
 *
 * @arg path какой файл, может быть относительным путем
 */
public class IncludeTag extends BaseGsp {
    protected void onRender() throws Exception {
        GspArgsUtils ar = new GspArgsUtils(this);
        String gspName = ar.getPath("path");
        outTag(gspName, getArgs());
    }
}
