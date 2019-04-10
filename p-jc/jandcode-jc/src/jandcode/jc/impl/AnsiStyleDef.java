package jandcode.jc.impl;

import jandcode.commons.*;
import jandcode.commons.ansifer.*;
import jandcode.commons.conf.*;
import jandcode.jc.*;

/**
 * Настройка стилей для консоли.
 */
public class AnsiStyleDef {

    public void styleForJc(Ansifer a) {

        a.registerStyle("app-header", AnsiferColor.YELLOW_H);
        a.registerStyle("app-info-name", AnsiferColor.CYAN);
        a.registerStyle("app-info-value", AnsiferColor.BLACK_H);
        a.registerStyle("app-delim", AnsiferColor.BLACK_H);
        a.registerStyle("app-footer", AnsiferColor.CYAN);
        a.registerStyle("app-cm-name", AnsiferColor.YELLOW);
        a.registerStyle("app-opt-name", AnsiferColor.CYAN);
        a.registerStyle("error-delim", AnsiferColor.RED_H);
        a.registerStyle("error-text", AnsiferColor.YELLOW);
        a.registerStyle("error-stack", AnsiferColor.CYAN);
        a.registerStyle("log-info", null);
        a.registerStyle("log-info-pfx", AnsiferColor.YELLOW);
        a.registerStyle("log-debug", AnsiferColor.BLACK_H);
        a.registerStyle("log-debug-pfx", AnsiferColor.CYAN);
        a.registerStyle("log-warn", AnsiferColor.BLACK_H);
        a.registerStyle("log-warn-pfx", AnsiferColor.RED_H);
        a.registerStyle("ant-info", AnsiferColor.BLACK_H);
        a.registerStyle("ant-info-pfx", AnsiferColor.GREEN);
        a.registerStyle("ant-error", AnsiferColor.RED_H);
        a.registerStyle("c1", AnsiferColor.YELLOW);
        a.registerStyle("c2", AnsiferColor.CYAN);
        a.registerStyle("c-gray", AnsiferColor.BLACK_H);

        //////
        a.registerStyle("opt-name", AnsiferColor.CYAN);
        a.registerStyle("opt-help", null);

    }

    /**
     * Настройка стилей из конфигурации
     */
    public void styleForJc(Ansifer a, JcConfig cfg) {
        // defaults
        styleForJc(a);

        // config
        for (Conf st : cfg.getConf().getConfs("color-style")) {
            String s;

            AnsiferColor color = null;
            AnsiferColor background = null;

            s = st.getString("color");
            if (!UtString.empty(s)) {
                color = AnsiferColor.fromString(s);
            }

            s = st.getString("background");
            if (!UtString.empty(s)) {
                background = AnsiferColor.fromString(s);
            }

            //
            a.registerStyle(st.getName(), color, background);
        }
    }

}
