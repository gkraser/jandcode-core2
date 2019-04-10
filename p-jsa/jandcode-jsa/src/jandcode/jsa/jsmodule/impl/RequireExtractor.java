package jandcode.jsa.jsmodule.impl;

import jandcode.commons.*;

import java.util.*;
import java.util.regex.*;

public class RequireExtractor {

    public static Pattern P_REQUIRE = Pattern.compile("(?:^\\uFEFF?|[^$_a-zA-Z\\xA0-\\uFFFF.\"'])require\\s*\\(\\s*(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"|'[^'\\\\]*(?:\\\\.[^'\\\\]*)*'|`[^`\\\\]*(?:\\\\.[^`\\\\]*)*`)\\s*\\)", Pattern.DOTALL | Pattern.MULTILINE);
    public static Pattern P_REMOVE_COMMENT_ML = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL | Pattern.MULTILINE);
    public static Pattern P_REMOVE_COMMENT_1L = Pattern.compile("//.*");
    public static Pattern P_STRING_CONST = Pattern.compile("(['|\"])(.*?\\/\\*.*?)\\1");

    /**
     * Извлекает require из исходника на js
     *
     * @param source откуда
     * @return список найденных require
     */
    public List<String> extractRequire(String source) {
        List<String> res = new ArrayList<>();

        if (UtString.empty(source)) {
            return res;
        }

        // зменяем * в константах на &, что бы они в коментариях не учитывались
        Matcher ms = P_STRING_CONST.matcher(source);
        StringBuffer sbs = new StringBuffer();
        while (ms.find()) {
            String repl = ms.group().replace('*', '&');
            repl = Matcher.quoteReplacement(repl);
            ms.appendReplacement(sbs, repl);
        }
        ms.appendTail(sbs);
        source = sbs.toString();

        // удаляем многострочные коментарии
        Matcher mc = P_REMOVE_COMMENT_ML.matcher(source);
        source = mc.replaceAll("");

        // удаляем однострочные коментарии
        mc = P_REMOVE_COMMENT_1L.matcher(source);
        source = mc.replaceAll("");

        // извлекаем require
        Matcher m = P_REQUIRE.matcher(source);
        while (m.find()) {
            String req = m.group(1);
            req = req.replaceAll("\'", "")
                    .replace("\"", "")
                    .replace('&', '*'); // возвращаем * на место!
            req = req.trim();
            if (!UtString.empty(req)) {
                res.add(req);
            }
        }

        return res;
    }

}
