package jandcode.core.web.action.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.web.action.*;

import java.util.*;

/**
 * Поставщик action из описаний в conf
 */
public class ActionProviderConf extends BaseComp implements IActionProvider {

    private String gspClassName;
    private Class gspCls;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.gspCls = UtClass.getClass(getGspClassName());
    }

    /**
     * Имя класса gsp, котороый будет использоватся,
     * если указан template и не указан класс.
     */
    public String getGspClassName() {
        return gspClassName;
    }

    public void setGspClassName(String gspClassName) {
        this.gspClassName = gspClassName;
    }

    public List<ActionDef> loadActions() throws Exception {
        List<ActionDef> res = new ArrayList<>();
        Collection<Conf> lst = getApp().getConf().getConfs("web/action");
        for (Conf x : lst) {
            Class cls = null;
            String clsName = x.getString("class");
            if (UtString.empty(clsName)) {
                String template = x.getString("template");
                if (!UtString.empty(template)) {
                    cls = this.gspCls;
                }
            }
            ActionDefConf a = new ActionDefConf(getApp(), x, UtConf.getNameAsPath(x), cls);
            res.add(a);
        }
        return res;
    }

}
