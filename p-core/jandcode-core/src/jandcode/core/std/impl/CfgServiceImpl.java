package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.std.*;

import java.util.*;

public class CfgServiceImpl extends BaseComp implements CfgService {

    private Conf origConf;
    private Conf conf;
    private List<ExpandRuleDef> expandRules = new ArrayList<>();

    class ExpandRuleDef {
        Conf conf;
        String mask;
        boolean ignore = false;

        public ExpandRuleDef(Conf conf) {
            this.conf = conf;
            this.mask = conf.getString("mask");
            this.ignore = conf.getBoolean("ignore", false);
        }

        boolean matchPath(String path) {
            return UtVDir.matchPath(mask, path);
        }

        boolean isIgnore() {
            return ignore;
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // правила раскрытия
        for (Conf x : cfg.getConf().getConfs("expand-rule")) {
            expandRules.add(0, new ExpandRuleDef(x));
        }

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
        expandProps(res, res, "");
        return res;
    }

    protected void expandProps(Conf root, Conf x, String prefix) {
        for (String pn : x.keySet()) {
            Object v = x.getValue(pn);
            String path = prefix + pn;
            if (v instanceof Conf) {
                expandProps(root, (Conf) v, path + "/");
            } else {
                if (rule_isIgnore(path)) {
                    // ничего не делаем, остаются переменные ${} как есть
                } else {
                    x.setValue(pn, expandPropValue(root, UtCnv.toString(v)));
                }
            }
        }
    }

    protected String expandPropValue(Conf root, String value) {
        if (!value.contains("${")) {
            return value;
        }
        return UtString.substVar(value, v -> expandPropValue(root, root.getString(v)));
    }

    //////

    protected boolean rule_isIgnore(String path) {
        if (this.expandRules.size() == 0) {
            return false;
        }
        for (ExpandRuleDef r : this.expandRules) {
            if (r.matchPath(path)) {
                return r.isIgnore();
            }
        }
        return false;
    }

}
