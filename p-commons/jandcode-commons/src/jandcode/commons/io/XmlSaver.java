package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.collect.*;

import java.io.*;

/**
 * Абстрактная писалка XML
 */
public abstract class XmlSaver implements ISaver {

    private int _colWordWrap = 80; // в какой колонке переводить строку
    private boolean _alwaysCloseTag = false;
    private boolean _outputXmlHeader = true;
    private boolean _useIndent = true;

    protected IndentWriter writer;

    class StackItem {
        String nodeName;
        boolean isChild;
        boolean isAttr;
        boolean isText;
    }

    private StackList<StackItem> _stack = new StackList<StackItem>();

    //////

    public SaveTo save() {
        return new SaveTo(this);
    }

    public void saveTo(Writer writer) throws Exception {
        this.writer = new IndentWriter(writer);
        getIndentWriter().setEnable(isUseIndent());
        _stack.clear();
        String charset = UtSave.getCharset(writer);
        writeXmlHeader(charset);
        onSaveXml();
    }

    //////

    /**
     * Перекрыть в потомках для обеспечения функционала
     */
    protected abstract void onSaveXml() throws Exception;

    /**
     * Всегда использовать закрывающий тег (без наворотов типа '/>')
     */
    public boolean isAlwaysCloseTag() {
        return _alwaysCloseTag;
    }

    public void setAlwaysCloseTag(boolean alwaysCloseTag) {
        _alwaysCloseTag = alwaysCloseTag;
    }

    /**
     * Выводить ли XML заголовок.
     */
    public boolean isOutputXmlHeader() {
        return _outputXmlHeader;
    }

    /**
     * Выводить ли XML заголовок.
     */
    public void setOutputXmlHeader(boolean outputXmlHeader) {
        _outputXmlHeader = outputXmlHeader;
    }

    /**
     * В какой колонке переносить атрибуты
     */
    public int getColWordWrap() {
        return _colWordWrap;
    }

    /**
     * В какой колонке переносить атрибуты
     */
    public void setColWordWrap(int colWordWrap) {
        _colWordWrap = colWordWrap;
    }


    /**
     * Использовать выравнивание
     */
    public boolean isUseIndent() {
        return _useIndent;
    }

    /**
     * Использование выравниевания
     *
     * @param useIndent true-использовать, false-нет
     */
    public void setUseIndent(boolean useIndent) {
        _useIndent = useIndent;
    }

    /**
     * writer в режиме отступов
     */
    protected IndentWriter getIndentWriter() {
        return writer;
    }

    /**
     * Писать заголовок XML
     */
    protected void writeXmlHeader(String charset) throws IOException {
        if (_outputXmlHeader) {
            writer.write("<?xml version=\"1.0\" encoding=\"" + charset //NON-NLS
                    + "\"?>\n");
        }
    }

    private void prepareWriteChild() throws Exception {
        StackItem si = _stack.last();
        if (si == null) {
            return;
        }
        if (!si.isChild) {
            if (!si.isText) {
                writer.write(">");
            }
            si.isChild = true;
        }
    }

    protected void prepareWriteText() throws Exception {
        StackItem si = _stack.last();
        if (si == null) {
            return;
        }
        if (!si.isText) {
            if (!si.isChild) {
                writer.write(">");
            }
            si.isText = true;
        }
    }

    /**
     * Начать узел
     */
    protected void startNode(String name) throws Exception {
        prepareWriteChild();
        StackItem si = new StackItem();
        si.nodeName = name;
        _stack.add(si);

        if (_stack.size() > 1) { // не корневая
            if (isUseIndent()) {
                getIndentWriter().indentInc();
                writer.write("\n");
            }
        }
        writer.write("<");
        writer.write(name);
    }

