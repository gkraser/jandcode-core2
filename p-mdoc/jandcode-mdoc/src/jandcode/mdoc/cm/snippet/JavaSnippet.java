package jandcode.mdoc.cm.snippet;

import jandcode.commons.*;
import jandcode.mdoc.groovy.*;

import java.util.*;
import java.util.regex.*;

public class JavaSnippet extends Snippet {

    private boolean methodExtracted;

    protected void makeParts(String text) {
        // запчасти
        Pattern p = Pattern.compile("//= *([\\w\\-_.]+)(.*?)(?=//=)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String s = m.group(2);
            s = UtString.normalizeIndent(s);
            addPart(m.group(1), s);
        }

        // убираем package
        String content = text;
        p = Pattern.compile("package .*$", Pattern.MULTILINE);
        m = p.matcher(content);
        if (m.find()) {
            content = m.replaceAll("");
        }

        // все маркеры snippet убираем
        content = replaceAll(content + "\n", "^\\s*//=.*?\\R", "");

        // текст всего файла
        content = UtString.normalizeIndent(content);
        addPart(CONTENT, content);
    }

    public SnippetPart getPart(String name) {
        SnippetPart p = super.getPart(name);
        if (p == null && !methodExtracted) {
            methodExtracted = true;
            GroovyMethodExtractor extr = new GroovyMethodExtractor();
            Map<String, String> methods = extr.extractMethodBodys(this.getPart(CONTENT_ALL).getText());
            for (Map.Entry<String, String> en : methods.entrySet()) {
                String mn = en.getKey();
                String mt = en.getValue();
                SnippetPart p1 = getPart(mn);
                if (p1 == null) {
                    JavaSnippet jsn = new JavaSnippet();
                    jsn.configure(mt, getLang());

                    // если есть part=body, то ее считаем за тело метода
                    SnippetPart pBody = jsn.getParts().find("body");
                    if (pBody != null) {
                        addPart(mn, pBody.getText());
                    } else {
                        addPart(mn, jsn.getPart(CONTENT).getText());
                    }
                    // полное тело метода
                    addPart(mn + "-all", jsn.getPart(CONTENT).getText());
                }
            }
            p = getPart(name);
        }
        return p;
    }
}
