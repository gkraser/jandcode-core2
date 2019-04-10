package jandcode.web.type.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.web.type.*;

import java.util.*;

public class TypeServiceImpl extends BaseComp implements TypeService {

    private NamedList<TypeDef> types = new DefaultNamedList<>();
    private List<ITypeProvider> typeProviders = new ArrayList<>();
    private ClassLinks<TypeDef> typesByClass = new ClassLinks<>();


    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        List<Conf> z;

        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/type-provider"));
        for (Conf x : z) {
            ITypeProvider q = (ITypeProvider) getApp().create(x);
            typeProviders.add(q);
        }

        //
        for (ITypeProvider p : typeProviders) {
            List<TypeDef> z1 = p.loadTypes();
            if (z1 != null) {
                types.addAll(z1);
            }
        }

        // регистрируем по классам
        for (TypeDef d : types) {
            typesByClass.add(d.getCls(), d);
        }

    }

    public NamedList<TypeDef> getTypes() {
        return types;
    }

    public TypeDef findType(Class cls) {
        return typesByClass.get(cls);
    }

}
