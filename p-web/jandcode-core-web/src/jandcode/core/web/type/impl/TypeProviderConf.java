package jandcode.core.web.type.impl;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.type.*;

import java.util.*;

/**
 * Поставщик type из описаний в conf
 */
public class TypeProviderConf extends BaseComp implements ITypeProvider {

    public List<TypeDef> loadTypes() throws Exception {
        List<TypeDef> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("web/type");
        for (Conf x : lst) {
            TypeDefConf a = getApp().create(x, TypeDefConf.class);
            res.add(a);
        }
        return res;
    }

}
