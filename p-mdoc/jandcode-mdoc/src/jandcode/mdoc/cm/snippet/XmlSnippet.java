package jandcode.mdoc.cm.snippet;

import jandcode.commons.*;
import jandcode.commons.simxml.*;

import java.util.regex.*;

public class XmlSnippet extends Snippet {
    protected void makeParts(String text) {
        // запчасти
        Pattern p = Pattern.compile("<!-- *= *(\\w+.*?) *?-->(.*?)(?=<!-- *=)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String s = m.group(2);
            s = UtString.normalizeIndent(s);
            String nm = m.group(1);

            int a = nm.indexOf(':');

            if (a != -1) {
                // вариант NAME:PATH
                String pt = nm.substring(a + 1);
                nm = nm.substring(0, a);
                s = wrapToPath(s, pt);
            }

            addPart(nm, s);
        }

        String content = text;

        // убираем кодировку
        content = replaceAll(content, "\\<\\?.*?\\?\\>", "");

        // все маркеры snippet убираем
        content = replaceAll(content, "^\\s*<!-- *=.*?-->\\R", "");

        // текст всего файла
        content = UtString.normalizeIndent(content);
        addPart(CONTENT, content);
    }

    //////

    private static String marker = "Q" + System.currentTimeMillis();
    private static Pattern p_marker = Pattern.compile("^(\\s*<" + marker + "/>)", Pattern.MULTILINE);

    /**
     * Обертываение строки в xml, который соответсвует path.
     * Напртимер для wrapToPath('n1','p1/p2') вернет:
     * <pre>{@code
     * <p1>
     *     <p2>
     *         n1
     *     </p2>
     * </p1>
     * }</pre>
     *
     * @param s    что обернуть
     * @param path какой путь
     * @return обкрнутый xml текст
     */
    protected String wrapToPath(String s, String path) {
        if (UtString.empty(path)) {
            path = "root";
        }

        //
        SimXml tmp = new SimXmlNode();
        SimXml tmp2 = tmp.findChild(path, true);
        tmp2.addChild(marker);
        SimXml tmp3 = tmp.getChilds().get(0);

        //
        SimXmlSaver sv = new SimXmlSaver(tmp3);
        sv.setAlwaysCloseTag(false);
        sv.setOutputXmlHeader(false);
        String s1 = sv.save().toString();

        //
        Matcher m = p_marker.matcher(s1);
        if (m.find()) {
            String mf = m.group(1);
            int indent = mf.length() - marker.length() - 3; // 3 - это символы '</>'
            s1 = m.replaceFirst(UtString.indent(s, indent));
            return s1;
        } else {
            return s;
        }
    }

}
