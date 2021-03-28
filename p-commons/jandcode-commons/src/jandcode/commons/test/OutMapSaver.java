package jandcode.commons.test;

import jandcode.commons.io.*;

import java.io.*;
import java.util.*;

/**
 * Вывод map (или list) в приличном виде для отладки
 */
public class OutMapSaver implements ISaver {

    private Object data;
    private IndentWriter wr;
    private boolean showClass;

    public OutMapSaver(Object data) {
        this.data = data;
    }

    public OutMapSaver(Object data, boolean showClass) {
        this(data);
        this.showClass = showClass;
    }

    public void saveTo(Writer writer) throws Exception {
        wr = new IndentWriter(writer);
        outValue(data);
    }

    private void out(Object s) throws Exception {
        if (s != null) {
            wr.write(s.toString());
        }
    }

    private void outValue(Object v) throws Exception {
        if (v instanceof Map) {
            outMap((Map) v);

        } else if (v instanceof Collection) {
            outList((Collection) v);

        } else {
            outSimple(v);

        }
    }

    private void outMap(Map map) throws Exception {
        out("{");
        if (showClass) {
            outClass(map);
        }
        wr.indentInc();
        out("\n");
        for (Object key : map.keySet()) {
            out(key);
            out(": ");
            outValue(map.get(key));
        }
        wr.indentDec();
        out("}\n");
    }

    private void outList(Collection list) throws Exception {
        out("[");
        if (showClass) {
            outClass(list);
        }
        wr.indentInc();
        out("\n");
        for (Object it : list) {
            outValue(it);
        }
        wr.indentDec();
        out("]\n");
    }

    private void outSimple(Object v) throws Exception {
        if (v == null) {
            out("<NULL>\n");
        } else {
            out(v.toString());
            if (showClass) {
                outClass(v);
            }
            out("\n");
        }
    }

    private void outClass(Object v) throws Exception {
        if (v != null) {
            out(" // class: ");
            out(v.getClass().getName());
            //out(")");
        }
    }

}
