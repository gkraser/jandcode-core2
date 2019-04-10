package jandcode.commons.attrparser;

import jandcode.commons.io.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Простой парзер строки с атрибутами вида:
 * <code>attrname=attrvalue attr-name="attr value" attr-name='attr value'</code>
 */
public class AttrParser extends TextParser {

    private Map<String, String> result = new LinkedHashMap<>();
    private List<NamedValue> resultList = new ArrayList<>();

    /**
     * Результат парзинга как map
     * Если есть дубли атрибутов - последний встреченный записывается.
     */
    public Map<String, String> getResult() {
        return result;
    }

    /**
     * Результат парзинга как список атрибутов и их значений.
     * Если есть дубли атрибутов - все равно записывается сюда.
     */
    public List<NamedValue> getResultList() {
        return resultList;
    }

    //////

    protected void onParse() throws Exception {
        //
        while (true) {
            char c = nextNotSpace();
            if (c == EOF) {
                break;
            }

            // это очень простой парзер, все что угодно - идентификатор
            // кроме начинающегося с '=', который игнорируем
            if (c == '=') {
                continue;
            }

            String idn = parseIdn();
            String value = "";
            c = nextNotSpace();
            if (c == '=') {
                nextNotSpace();
                if (last != EOF) {
                    value = parseValue();
                }
            } else {
                push(last);
            }
            result.put(idn, value);
            //
            NamedValue nv = new NamedValue();
            nv.setName(idn);
            nv.setValue(value);
            resultList.add(nv);
        }
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    private char nextNotSpace() throws Exception {
        next();
        while (isSpace(last)) {
            next();
        }
        return last;
    }

    private String parseIdn() throws Exception {
        StringBuilder sb = new StringBuilder();
        while (!isSpace(last) && last != '=' && last != EOF) {
            sb.append(last);
            next();
        }
        push(last);
        return sb.toString();
    }

    private String parseValue() throws Exception {
        StringBuilder sb = new StringBuilder();
        char startQ = 0;
        if (last == '\"' || last == '\'') {
            startQ = last;
            next();
        }
        while (true) {
            if (last == EOF) {
                break;
            }
            if (startQ == 0) {
                if (isSpace(last)) {
                    break;
                }
            } else {
                if (last == startQ) {
                    break;
                }
            }
            sb.append(last);
            next();
        }
        return sb.toString();
    }


}
