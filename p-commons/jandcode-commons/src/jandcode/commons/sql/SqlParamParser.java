package jandcode.commons.sql;

import jandcode.commons.*;
import jandcode.commons.io.*;

import java.util.*;

/**
 * Парзер sql с параметрами.
 * <ul>
 * <li>':name' и ':{name}' - параметры, которые превращаются в '?'.</li>
 * <li>'\:' - превращает в ':'</li>
 * <li>учитываются коментарии sql: '-- xxx' и многострочный '/*', в них
 * параметры не разбираются</li>
 * <li>строковые константы не учитываются! Если вам необходима строковая константа
 * вида ':xxx', ':{xxx}', '--xxx', '/*xxx' - преобразуйте ее в другой вид, иначе она
 * будет рассматриваться как параметр!
 * </li>
 * </ul>
 */
public class SqlParamParser extends TextParser {

    private StringBuilder res = new StringBuilder();
    private ArrayList<String> params = new ArrayList<>();

    /**
     * Создать и выполнить разбор
     *
     * @param sqltext исходный sql
     */
    public SqlParamParser(String sqltext) {
        loadFrom(sqltext);
    }

    /**
     * Список имен параметров, которые заменены на ?
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Преобразованный sql
     */
    public String getResult() {
        return res.toString();
    }

    //////

    protected void onParse() throws Exception {
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            }
            if (c == '\\') {
                char c2 = next();
                if (c2 == ':') {
                    res.append(c2);
                } else {
                    res.append(c);
                    push(c2);
                }
            } else if (c == ':') {
                char c2 = next();
                String s;
                if (c2 == ':') {
                    // два ':' подряд, для postgres
                    res.append(c);
                    res.append(c2);
                    continue;
                } else if (c2 == '{') {
                    s = grabUntil('}');
                    if (!isIdn(s)) {
                        res.append(":{");
                        res.append(s);
                        res.append('}');
                        continue;
                    }
                } else {
                    push(c2);
                    s = grabIdn();
                    if (UtString.empty(s)) {
                        res.append(c);
                        continue;
                    }
                }
                params.add(s);
                res.append('?');
            } else if (c == '-') {
                char c2 = next();
                String s;
                if (c2 == '-') {
                    s = grabUntil('\n');
                    res.append(c);
                    res.append(c2);
                    res.append(s);
                    if (last != EOF) {
                        res.append(last);
                    }
                } else {
                    res.append(c);
                    push(c2);
                }
            } else if (c == '/') {
                char c2 = next();
                String s;
                if (c2 == '*') {
                    s = grabUntil2('*', '/');
                    res.append("/*");
                    res.append(s);
                    if (last != EOF) {
                        res.append("*/");
                    }
                } else {
                    res.append(c);
                    push(c2);
                }
            } else {
                res.append(c);
            }
        }
    }

    private String grabIdn() throws Exception {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (UtString.isIdnChar(c) || c == '.') {
                sb.append(c);
            } else {
                push(c);
                break;
            }
        }
        return sb.toString();
    }

    private String grabUntil(char terminate) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (c != terminate && c != EOF) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private String grabUntil2(char terminate, char terminate2) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            } else if (c != terminate) {
                sb.append(c);
            } else {
                char c2 = next();
                if (c2 == terminate2) {
                    break;
                }
                sb.append(c);
                push(c2);
            }
        }
        return sb.toString();
    }

    private boolean isIdn(CharSequence s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!UtString.isIdnChar(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
