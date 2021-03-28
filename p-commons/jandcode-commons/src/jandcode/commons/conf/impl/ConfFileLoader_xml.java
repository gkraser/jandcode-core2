package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.io.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;

import java.io.*;

/**
 * Реализация загрузки одного файла в conf из формата xml.
 * Каждый узел - становится безымянным и в его атрибут $name сохраняется имя узла.
 * Все дочерние помещаются в безымянные свойства.
 * Все текстовые значения помещаются в атрибут text.
 * Комментарий начинающийся с @ - в атрибут comment.
 */
public class ConfFileLoader_xml extends DefaultHandler2 implements ILoader {

    private Conf root;
    private ConfLoaderContext loader;
    private StringBuilder buffer;
    protected StackList<Conf> stack;
    Locator locator;


    public ConfFileLoader_xml(Conf root, ConfLoaderContext loader) {
        this.root = root;
        this.loader = loader;
    }

    public void loadFrom(Reader reader) throws Exception {
        buffer = new StringBuilder();
        stack = new StackList<>();
        //
        XMLReader xreader = UtXml.createSimpleXMLReader(this);
        xreader.parse(UtLoad.getInputSource(reader));
        //
    }

    //////

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        Conf cur;
        if (stack.size() == 0) {
            cur = root;
        } else {
            assignValue();
            if (qName.startsWith(ConfFileLoader_cfx.FUNC_PREFIX)) {
                cur = UtConf.create(qName);
            } else {
                cur = stack.last().findConf("#", true);
                cur.setValue("$name", qName);
            }
        }
        stack.add(cur);
        loader.addOrigin(cur, null, this.locator.getLineNumber());

        int cnt = attributes.getLength();
        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                cur.put(attributes.getQName(i), loader.expandVars(attributes.getValue(i)));
            }
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    public void comment(char ch[], int start, int length) throws SAXException {
        StringBuilder sb = new StringBuilder();
        sb.append(ch, start, length);
        String s = sb.toString().trim();
        if (stack.size() > 1 && sb.length() > 0 && sb.charAt(0) == '@') {
            s = s.substring(1);
            s = UtString.normalizeIndent(s);
            if (s.length() > 0) {
                stack.last().put("comment", loader.expandVars(s));
            }
        }
    }

    protected void assignValue() {
        if (buffer.length() == 0) {
            return;
        }
        String s;
        s = buffer.toString().replaceAll("\r", "");
        s = UtString.normalizeIndent(s);
        if (s.length() > 0) {
            stack.last().put("text", loader.expandVars(s));
        }
        buffer.setLength(0);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        assignValue();
        Conf last = stack.pop();
        if (last.getName().startsWith(ConfFileLoader_cfx.FUNC_PREFIX)) {
            String fn = UtString.removePrefix(last.getName(), ConfFileLoader_cfx.FUNC_PREFIX);
            loader.execFunc(fn, last, stack.last());
        }
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
}
