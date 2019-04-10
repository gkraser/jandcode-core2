package jandcode.mdoc.builder.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.cm.*;
import jandcode.mdoc.source.*;

import java.util.*;

public class OutBuilderServiceImpl extends BaseComp implements OutBuilderService {

    private NamedList<OutBuilderDef> builders = new DefaultNamedList<>();
    private NamedList<CmDef> cms = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf x : getApp().getConf().getConfs("mdoc/out-builder")) {
            OutBuilderDef d = new ConfOutBuilderDef(getApp(), x);
            builders.add(d);
        }

        //
        for (Conf x : getApp().getConf().getConfs("mdoc/cm")) {
            CmDef d = new CmDef(getApp(), x);
            cms.add(d);
        }
    }

    public OutBuilder createBuilder(String name, Doc doc) throws Exception {
        OutBuilderDef d = builders.get(name);
        BaseOutBuilder builder = (BaseOutBuilder) d.createInst();
        builder.configure(doc);

        if (builder instanceof HtmlOutBuilder) {
            // особая инициализацяи для html

            // регистрация команд общих
            for (CmDef cm : this.cms) {
                builder.getBeanFactory().registerBean("cm-" + cm.getName(), cm.getCls());
            }

            // регистрация команд личных из gsp
            Collection<SourceFile> gspCms = doc.getSourceFiles().findFiles("_theme/cm/*.gsp");
            for (SourceFile f : gspCms) {
                String fn = UtFile.removeExt(UtFile.filename(f.getPath()));
                GspCmHandler cmh = builder.create(GspCmHandler.class);
                cmh.setGspTemplate(f.getPath());
                builder.getBeanFactory().registerBean("cm-" + fn, cmh);
            }

        }

        return builder;
    }

}
