package jandcode.mdoc.cm.snippet;

import jandcode.commons.*;

import java.util.regex.*;

public class XmlSnippet extends Snippet {
    protected void makeParts(String text) {
        // запчасти
        Pattern p = Pattern.compile("<!-- *= *(\\w+).*?-->(.*?)(?=<!-- *=)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String s = m.group(2);
            s = UtString.normalizeIndent(s);
            addPart(m.group(1), s);
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
}
