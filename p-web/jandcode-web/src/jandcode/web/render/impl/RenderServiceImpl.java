package jandcode.web.render.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.render.*;

import java.util.*;

public class RenderServiceImpl extends BaseComp implements RenderService {

    private NamedList<RenderDef> renders = new DefaultNamedList<>();
    private List<IRenderProvider> renderProviders = new ArrayList<>();
    private List<IRenderFactory> renderFactorys = new ArrayList<>();


    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        //
        List<Conf> z;

        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/render-provider"));
        for (Conf x : z) {
            IRenderProvider q = (IRenderProvider) getApp().create(x);
            renderProviders.add(q);
        }

        //
        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/render-factory"));
        for (Conf x : z) {
            IRenderFactory q = (IRenderFactory) getApp().create(x);
            renderFactorys.add(q);
        }

        for (IRenderProvider p : renderProviders) {
            List<RenderDef> z1 = p.loadRenders();
            if (z1 != null) {
                renders.addAll(z1);
            }
        }
    }

    public NamedList<RenderDef> getRenders() {
        return renders;
    }

    public IRender createRender(Object data, Request request) {
        IRender a = null;
        for (int i = renderFactorys.size() - 1; i >= 0; i--) {
            IRenderFactory p = renderFactorys.get(i);
            a = p.createRender(data, request);
            if (a != null) {
                break;
            }
        }
        return a;
    }

    public IRender createRender(String name) {
        RenderDef rdef = getRenders().find(name);
        if (rdef == null) {
            throw new XError("Render {0} not found", name);
        }
        return rdef.createInst();
    }

}
