package jandcode.commons.simxml.impl;

import jandcode.commons.error.*;

import java.util.*;

/**
 * Представление пути для SimXml
 */
public class SimXmlPath {

    private String attrName;
    private List<Item> items = new ArrayList<>();

    public class Item {
        String nodeName;
        String attrName;
        String attrValue;

        public Item(String nodeName, String attrName, String attrValue) {
            this.nodeName = nodeName;
            this.attrName = attrName;
            this.attrValue = attrValue;
        }

        public String getNodeName() {
            return nodeName;
        }

        public String getAttrName() {
            return attrName;
        }

        public boolean hasAttrName() {
            return attrName != null;
        }

        public String getAttrValue() {
            return attrValue;
        }
    }

    public SimXmlPath(String path) {
        parse(path);
    }

    private void parse(String path) {
        int a = path.indexOf(':');
        if (a != -1) {
            this.attrName = path.substring(a + 1);
            path = path.substring(0, a);
        }
        if (path.length() != 0) {
            a = path.indexOf('/');
            if (a == -1) {
                a = path.indexOf('@');
            }
            if (a == -1) {
                this.items.add(new Item(path, null, null));
            } else {
                String[] ar = path.split("/");
                for (String s1 : ar) {
                    a = s1.indexOf('@');
                    if (a == -1) {
                        this.items.add(new Item(s1, null, null));
                    } else {
                        String nodeName = s1.substring(0, a);
                        String s2 = s1.substring(a + 1);
                        a = s2.indexOf('=');
                        if (a == -1) {
                            throw new XError("Не указано имя атрибута [{0}]", s2);
                        } else {
                            this.items.add(new Item(nodeName, s2.substring(0, a), s2.substring(a + 1)));
                        }
                    }
                }
            }
        }
    }

    public String getAttrName() {
        return attrName;
    }

    public boolean hasAttrName() {
        return attrName != null;
    }

    public List<Item> getItems() {
        return items;
    }

}
