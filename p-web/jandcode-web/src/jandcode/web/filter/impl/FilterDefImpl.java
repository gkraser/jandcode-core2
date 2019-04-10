package jandcode.web.filter.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.web.filter.*;

import java.util.*;

public class FilterDefImpl extends BaseComp implements FilterDef {

    private Set<FilterType> types = new LinkedHashSet<>();
    private IFilter inst;
    private boolean enabled = true;

    public FilterDefImpl(App app, Conf conf) {
        setApp(app);
        setName(conf.getName());
        UtReflect.getUtils().setProps(this, conf);
        inst = (IFilter) getApp().create(conf);
        if (types.size() == 0) {
            throw new XError("Не указан атрибут type для фильтра {0}", getName());
        }
    }

    public IFilter getInst() {
        return inst;
    }

    public Set<FilterType> getTypes() {
        return types;
    }

    /**
     * Установить типы фильтров. Можно указаывать несколько значений через ',' или
     * '*' для всех типов фильтров
     */
    public void setType(String typesName) {
        if (UtString.empty(typesName)) {
            return;
        }
        String[] a = typesName.split(",");
        for (String b : a) {
            try {
                if (b.equals("*")) {
                    for (FilterType ft : FilterType.values()) {
                        types.add(ft);
                    }
                } else {
                    FilterType ft = FilterType.valueOf(b);
                    types.add(ft);
                }
            } catch (IllegalArgumentException e) {
                throw new XError("Не правильное значение [{0}] для типа фильтра [{1}]", b, getName());
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
