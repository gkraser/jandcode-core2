package jandcode.mdoc.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.source.impl.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

/**
 * Враппер для документа.
 * Содержит копии списков файлов и статей.
 */
public class DocWrap implements Doc {

    private Doc docWrap;
    private SourceFileHolder sourceFiles;
    private TopicHolder topics;

    public DocWrap(Doc doc) {
        this.docWrap = doc;

        // заменяем списки статей и файлов на собственные копии
        this.sourceFiles = new SourceFileHolderImpl();
        for (SourceFile f : doc.getSourceFiles()) {
            this.sourceFiles.add(f);
        }

        this.topics = new TopicHolderImpl();
        for (Topic t : doc.getTopics()) {
            this.topics.add(t);
        }

    }

    /**
     * Оригинальный документ
     */
    public Doc getDocWrap() {
        return docWrap;
    }

    public App getApp() {
        return docWrap.getApp();
    }

    public DocCfg getCfg() {
        return docWrap.getCfg();
    }

    public DocMode getMode() {
        return docWrap.getMode();
    }

    public void load() throws Exception {
        throw new XError("load for DocWrap unsupported");
    }

    public SourceFileHolder getSourceFiles() {
        return this.sourceFiles;
    }

    public TopicHolder getTopics() {
        return this.topics;
    }

    public Topic createTopic(SourceFile sourceFile) {
        return docWrap.createTopic(sourceFile);
    }

    public OutBuilder createBuilder(String name) throws Exception {
        return docWrap.createBuilder(name);
    }

}
