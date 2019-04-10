package jandcode.mdoc.cm;

import jandcode.commons.*;

import java.util.*;

/**
 * Буфер вывода для code gen
 */
public class CodeGenTextBuffer {

    private StringBuilder sb = new StringBuilder();
    private List<CharSequence> parts = new ArrayList<>();

    /**
     * Ссылка на внутренний StringBuilder
     */
    public StringBuilder getSb() {
        return sb;
    }

    /**
     * Текущий текст
     */
    public String getText() {
        return sb.toString();
    }


    public void outText(Object s) {
        if (s == null) {
            s = "";
        }
        sb.append(s);
        parts.add(UtCnv.toString(s));
    }

    public String getPart(int num) {
        if (num < 0 || num >= parts.size()) {
            return "";
        }
        return parts.get(num).toString();
    }

    public String getPartAsComment(int num) {
        String s = getPart(num);
        s = UtString.trimLast(s);
        if (s.indexOf('\n') == -1) {
            s = "// " + s;
        } else {
            s = "/*\n" + s + "\n*/";
        }
        return s;
    }
}
