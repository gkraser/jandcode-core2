package jandcode.mdoc.impl;

import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.cfg.*;
import jandcode.mdoc.topic.*;

public class MDocServiceImpl extends BaseComp implements MDocService {

    public TopicService getTopicService() {
        return getApp().bean(TopicService.class);
    }

    public DocCfgService getDocCfgService() {
        return getApp().bean(DocCfgService.class);
    }

    public Doc createDocument(DocCfg cfg) throws Exception {
        DocImpl doc = getApp().create(DocImpl.class);
        doc.configure(cfg);
        return doc;
    }

}
