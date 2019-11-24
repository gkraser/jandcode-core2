package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;

import java.io.*;
import java.util.*;

/**
 * Реализация загрузки одного файла в conf из формата cfx
 */
public class ConfFileLoader_cfx extends DefaultHandler2 implements ILoader {

    // префикс для функций
    public static final String FUNC_PREFIX = "x-"; //NON-NLS

    // функция if
    public static final String FUNC_IF = "if"; //NON-NLS

    // функция if-not
    public static final String FUNC_IF_NOT = "if-not"; //NON-NLS

    // имя атрибута, в котором хранится имя узла, т.к. это имя неправильное xml-имя
    public static final String ATTR_X_NAME = "x-name"; //NON-NLS

    // специальный атрибут name узел описывает свойство node/name, а не node
    public static final String ATTR_NAME = "name"; //NON-NLS

    private Conf root;
    private ConfLoaderContext loader;
    private StringBuilder buffer;
    private StackList<StackItem> stack;
    Locator locator;
    private int ignoreLevel = 0;

    class StackItem {
        StackItem prev;
        String name;
        String func;
        String text;
        String comment;
        Conf conf;
        int lineNum;

        public StackItem(StackItem prev, String name) {
            this.prev = prev;
            setName(name);
        }

        void setName(String name) {
            this.name = UtConf.normalizeName(name);
            this.func = UtString.removePrefix(this.name, FUNC_PREFIX);
            if (this.name.equals(ConfConsts.XML_PROP_NONAME)) {
                this.name = ConfConsts.NONAME_KEY;
            }
        }

        Conf getConf() {
            if (prev == null) {
                return root;
            }
            if (conf == null) {
                // первый раз
                if (func != null) {
                    // функция имеет собственный не связанный conf
                    conf = UtConf.create();
                } else {
                    // берем у родителя
                    conf = prev.getConf().findConf(this.name, true);
                }
                loader.addOrigin(conf, null, lineNum);
            }
            return conf;
        }
    }

    public ConfFileLoader_cfx(Conf root, ConfLoaderContext loader) {
        this.root = root;
        this.loader = loader;
    }

    public LoadFrom load() {
        return new LoadFrom(this);
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

        if (ignoreLevel > 0) {
            ignoreLevel++;
            return;
        }

        StackItem cur;
        if (stack.size() == 0) {
            cur = new StackItem(null, qName);
        } else {
            assignValue();
            cur = new StackItem(stack.last(), qName);
        }
        cur.lineNum = this.locator.getLineNumber();
        stack.add(cur);

        int cnt = attributes.getLength();
        if (cnt > 0) {
            Map<String, Object> attrs = new LinkedHashMap<>();
            String valueNameAttr = null;
            for (int i = 0; i < cnt; i++) {
                String an = attributes.getQName(i);
                String av = attributes.getValue(i);
                if (ATTR_X_NAME.equals(an)) {
                    cur.setName(av);  // имя еще можно менять
                } else if (ATTR_NAME.equals(an)) {
                    valueNameAttr = UtConf.normalizeName(av);    // значение атрибута name, особый случай
                } else {
                    attrs.put(an, loader.expandVars(av));
                }
            }
            if (valueNameAttr != null) {
                if (cur.func == null) {
                    // это не функция, перемещаем
                    if (valueNameAttr.equals(ConfConsts.XML_PROP_NONAME)) {
                        valueNameAttr = ConfConsts.NONAME_KEY;
                    }
                    cur.name = cur.name + "/" + valueNameAttr;
                    cur.getConf();  // это точно conf
                } else {
                    // это функция, отдаем ей атрибут name
                    cur.getConf().setValue(ATTR_NAME, valueNameAttr);
                }
            }
            if (attrs.size() > 0) {
                for (Map.Entry<String, Object> en : attrs.entrySet()) {
                    cur.getConf().setValue(en.getKey(), en.getValue());
                }
            }
        }

        // x-if
        if (cur.func != null) {
            if (cur.func.equals(FUNC_IF) || cur.func.equals(FUNC_IF_NOT)) {
                boolean trueValue = cur.func.equals(FUNC_IF);
                Object v = loader.evalExpression(cur.getConf());
                if (v instanceof Boolean) {
                    Boolean vb = (Boolean) v;
                    if (vb == trueValue) {
                        cur.conf = cur.prev.getConf();
                    } else {
                        ignoreLevel = 1;
                    }
                } else {
                    throw new XError("Выражение должно возвращать boolean");
                }
            }
        }


    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (ignoreLevel > 0) {
            return;
        }
        buffer.append(ch, start, length);
    }

    public void comment(char ch[], int start, int length) throws SAXException {
        if (ignoreLevel > 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ch, start, length);
        String s = sb.toString().trim();
        if (stack.size() > 1 && sb.length() > 0 && sb.charAt(0) == '@') {
            s = s.substring(1);
            s = UtString.normalizeIndent(s);
            if (s.length() > 0) {
                stack.last().comment = s;
            }
        }
    }

    protected void assignValue() {
        if (ignoreLevel > 0) {
            return;
        }
        if (buffer.length() == 0) {
            return;
        }
        String s;
        s = buffer.toString().replaceAll("\r", "");
        s = UtString.normalizeIndent(s);
        if (s.length() > 0) {
            stack.last().text = s;
        }
        buffer.setLength(0);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (ignoreLevel > 0) {
            ignoreLevel--;
            if (ignoreLevel > 0) {
                return;
            }
        }

        assignValue();
        StackItem cur = stack.pop();
        StackItem prev = stack.last();

        if (prev == null) {
            return; // корень закрылся
        }

        if (cur.comment != null) {
            String comment = UtString.trimLast(cur.comment);
            if (comment.length() > 0) {
                cur.getConf().setValue(ConfConsts.XML_PROP_COMMENT, loader.expandVars(comment));
            }
        }

        if (cur.func != null) {
            // это функция, выполняем
            loader.execFunc(cur.func, cur.getConf(), prev.getConf());

        } else if (cur.conf == null) {
            // свойств нет
            String text = cur.text;
            if (text != null) {
                text = UtString.trimLast(text);
            }
            if (!UtString.empty(text)) {
                // текст имеется, это атрибут владельца
                Object prevValue = prev.getConf().getValue(cur.name);
                if (!(prevValue instanceof Conf)) {
                    // существующее значение не Conf, обновляем 
                    prev.getConf().setValue(cur.name, loader.expandVars(text));
                    loader.addOrigin(prev.getConf(), cur.name, cur.lineNum);
                }
            } else {
                // это пустой контейнер
                prev.getConf().findConf(cur.name, true);
                loader.addOrigin(prev.getConf(), cur.name, cur.lineNum);
            }

        }

    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
}
