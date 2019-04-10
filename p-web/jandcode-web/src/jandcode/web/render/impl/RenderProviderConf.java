package jandcode.web.render.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.web.render.*;

import java.util.*;

/**
 * Поставщик render из описаний в conf
 */
public class RenderProviderConf extends BaseComp implements IRenderProvider {

    public List<RenderDef> loadRenders() throws Exception {
        List<RenderDef> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("web/render");
        for (Conf x : lst) {
            RenderDefConf a = new RenderDefConf(getApp(), x);
            res.add(a);
        }
        return res;
    }
}
