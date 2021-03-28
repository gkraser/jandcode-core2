package jandcode.commons.simxml;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.io.*;
import jandcode.commons.variant.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;

import java.io.*;

/**
 * Загрузка SimXml из xml.
 */
public class SimXmlLoader extends DefaultHandler2 implements ILoader {

    protected SimXml root;
    private boolean trimSpace = true;
    private boolean trimSpaceEnd = false;
    private boolean loadComment = false;

    private StringBuilder buffer;
    protected StackList<SimXml> stack;

    public SimXmlLoader(SimXml root) {
        this.root = root;
    }

    public void loadFrom(Reader reader) throws Exception {
        root.clear();
        buffer = new StringBuilder();
        stack = new StackList<>();
        //
        XMLReader xreader = UtXml.createSimpleXMLReader(this);
        xreader.parse(UtLoad.getInputSource(reader));
        //
    }

    //////

    /**
     * Обрезать ли пробелы в тексте. По умолчанию - true
     */
    public void setTrimSpace(boolean trimSpace) {
        this.trimSpace = trimSpace;
    }

    /**
     * Где обрезать пробелы. При значении true - только в конце. По умолчанию false
     */
    public void setTrimSpaceEnd(boolean trimSpaceEnd) {
        this.trimSpaceEnd = trimSpaceEnd;
    }

    /**
     * Загружать ли коментарии.
     * При true - коментарии загружаются в узлы '#comment'.
     * При false - пропускаются.
     * По умолчанию - false.
     */
    public void setLoadComment(boolean loadComment) {
        this.loadComment = loadComment;
    }

    //////

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        SimXml cur;
        if (stack.size() == 0) {
            cur = root;
            cur.setName(qName);
        } else {
            assignValue();
            cur = stack.last().addChild(qName);
        }
        stack.add(cur);

        int cnt = attributes.getLength();
        if (cnt > 0) {
            IVariantMap m = cur.getAttrs();
            for (int i = 0; i < cnt; i++) {
                m.put(attributes.getQName(i), attributes.getValue(i));
            }
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        assignValue();
        stack.pop();
    }

    protected void assignValue() {
        if (buffer.length() == 0) {
            return;
        }
        String s;
        if (trimSpace) {
            if (trimSpaceEnd) {
                s = UtString.trimLast(buffer.toString().replaceAll("\r", ""));
            } else {
                s = buffer.toString().trim().replaceAll("\r", "");
            }
        } else {
            s = buffer.toString().replaceAll("\r", "");
        }
        if (s.length() > 0) {
            if (stack.last().hasChilds()) {
                stack.last().addChild(SimXmlConsts.NODE_TEXT).setText(s);
            } else {
                stack.last().setText(s);
            }
        }
        buffer.setLength(0);
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (!this.loadComment) {
            return;
        }
        assignValue(); // текст до коментария

        StringBuilder sb = new StringBuilder();
        sb.append(ch, start, length);

        String s;
        if (trimSpace) {
            if (trimSpaceEnd) {
                s = UtString.trimLast(sb.toString().replaceAll("\r", ""));
            } else {
                s = sb.toString().trim().replaceAll("\r", "");
            }
        } else {
            s = sb.toString().replaceAll("\r", "");
        }

        stack.last().addChild(SimXmlConsts.NODE_COMMENT).setText(s);
    }

}