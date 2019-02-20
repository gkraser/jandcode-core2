package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.io.*;

/**
 * Запись Conf в xml канонического формата.
 */
public class ConfXmlSaver extends XmlSaver {

    private Conf root;

    public ConfXmlSaver(Conf root) {
        this.root = root;
        setAlwaysCloseTag(true);
    }

    protected void onSaveXml() throws Exception {
        saveNode(this.root, "root", null);
    }

    private void saveNode(Conf node, String nodeName, String propName) throws Exception {

        startNode(normNodeName(nodeName));
        if (propName != null) {
            writeAttr("x-name", propName);
        }

        for (String p : node.keySet()) {
            Object v = node.getValue(p);
            propName = null;
            String prop = normNodeName(p);
            if (prop.startsWith(ConfConsts.NONAME_KEY)) {
                prop = ConfConsts.XML_PROP_NONAME;
            } else if (!UtString.isXmlName(prop) || prop.equals(ConfConsts.XML_PROP_NONAME) || prop.startsWith("x-")) {
                propName = prop;
                prop = "prop";
            }
            if (v instanceof Conf) {
                saveNode((Conf) v, prop, propName);
            } else {
                startNode(normNodeName(prop));
                if (propName != null) {
                    writeAttr("x-name", propName);
                }
                writeText(UtCnv.toString(v));
                stopNode();
            }
        }

        stopNode();
    }

    private String normNodeName(String nodeName) {
        if (nodeName.indexOf('!') != -1) {
            return nodeName.replace("!", "--");
        }
        return nodeName;
    }

}