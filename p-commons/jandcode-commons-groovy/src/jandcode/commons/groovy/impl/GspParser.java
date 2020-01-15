package jandcode.commons.groovy.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;

/**
 * Парзер для шаблонов "gsp".
 * Особенности:
 * - расчитывает на то, что обработчик скрипта имеет метод 'out(object)'
 * - формирует скрипт, который по количеству строк совпадает с оригиналом, т.е. номер
 * строки в сгенерированном совпадает с номером строки в шаблоне
 * - для '${A}' генерирует 'out(A)'
 * - для '<% A %>' генерирует ' A '
 * - для '<%= A %>' генерирует 'out( A )'
 * - для '<%-- A --%>' генерирует java-коментарий
 * - для '<xx:yy/>'  генерирует 'outTag('xx/yy')'
 * - для '<xx:yy atr="value" atr2="value2"/>'  генерирует 'outTag('xx/yy',atr:value,atr2:value)'
 * - для '<xx:yy atr="value" atr2="value2"> body </xx:yy>'  генерирует 'outTag('xx/yy',atr:value,atr2:value) { body }'
 */
public class GspParser extends TextParser {

    private StringBuilder sb = new StringBuilder();
    private StringBuilder nextstr = new StringBuilder();
    private boolean _ignore_empty;
    private boolean _code_instr;
    private String _printMethodName = "out";
    private String _nsTagTemplate = "outTag('$1/$2'";
    private char _lastOutChar = EOF;

    public String getScriptText() {
        return sb.toString();
    }

    /////////////

    /**
     * Имя 'print' метода, который генерируется для вывода текста
     *
     * @param printMethodName
     */
    public void setPrintMethodName(String printMethodName) {
        _printMethodName = printMethodName;
    }

    public String getPrintMethodName() {
        return _printMethodName;
    }

    /**
     * Шаблон для генерации по тегам с namespace.
     * $1 заменяется на namespace, $2 заменяется на имя тега.
     * Например: для '$1.$2(' и {@code <jc:nnn/>} получим 'jc.nnn('
     */
    public void setNsTagTemplate(String nsTagTemplate) {
        _nsTagTemplate = nsTagTemplate;
    }

    /////////////

    public char next() throws Exception {
        char c = super.next();
        if (c == '\r') {
            c = next();
        }
        return c;
    }

    private void setIgnore_empty(boolean ignore_empty) {
        if (_code_instr) {
            _ignore_empty = false;  // если были уже выведены строки - не игнорируем
        } else {
            _ignore_empty = ignore_empty;
        }
    }

    protected void onParse() throws Exception {
        sb = new StringBuilder();
        nextstr = new StringBuilder();
        _ignore_empty = false;
        _code_instr = false;

        parseBody(null, null);

        flushText();
    }

    private void parseBody(StringBuilder prefix, StringBuilder suffix) throws Exception {
        while (true) {
            char c = next();
            if (last == EOF) {
                if (prefix == null) {
                    break; //основное тело
                } else {
                    errorWaitString("</" + prefix + ":" + suffix + ">");
                }
            }

            if (c == '<') {
                char c1 = next();
                if (c1 == '%') {
                    parseCode();
                } else if (UtString.isIdnStartChar(c1)) {
                    parseTag();
                } else if (c1 == '/') {
                    if (prefix == null) {
                        // основное тело
                        push(last);
                        outText(c);
                    } else {
                        // конец тега
                        if (parseTagEnd(prefix, suffix)) {
                            break;
                        }
                    }
                } else {
                    push(last);
                    outText(c);
                }
            } else if (c == '$') {
                char c1 = next();
                if (c1 == '{') {
                    parseCodeExpand();
                } else {
                    push(last);
                    outText(c);
                }
            } else if (c == '%') {
                char c1 = next();
                if (c1 == '{') {
                    parseCommentGsp();
                } else {
                    push(last);
                    outText(c);
                }
            } else {
                outText(c);
            }
        }
    }

    private void parseCode() throws Exception {
        char c = next();
        if (c == '=') {
            parseCodePrint();
        } else if (c == '-') {
            char c1 = next();
            if (c1 == '-') {
                parseComment("--%>", '%', '>');
            } else {
                push(c1);
                push(c);
                parseCodeInline();
            }
        } else if (c == '@') {
            parseDirective();
        } else {
            push(last);
            parseCodeInline();
        }
    }

    private void parseCodeInline() throws Exception {
        setIgnore_empty(true);
        flushText();
        //        
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitString("%>");
            }
            if (c == '%') {
                char c1 = next();
                if (c1 == '>') {
                    break;
                } else {
                    push(last);
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
    }

    private void parseCodePrint() throws Exception {
        setIgnore_empty(false);
        flushText();
        //
        outPrintMethodStart();
        //
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitString("%>");
            }
            if (c == '%') {
                char c1 = next();
                if (c1 == '>') {
                    break;
                } else {
                    push(last);
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        //
        outPrintMethodEnd();
        //
    }

    private void parseCodeExpand() throws Exception {
        setIgnore_empty(false);
        flushText();
        //
        outPrintMethodStart();
        //
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitString("}");
            }
            if (c == '}') {
                break;
            } else {
                sb.append(c);
            }
        }
        //
        outPrintMethodEnd();
        //
    }

