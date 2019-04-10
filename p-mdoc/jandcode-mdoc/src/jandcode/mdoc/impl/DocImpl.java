package jandcode.mdoc.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.source.impl.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

import java.util.*;

public class DocImpl extends BaseComp implements Doc {

    private DocCfg cfg;
    private SourceFileHolder sourceFiles = new SourceFileHolderImpl();
    private TopicHolder topics = new TopicHolderImpl();
    private DocMode mode = new DocMode();

    public void configure(DocCfg cfg) throws Exception {
        this.cfg = cfg;
    }

    public void load() throws Exception {
        this.sourceFiles = new SourceFileHolderImpl();
        this.topics = new TopicHolderImpl();

        // path файлов, которые потенциально - исходники для статей
        Set<String> topicSources = new LinkedHashSet<>();

        // исходники по умолчанию
        for (Conf srcDefault : getApp().getConf().getConfs("mdoc/src-default")) {
            FilesetCfg fs = getApp().bean(DocCfgService.class).createFilesetCfg(srcDefault);
            FilesetSourceFileLoader loader = new FilesetSourceFileLoader(getApp(), fs, "");
            List<SourceFile> lst = loader.loadFiles();
            if (lst != null) {
                for (SourceFile f : lst) {
                    this.sourceFiles.add(f);
                }
            }
        }

        // исходные файлы
        for (FilesetCfg fs : getCfg().getSrcs()) {
            FilesetSourceFileLoader loader = new FilesetSourceFileLoader(getApp(), fs, null);
            List<SourceFile> lst = loader.loadFiles();
            if (lst != null) {
                for (SourceFile f : lst) {
                    this.sourceFiles.add(f);
                }
                if (!fs.isResources()) {
                    for (SourceFile f : lst) {
                        // эти файлы (их имена) - потенциальные статьи
                        topicSources.add(f.getPath());
                    }
                }
            }
        }

        // статьи среди исходных файлов только в topicSources
        for (String tf : topicSources) {
            SourceFile f = this.sourceFiles.find(tf);
            if (f != null) {
                TopicFactory factory = findTopicFactory(f);
                if (factory != null) {
                    Topic topic = new TopicWrap(f, this, factory);
                    this.topics.add(topic);
                }
            }
        }
    }

    ///

    public DocCfg getCfg() {
        return cfg;
    }

    public DocMode getMode() {
        return mode;
    }

    public SourceFileHolder getSourceFiles() {
        return sourceFiles;
    }

    public TopicHolder getTopics() {
        return topics;
    }

    protected TopicFactory findTopicFactory(SourceFile sourceFile) {
        return getApp().bean(MDocService.class).getTopicService().findTopicFactory(sourceFile, this);
    }

    public Topic createTopic(SourceFile sourceFile) {
        TopicFactory factory = findTopicFactory(sourceFile);
        if (factory == null) {
            throw new XError("Для файла {0} не предусмотрена фабрика статей", sourceFile.getPath());
        }
        return new TopicWrap(sourceFile, this, factory);
    }

    public OutBuilder createBuilder(String name) throws Exception {
        return getApp().bean(OutBuilderService.class).createBuilder(name, this);
    }
}
