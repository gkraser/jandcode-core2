package jandcode.mdoc.builder.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.mdoc.builder.*;

public class ConfOutBuilderDef extends BaseComp implements OutBuilderDef {

    private Conf conf;

    public ConfOutBuilderDef(App app, Conf conf) {
        setApp(app);
        setName(conf.getName());
        this.conf = conf;
    }

    public OutBuilder createInst() {
        return (OutBuilder) getApp().create(conf);
    }

}
