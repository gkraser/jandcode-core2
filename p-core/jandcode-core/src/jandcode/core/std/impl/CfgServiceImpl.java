package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.std.*;

public class CfgServiceImpl extends BaseComp implements CfgService {

    private Conf origConf;
    private Conf conf;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // оригинальная, копия
        this.origConf = UtConf.create();
        this.origConf.join(getApp().getConf().getConf("cfg"));

    }

    public Conf getConf() {
        if (conf == null) {
            synchronized (this) {
                if (conf == null) {
                    Conf tmp = buildConf();
                    conf = tmp;
                }
            }
        }
        return conf;
    }

    public Conf getOrigConf() {
        return origConf;
    }

    //////

    protected Conf buildConf() {
        Conf res = UtConf.create();
        res.join(this.origConf);
        expandProps(res, res);
        return res;
    }

    protected void expandProps(Conf root, Conf x) {
        for (String pn : x.keySet()) {
            Object v = x.getValue(pn);
            if (v instanceof Conf) {
                expandProps(root, (Conf) v);
            } else {
                x.setValue(pn, expandPropValue(root, UtCnv.toString(v)));
            }
        }
    }

    protected String expandPropValue(Conf root, String value) {
        if (!value.contains("${")) {
            return value;
        }
        return UtString.substVar(value, v -> expandPropValue(root, root.getString(v)));
    }

}
