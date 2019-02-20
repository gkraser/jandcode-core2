package jandcode.commons.simxml.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.simxml.*;
import jandcode.commons.variant.*;

import java.util.*;

public class SimXmlImpl implements SimXml {

    protected static List<SimXml> EMPTY_CHILDS = Collections.unmodifiableList(new ArrayList<>());

    private String name;
    private String text;
    private IVariantMap attrs;
    private List<SimXml> childs;

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public void setName(String name) {
        this.name = UtStrDedup.normal(name);
    }

    public boolean hasName() {
        return this.name != null && this.name.length() > 0;
    }

    public boolean hasName(String name) {
        return this.name != null && this.name.equals(name);
    }

    //////

    public String getText() {
        return this.text == null ? "" : this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean hasText() {
        return this.text != null && this.text.length() > 0;
    }

    //////

    public boolean hasAttrs() {
        return this.attrs != null && this.attrs.size() > 0;
    }

    public void clearAttrs() {
        this.attrs = null;
    }

    public IVariantMap getAttrs() {
        if (this.attrs == null) {
            this.attrs = new SimXmlAttrs();
        }
        return this.attrs;
    }

    //////

    public boolean hasChilds() {
        return this.childs != null && this.childs.size() > 0;
    }

    public void clearChilds() {
        this.childs = null;
    }

    public List<SimXml> getChilds() {
        if (this.childs == null) {
            return EMPTY_CHILDS;
        } else {
            return this.childs;
        }
    }

    public void addChild(SimXml child) {
        if (this.childs == null) {
            this.childs = new ArrayList<>();
        }
        this.childs.add(child);
    }

    public SimXml addChild(String name) {
        SimXmlImpl x = new SimXmlImpl();
        x.setName(name);
        addChild(x);
        return x;
    }

    //////


    public void removeChild(SimXml x) {
        if (this.childs == null) {
            return;
        }
        this.childs.remove(x);
    }

    public void removeChild(int index) {
        if (this.childs == null) {
            return;
        }
        this.childs.remove(index);
    }

    //////

    public SimXml findChild(String path, boolean createIfNotExist) {
        SimXmlPath p = new SimXmlPath(path);
        return internalFindChild(p, createIfNotExist);
    }

    protected SimXml internalFindChild(SimXmlPath path, boolean createIfNotExist) {
        SimXmlImpl cur = this;
        for (SimXmlPath.Item pi : path.getItems()) {
            SimXmlImpl found = cur.internalFindSelfChild(pi);
            if (found == null) {
                if (!createIfNotExist) {
                    return null;
                }
                found = cur.internalAddChild(pi);
            }
            cur = found;
        }
        return cur;
    }

    protected SimXmlImpl internalFindSelfChild(SimXmlPath.Item p) {
        if (this.childs != null) {
            String nn = p.getNodeName();
            for (SimXml x : this.childs) {
                if (x.hasName(nn)) {
                    if (p.hasAttrName()) {
                        if (!x.hasAttrs()) {
                            // атрибутов нет
                            continue;
                        }
                        String av = x.getAttrs().getString(p.getAttrName());
                        if (!av.equals(p.getAttrValue())) {
                            // значение атрибута не соответствует
                            continue;
                        }
                    }
                    return (SimXmlImpl) x;
                }
            }
        }
        return null;
    }

    protected SimXmlImpl internalAddChild(SimXmlPath.Item pi) {
        SimXmlImpl x = new SimXmlImpl();
        x.setName(pi.getNodeName());
        if (pi.hasAttrName()) {
            x.getAttrs().put(pi.getAttrName(), pi.getAttrValue());
        }
        addChild(x);
        return x;
    }

    //////

    public void clear() {
        this.text = null;
        clearAttrs();
        clearChilds();
    }

    //////

    public Object getValue(String path) {
        if (UtString.empty(path)) {
            return null;
        }
        int a = path.indexOf('/');
        if (a == -1) {
            a = path.indexOf(':');
        }
        if (a == -1) {
            a = path.indexOf('@');
        }
        if (a == -1) {
            // просто атрибут
            return internalGetValueAttr(path);
        } else {
            // путь имеется
            SimXmlPath p = new SimXmlPath(path);
            if (!p.hasAttrName()) {
                // путь есть, но атрибута нет
                return null;
            }
            SimXml fnd = internalFindChild(p, false);
            if (fnd == null) {
                return null;
            }
            return ((SimXmlImpl) fnd).internalGetValueAttr(p.getAttrName());
        }
    }

    protected Object internalGetValueAttr(String attrName) {
        if (SimXmlConsts.ATTR_TEXT.equals(attrName)) {
            return getText();
        } else {
            if (this.attrs == null) {
                return null;
            }
            return this.attrs.get(attrName);
        }
    }

    public void setValue(String path, Object value) {
        if (UtString.empty(path)) {
            return;
        }
        int a = path.indexOf('/');
        if (a == -1) {
            a = path.indexOf(':');
        }
        if (a == -1) {
            a = path.indexOf('@');
        }
        if (a == -1) {
            // просто атрибут
            internalSetValueAttr(path, value);
        } else {
            // путь имеется
            SimXmlPath p = new SimXmlPath(path);
            if (!p.hasAttrName()) {
                // путь есть, но атрибута нет
                return;
            }
            SimXml fnd = internalFindChild(p, true);
            ((SimXmlImpl) fnd).internalSetValueAttr(p.getAttrName(), value);
        }
    }

    protected void internalSetValueAttr(String attrName, Object value) {
        if (SimXmlConsts.ATTR_TEXT.equals(attrName)) {
            setText(UtCnv.toString(value));
        } else {
            getAttrs().put(attrName, value);
        }
    }

    //////

    public LoadFrom load() {
        return new LoadFrom(new SimXmlLoader(this));
    }

    public SaveTo save() {
        return new SaveTo(new SimXmlSaver(this));
    }

}
