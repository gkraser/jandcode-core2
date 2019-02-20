package jandcode.commons.source;

import jandcode.commons.*;

import java.util.*;
import java.util.regex.*;

/**
 * Извлекает директивы из текста.
 * Директива представляет собой конструкцию:
 * <p>
 * <code>//#jc NAME [PARAM1[,PARAM2...]]</code>
 * <p>
 * Может быт как коментарием на верхнем уровне (в поддерживаемых коментарий '//' файлах),
 * так и внутри многострочных коментариях.
 */
public class JcDirectiveExtractor {

    /**
     * Маркировка директивы
     */
    public static final String DIRECTIVE_MARK = "//#jc";

    private List<JcDirective> items = new ArrayList<>();

    /**
     * Создать объект и распарзить строку с текстом s
     */
    public JcDirectiveExtractor(String s) {
        parse(s);
    }

    /**
     * Директивы в тексте
     */
    public List<JcDirective> getItems() {
        return items;
    }

    protected void parse(String s) {
        if (UtString.empty(s)) {
            return;
        }
        int a = s.indexOf(DIRECTIVE_MARK);
        if (a == -1) {
            return;
        }
        Pattern p = Pattern.compile(JcDirectiveExtractor.DIRECTIVE_MARK + "(.*)$", Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            String s1 = m.group(1).trim();
            if (UtString.empty(s1)) {
                continue;
            }
            JcDirective d = new JcDirective(s1);
            items.add(d);
        }
    }

}
