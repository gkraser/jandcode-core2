package jandcode.commons.ansifer4.impl;

import jandcode.commons.ansifer4.*;

/**
 * Настройка стилей для консоли.
 */
public class DefaultStyles {

    public static void registerDefaultStyles(Ansifer a) {
        a.registerStyle("error-delim", AnsiferColor.red_h, null, true);
        a.registerStyle("error-text", AnsiferColor.yellow, null, true);
        a.registerStyle("error-stack", AnsiferColor.cyan, null, true);
        a.registerStyle("log-info", null, null, true);
        a.registerStyle("log-info-pfx", AnsiferColor.yellow, null, true);
        a.registerStyle("log-debug", AnsiferColor.black_h, null, true);
        a.registerStyle("log-debug-pfx", AnsiferColor.cyan, null, true);
        a.registerStyle("log-warn", AnsiferColor.black_h, null, true);
        a.registerStyle("log-warn-pfx", AnsiferColor.red_h, null, true);
        a.registerStyle("c1", AnsiferColor.yellow, null, true);
        a.registerStyle("c2", AnsiferColor.cyan, null, true);
        a.registerStyle("c-gray", AnsiferColor.black_h, null, true);
        a.registerStyle("opt-name", AnsiferColor.cyan, null, true);
        a.registerStyle("opt-help", null, null, true);
    }

}
