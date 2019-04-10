package jandcode.web.action.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.web.action.*;

import java.util.*;

/**
 * Поставщик action из описаний в conf
 */
public class ActionProviderConf extends BaseComp implements IActionProvider {

    public List<ActionDef> loadActions() throws Exception {
        List<ActionDef> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("web/action");
        for (Conf x : lst) {
            ActionDefConf a = new ActionDefConf(getApp(), x, UtConf.getNameAsPath(x));
            res.add(a);
        }
        return res;
    }

}
