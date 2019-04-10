package jandcode.commons.simxml;

import jandcode.commons.io.*;
import jandcode.commons.variant.*;

/**
 * Запись SimXml в xml.
 */
public class SimXmlSaver extends XmlSaver {

    private SimXml root;
    private boolean saveRootNode = true;


    public SimXmlSaver(SimXml root) {
        this.root = root;
    }

    /**
     * Записывать ли корневой узел. По умолчанию - true.
     */
    public void setSaveRootNode(boolean saveRootNode) {
        this.saveRootNode = saveRootNode;
    }

    protected void onSaveXml() throws Exception {
        saveInternal(root);
    }

    private void saveInternal(SimXml node) throws Exception {
        saveNode(node, saveRootNode);
    }

    private void saveNode(SimXml node, boolean saveSelfTag) throws Exception {
        String nodeName = node.getName();

        if (SimXmlConsts.NODE_COMMENT.equals(nodeName)) {
            writeCommentNode(node.getText());
            return;
        }

        if (SimXmlConsts.NODE_TEXT.equals(nodeName)) {
            writeTextNode(node.getText());
            return;
        }

        if (saveSelfTag) {

            if (nodeName.length() == 0) {
                nodeName = node == this.root ? SimXmlConsts.NODE_DEFAULT_ROOT : SimXmlConsts.NODE_DEFAULT;
            }

            startNode(nodeName);

            if (node.hasAttrs()) {

                IVariantMap m = node.getAttrs();
                for (String an : m.keySet()) {
                    writeAttr(an, m.getString(an));
                }

            }

        }

        if (node.hasText()) {
            writeText(node.getText());
        }

        if (node.hasChilds()) {
            for (SimXml n : node.getChilds()) {
                saveNode(n, true);
            }
        }

        if (saveSelfTag) {
            stopNode();
        }

    }

}