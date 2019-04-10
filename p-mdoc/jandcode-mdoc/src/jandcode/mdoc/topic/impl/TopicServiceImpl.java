package jandcode.mdoc.topic.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

import java.util.*;

public class TopicServiceImpl extends BaseComp implements TopicService {

    private NamedList<TopicFactoryDef> topicFactorys = new DefaultNamedList<>();
    private List<TopicFactoryBindDef> topicFactoryBinds = new ArrayList<>();

    class TopicFactoryDef extends Named {

        TopicFactory factory;

        public TopicFactoryDef(Conf conf) {
            this.setName(conf.getName());
            this.factory = (TopicFactory) getApp().create(conf);
        }

        public TopicFactory getFactory() {
            return factory;
        }
    }

    class TopicFactoryBindDef {

        TopicFactoryDef factoryDef;
        List<String> masks = new ArrayList<>();

        public TopicFactoryBindDef(Conf conf) {
            this.factoryDef = topicFactorys.get(conf.getString("factory"));
            masks.addAll(UtCnv.toList(conf.getString("masks")));
        }

        public TopicFactory getFactory() {
            return factoryDef.getFactory();
        }

        public boolean match(String path) {
            for (String mask : this.masks) {
                if (UtVDir.matchPath(mask, path)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf x : getApp().getConf().getConfs("mdoc/topic-factory")) {
            TopicFactoryDef f = new TopicFactoryDef(x);
            topicFactorys.add(f);
        }

        //
        for (Conf x : getApp().getConf().getConfs("mdoc/topic-factory-bind")) {
            TopicFactoryBindDef f = new TopicFactoryBindDef(x);
            topicFactoryBinds.add(0, f);  // чем позже, тем приоритетней
        }

    }

    public TopicFactory findTopicFactory(SourceFile sourceFile, Doc doc) {
        String path = sourceFile.getPath();
        for (TopicFactoryBindDef bind : this.topicFactoryBinds) {
            if (bind.match(path)) {
                return bind.getFactory();
            }
        }
        return null;
    }
}