    private void parseComment(String waitEnds, char end1, char end2) throws Exception {
        setIgnore_empty(true);
        flushText();
        //
        sb.append("/*");
        //
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitString(waitEnds);
            }
            if (c == '-') {
                char c1 = next();
                if (c1 == '-') {
                    char c2 = next();
                    if (c2 == end1) {
                        char c3 = next();
                        if (c3 == end2) {
                            break;
                        }
                        push(c3);
                    }
                    push(c2);
                }
                push(c1);
            }

            if (c == '*') {
                sb.append("\\*\\");
            } else {
                sb.append(c);
            }

        }
        //
        sb.append("*/");
        //
    }

    private void parseCommentGsp() throws Exception {
        // уже прочитаны '%{'
        char c1 = next();
        if (c1 == '-') {
            char c2 = next();
            if (c2 == '-') {
                parseComment("--}%", '}', '%');
            } else {
                outText('%');
                outText('{');
                outText('-');
                push(last);
            }
        } else {
            outText('%');
            outText('{');
            push(last);
        }
    }

    private void parseDirective() throws Exception {
        setIgnore_empty(true);
        flushText();
        //
        parseWhite();

        char c = next();
        if (!UtString.isIdnStartChar(c)) {
            error("Ожидается идентификатор");
        }

        StringBuilder bIdn = new StringBuilder();
        bIdn.append(c);
        parseIdn(bIdn);

        if (!bIdn.toString().equals("page")) {
            error("Ожидается идентификатор 'page'");
        }

        int cntEOL = 0;
        while (true) {
            c = next();
            if (last == EOF) {
                errorWaitString("%>");
            }
            if (UtString.isWhiteChar(c)) {
                if (c == '\n') {
                    cntEOL++;
                }
            } else if (UtString.isIdnChar(c)) {
                StringBuilder bA = new StringBuilder();
                StringBuilder bV = new StringBuilder();
                parseAttr(bA, bV);
                String a = bA.toString();
                if (!a.equals("import")) {
                    error("Ожидается идентификатор 'import'");
                }
                //
                String importAttr = bV.toString();
                if (importAttr.length() == 0) {
                    throw new Exception("Значение для атрибута 'import' не задано");
                }
                //
                String ar[] = importAttr.split(";");
                for (int i = 0; i < ar.length; i++) {
                    String s1 = ar[i].trim();
                    if (s1.length() > 0) {
                        sb.append("import ");
                        sb.append(s1);
                        sb.append("; ");
                    }
                }
            } else if (c == '%') {
                char c1 = next();
                if (c1 == '>') {
                    // конец 
                    break;
                } else {
                    errorWaitChars(">");
                }
            } else {
                error("Не ожидаемый символ '" + c + "'");
            }
        }
        if (cntEOL > 0) {
            sb.append(UtString.repeat("\n", cntEOL));
        }
    }

    private void parseTag() throws Exception {
        // прочитано <X

        // забираем idn
        StringBuilder bPrefix = new StringBuilder();
        bPrefix.append(last);
        parseIdn(bPrefix);

        // ':' ?
        char c = next();
        if (c != ':') {
            // не наш
            outText('<');
            outText(bPrefix);
            push(c);
            return;
        }

        // наш
        char c1 = next();
        if (UtString.isIdnStartChar(c1)) {
            StringBuilder bSuffix = new StringBuilder();
            bSuffix.append(c1);
            parseIdn(bSuffix);

            parseTagXc(bPrefix, bSuffix);

        } else {
            // опять не наш
            outText('<');
            outText(bPrefix);
            outText(c);
            outText(c1);
        }
    }

    /**
     * Разбор конца тега xc. Возвращает true, если конец тега найден
     */
    private boolean parseTagEnd(StringBuilder prefix, StringBuilder suffix) throws Exception {
        setIgnore_empty(true);
        // прочитано </

        char c = next();
        if (!UtString.isIdnStartChar(c)) {
            outText("</");
            outText(c);
            return false;
        }

        // забираем idn
        StringBuilder bPrefix = new StringBuilder();
        bPrefix.append(c);
        parseIdn(bPrefix);

        // ':' ?
        c = next();
        if (c != ':') {
            outText("</");
            outText(bPrefix);
            outText(c);
            return false;
        }

        c = next();
        if (!UtString.isIdnStartChar(c)) {
            error("Ожидается идентификатор");
        }

        // забираем idn
        StringBuilder bSuffix = new StringBuilder();
        bSuffix.append(c);
        parseIdn(bSuffix);

        c = next();
        if (c != '>') {
            errorWaitChars(">");
        }

        if (prefix.toString().equals(bPrefix.toString()) &&
                suffix.toString().equals(bSuffix.toString())) {
            flushText();
            sb.append('}');
            return true;
        }

        error("Закрывающий тег </" + bPrefix + ":" + bSuffix + "> не имеет открывающего");
        return false;
    }

    private void parseTagXc(StringBuilder prefix, StringBuilder suffix) throws Exception {
        setIgnore_empty(false);
        flushText();
        //

        sb.append(_nsTagTemplate.replace("$1", prefix).replace("$2", suffix));

        // атрибуты
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitString("/>");
            }
            if (UtString.isWhiteChar(c)) {
                sb.append(c);
            } else if (UtString.isIdnChar(c)) {
                sb.append(',');
                parseAttr(null, null);
            } else if (c == '/') {
                char c1 = next();
                if (c1 == '>') {
                    // конец однострочного
                    sb.append(')');
                    sb.append(';');
                    break;
                } else {
                    errorWaitChars(">");
                }
            } else if (c == '>') {
                // конец многострочного
                setIgnore_empty(true);
                sb.append(") {");
                parseBody(prefix, suffix);
                break;
            } else {
                push(last);
                break;
            }
        }

    }

    private void parseAttr(StringBuilder saveName, StringBuilder saveValue) throws Exception {
        StringBuilder bAttr = saveName;
        if (bAttr == null) {
            bAttr = new StringBuilder();
        }
        bAttr.append(last);
        parseIdn(bAttr);
        if (saveName == null) {
            if (bAttr.indexOf("-") == -1) {
                sb.append(bAttr);
            } else {
                sb.append('"');
                sb.append(bAttr);
                sb.append('"');
            }
        }

        parseWhite();

        char c = next();
        if (c != '=') {
            errorWaitChars("=");
        }
        if (saveName == null) {
            sb.append(':');
        }

        parseWhite();

        c = next();
        if (c != '\'' && c != '"') {
            errorWaitChars("'\"");
        }

        StringBuilder bValue = saveValue;
        if (bValue == null) {
            bValue = new StringBuilder();
        }
        parseAttrValue(bValue);

        if (saveValue == null) {
            boolean appendQ = !(bValue.length() > 3 && bValue.charAt(0) == '$' &&
                    bValue.charAt(1) == '{' && bValue.charAt(bValue.length() - 1) == '}');
            if (appendQ) {
                sb.append('"');
                sb.append(bValue);
                sb.append('"');
            } else {
                sb.append(bValue.substring(2, bValue.length() - 1));
            }
        }
    }

    /**
     * Извлекает следующий idn в b
     */
    private void parseAttrValue(StringBuilder b) throws Exception {
        // прочитано : первая кавычка
        char startQuote = last;
        while (true) {
            char c = next();
            if (last == EOF) {
                errorWaitChars("" + startQuote);
            }
            if (c == startQuote) {
                break;
            } else if (c == '\\') {
                char c1 = next();
                b.append(c);
                b.append(c1);
            } else {
                b.append(c);
            }
        }
    }

    private void parseWhite() throws Exception {
        while (true) {
            char c = next();
            if (last == EOF) {
                break;
            }
            if (UtString.isWhiteChar(c)) {
                sb.append(c);
            } else {
                push(last);
                break;
            }
        }
    }

    /**
     * Извлекает следующий idn в b
     */
    private void parseIdn(StringBuilder b) throws Exception {
        // прочитано <X
        boolean minus = false;
        while (true) {
            char c = next();
            if (last == EOF) {
                break;
            }
            if (UtString.isIdnChar(c)) {
                b.append(c);
                minus = false;
            } else if (c == '-') {
                b.append(c);
                minus = true;
            } else {
                push(last);
                break;
            }
        }
        if (minus) {
            error("Идентификатор не должен оканчиваться символом '-'");
        }
    }


    private void outText(char c) {
        if (c == EOF) {
            return;
        }
        _lastOutChar = c;
        nextstr.append(c);
        if (c == '\n') {
            flushText();
            sb.append(c);
            _ignore_empty = false;
            _code_instr = false;
        }
    }

    private void outText(CharSequence cs) {
        for (int i = 0; i < cs.length(); i++) {
            outText(cs.charAt(i));
        }
    }

    private void flushText() {
        if (_ignore_empty) {
            if (UtString.isWhite(nextstr)) {
                nextstr.setLength(0);
            } else {
                _ignore_empty = false;
            }
        }
        if (nextstr.length() > 0) {
            outPrintMethodStart("\"");
            for (int i = 0; i < nextstr.length(); i++) {
                char c = nextstr.charAt(i);
                if (c == '\n') {
                    sb.append("\\n");
                } else if (c == '\"') {
                    sb.append("\\\"");
                } else if (c == '\\') {
                    sb.append("\\\\");
                } else if (c == '$') {
                    sb.append("\\$");
                } else {
                    sb.append(c);
                }
            }
            outPrintMethodEnd("\"");
            nextstr.setLength(0);
            _code_instr = true;
        }
    }

    private void outPrintMethodStart() {
        sb.append(";;");
        sb.append(_printMethodName);
        sb.append("(");
    }

    private void outPrintMethodStart(String prefix) {
        outPrintMethodStart();
        sb.append(prefix);
    }

    private void outPrintMethodEnd() {
        sb.append(");");
    }

    private void outPrintMethodEnd(String suffix) {
        sb.append(suffix);
        outPrintMethodEnd();
    }

}
