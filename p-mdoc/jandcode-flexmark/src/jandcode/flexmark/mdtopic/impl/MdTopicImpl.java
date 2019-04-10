package jandcode.flexmark.mdtopic.impl;


import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.util.ast.*;
import jandcode.flexmark.mdtopic.*;

import java.util.*;

/**
 * Представление md-файла
 */
public class MdTopicImpl implements MdTopic {

    private Document documentNode;
    private String body;
    private MdVisitorData visitorData;

    public MdTopicImpl(Document documentNode, HtmlRenderer renderer) {
        this.documentNode = documentNode;
        prepare(renderer);
    }

    private void prepare(HtmlRenderer renderer) {
        // обрабатываем
        MdVisitor v = new MdVisitor();
        v.visit(documentNode);
        this.visitorData = v.getData();

        // рендерим
        this.body = renderer.render(this.documentNode);
    }

    public Document getDocumentNode() {
        return documentNode;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return visitorData.getTitle();
    }

    public MdToc getToc() {
        return visitorData.getToc();
    }

    public Map<String, String> getProps() {
        return visitorData.getProps();
    }

}
