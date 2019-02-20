package jandcode.commons.source;

import jandcode.commons.io.*;

/**
 * Извлекает из кода блок.
 * Принцип такой: ищем любыми способами начало блока, включая '{'.
 * Текст, начиная с найденного '{' символа передаем в парзер.
 * Он должен вернуть текст до конца блока в методе getResult().
 */
public class JavaSimpleBlockExtractor extends TextParser {

    private StringBuilder sb = new StringBuilder();

    /**
     * Результат работы: текст блока, без ограничивающих '{}'
     */
    public String getResult() {
        return sb.toString();
    }

    protected void onParse() throws Exception {
        sb.setLength(0);
        int level = 0;
        char c = next();
        if (c != '{') {
            return;
        }
        while (true) {
            c = next();
            if (c == EOF) {
                sb.setLength(0);  // не нашли полноценный блок
                break; // конец
            }
            if (c == '}') {
                if (level == 0) {
                    break;
                }
                level--;
                store(c);
            } else if (c == '{') {
                level++;
                store(c);
            } else if (c == '/') {
                char c1 = next();
                if (c1 == '/') {
                    parseCommentLine();
                } else if (c1 == '*') {
                    parseCommentBlock();
                } else {
                    store(c);
                    push(c1);
                }
            } else if (c == '\'') {
                parseStr();
            } else if (c == '\"') {
                parseStr();
            } else {
                store(c);
            }
        }

    }

    private void store(char c) {
        sb.append(c);
    }

    private void parseCommentBlock() throws Exception {
        // прочитано //
        store('/');
        store('*');
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            }
            if (c == '*') {
                char c1 = next();
                if (c1 == '/') {
                    store(c);
                    store(c1);
                    break;
                } else {
                    store(c);
                    push(c1);
                }
            } else {
                store(c);
            }
        }
    }

    private void parseCommentLine() throws Exception {
        // прочитано //
        store('/');
        store('/');
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            }
            store(c);
            if (c == '\n') {
                break;
            }
        }
    }

    private void parseStr() throws Exception {
        // прочитано "
        char bound = last;
        //
        store(bound);
        boolean multiline = false;
        char x1 = next();
        char x2 = next();
        if (x1 == bound && x2 == bound) {
            multiline = true;
            store(bound);
            store(bound);
        } else {
            push(x2);
            push(x1);
        }
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            } else if (c == '\\') {
                char c1 = next(); // главное что кавычка уйдет
                store(c);
                store(c1);
            } else if (c == bound) {
                store(c);
                if (!multiline) {
                    break;
                } else {
                    char c1 = next();
                    char c2 = next();
                    if (c1 == bound && c2 == bound) {
                        store(c1);
                        store(c2);
                        break;
                    } else {
                        push(c2);
                        push(c1);
                    }
                }
            } else {
                store(c);
            }
        }
    }

}
