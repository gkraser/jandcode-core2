package jandcode.mdoc.topic.impl;

import jandcode.commons.variant.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

public class TopicWrap implements Topic {

    private Topic topicWrap;
    private Doc doc;
    private TopicFactory topicFactory;
    private SourceFile sourceFile;

    public TopicWrap(SourceFile sourceFile, Doc doc, TopicFactory topicFactory) {
        this.doc = doc;
        this.topicFactory = topicFactory;
        this.sourceFile = sourceFile;
        reload();
    }

    public String getId() {
        return topicWrap.getId();
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public String getBody() {
        return topicWrap.getBody();
    }

    public String getTitle() {
        return topicWrap.getTitle();
    }

    public String getTitleShort() {
        return topicWrap.getTitleShort();
    }

    public Toc getToc() {
        return topicWrap.getToc();
    }

    public IVariantMap getProps() {
        return topicWrap.getProps();
    }

    public void reload() {
        this.topicWrap = topicFactory.createTopic(this.sourceFile, this.doc);
    }

}
