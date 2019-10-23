package jandcode.jc.impl;

import jandcode.commons.ansifer.*;

/**
 * Настройка стилей для консоли.
 */
public class AnsiStyleDef {

    public void styleForJc(Ansifer a) {
        a.registerStyle("app-header", AnsiferColor.yellow_h, null, false);
        a.registerStyle("app-info-name", AnsiferColor.cyan, null, false);
        a.registerStyle("app-info-value", AnsiferColor.black_h, null, false);
        a.registerStyle("app-delim", AnsiferColor.black_h, null, false);
        a.registerStyle("app-footer", AnsiferColor.cyan, null, false);
        a.registerStyle("app-cm-name", AnsiferColor.yellow, null, false);
        a.registerStyle("app-opt-name", AnsiferColor.cyan, null, false);
        a.registerStyle("error-delim", AnsiferColor.red_h, null, false);
        a.registerStyle("error-text", AnsiferColor.yellow, null, false);
        a.registerStyle("error-stack", AnsiferColor.cyan, null, false);
        a.registerStyle("log-info", null, null, false);
        a.registerStyle("log-info-pfx", AnsiferColor.yellow, null, false);
        a.registerStyle("log-debug", AnsiferColor.black_h, null, false);
        a.registerStyle("log-debug-pfx", AnsiferColor.cyan, null, false);
        a.registerStyle("log-warn", AnsiferColor.black_h, null, false);
        a.registerStyle("log-warn-pfx", AnsiferColor.red_h, null, false);
        a.registerStyle("ant-info", AnsiferColor.black_h, null, false);
        a.registerStyle("ant-info-pfx", AnsiferColor.green, null, false);
        a.registerStyle("ant-error", AnsiferColor.red_h, null, false);
        a.registerStyle("c1", AnsiferColor.yellow, null, false);
        a.registerStyle("c2", AnsiferColor.cyan, null, false);
        a.registerStyle("c-gray", AnsiferColor.black_h, null, false);
        a.registerStyle("opt-name", AnsiferColor.cyan, null, false);
        a.registerStyle("opt-help", null, null, false);
    }

}