    /**
     * Закончить узел
     */
    protected void stopNode() throws Exception {
        StackItem si = _stack.last();
        if (si == null) {
            return;
        }
        if (isAlwaysCloseTag()) {
            prepareWriteText();
        }

        if (!si.isChild && !si.isAttr && !si.isText) {
            writer.write("/>");
        } else if (!si.isChild && !si.isText && si.isAttr) {
            writer.write("/>");
        } else {
            if (si.isChild) {
                if (isUseIndent()) {
                    writer.write("\n");
                }
            }
            writer.write("</");
            writer.write(si.nodeName);
            writer.write(">");
        }

        if (isUseIndent()) {
            getIndentWriter().indentDec();
        }
        _stack.pop();
    }

    /**
     * Писать атрибут
     */
    protected void writeAttr(String name, String value) throws Exception {
        StackItem si = _stack.last();
        if (si == null) {
            return;
        }

        if (isUseIndent()) {
            if (getIndentWriter().getCol() + name.length() + value.length() + 4 > _colWordWrap) {
                writer.write("\n");
                writeIndent(si.nodeName.length() + 1);
            }
        }
        writer.write(" ");
        writer.write(name);
        writer.write("=\"");
        boolean saveEnable = getIndentWriter().isEnable();
        getIndentWriter().setEnable(false);
        writeQuoteAttrValue(value);
        getIndentWriter().setEnable(saveEnable);
        writer.write("\"");
    }

    /**
     * Писать текст
     */
    protected void writeText(String text) throws Exception {
        prepareWriteText();
        boolean saveEnable = getIndentWriter().isEnable();
        getIndentWriter().setEnable(false);
        writeQuoteNodeValue(text);
        getIndentWriter().setEnable(saveEnable);
    }

    /**
     * Писать коментарий
     */
    protected void writeCommentNode(String text) throws Exception {
        prepareWriteChild();
        if (isUseIndent()) {
            getIndentWriter().indentInc();
            writer.write("\n");
        }

        boolean saveEnable = getIndentWriter().isEnable();
        writer.write("<!--");
        getIndentWriter().setEnable(false);
        writeQuoteNodeValue(text);
        writer.write("-->");
        getIndentWriter().setEnable(saveEnable);

        if (isUseIndent()) {
            getIndentWriter().indentDec();
        }
    }

    /**
     * Писать текст
     */
    protected void writeTextNode(String text) throws Exception {
        prepareWriteChild();
        if (isUseIndent()) {
            getIndentWriter().indentInc();
            writer.write("\n");
        }

        boolean saveEnable = getIndentWriter().isEnable();

        int a = text.indexOf('\n');
        if (a == -1) {
            writeQuoteNodeValue(text);
        } else {
            writeQuoteNodeValue(text.substring(0, a));
            getIndentWriter().setEnable(false);
            writeQuoteNodeValue(text.substring(a));
        }

        getIndentWriter().setEnable(saveEnable);

        if (isUseIndent()) {
            getIndentWriter().indentDec();
        }
    }

    /**
     * Писать значение атрибута в закодированном виде
     */
    protected void writeQuoteAttrValue(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '&') {
                writer.write("&amp;"); //NON-NLS
            } else if (c == '<') {
                writer.write("&lt;"); //NON-NLS
            } else if (c == '>') {
                writer.write("&gt;"); //NON-NLS
            } else if (c == '"') {
                writer.write("&quot;"); //NON-NLS
            } else if (c < 32 && c != 10 && c != 13) {
                writer.write(' ');
            } else {
                writer.write(c);
            }
        }
    }

    /**
     * Писать текст в закодированном виде
     */
    protected void writeQuoteNodeValue(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '&') {
                writer.write("&amp;"); //NON-NLS
            } else if (c == '<') {
                writer.write("&lt;"); //NON-NLS
            } else if (c == '>') {
                writer.write("&gt;"); //NON-NLS
            } else if (c < 32 && c != 10 && c != 13) {
                writer.write(' ');
            } else {
                writer.write(c);
            }
        }
    }

    /**
     * Писать count символов ' '
     */
    protected void writeIndent(int count) throws Exception {
        if (!isUseIndent()) {
            return;
        }
        for (int i = 0; i < count; i++) {
            writer.write(' ');
        }
    }

    /**
     * Писать eol и отступы
     */
    protected void writeEol() throws IOException {
        writer.write("\n");
    }
}
