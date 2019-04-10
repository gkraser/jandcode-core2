package jandcode.web.filter.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.filter.*;

import java.util.*;

public class FilterServiceImpl extends BaseComp implements FilterService, IFilter {

    private NamedList<FilterDef> filters = new DefaultNamedList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        List<Conf> z;

        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/filter"));
        for (Conf x : z) {
            FilterDef q = new FilterDefImpl(getApp(), x);
            filters.add(q);
        }
    }

    public NamedList<FilterDef> getFilters() {
        return filters;
    }

    public void execFilter(FilterType type, Request request) throws Exception {
        for (FilterDef h : filters) {
            if (h.isEnabled() && h.getTypes().contains(type)) {
                h.getInst().execFilter(type, request);
            }
        }
    }
}
