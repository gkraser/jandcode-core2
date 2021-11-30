package jandcode.core.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;

public class BeanConfigImpl implements BeanConfig {

    protected Conf conf;
    protected String name;
    protected boolean hasConf;

    public BeanConfigImpl() {
    }

    public BeanConfigImpl(Conf conf, String name) {
        this.conf = conf;
        this.name = name;
        this.hasConf = this.conf != null;
    }

    public BeanConfigImpl(Conf conf) {
        this.conf = conf;
        this.hasConf = this.conf != null;
    }

    public String getName() {
        if (name == null) {
            return getConf().getName();
        }
        return name;
    }

    public boolean hasName() {
        if (!UtString.empty(name)) {
            return true;
        }
        if (conf == null) {
            return false;
        }
        if (conf.hasName()) {
            return true;
        }
        return false;
    }

    public Conf getConf() {
        if (conf == null) {
            conf = Conf.create();
        }
        return conf;
    }

    public boolean hasConf() {
        return hasConf;
    }

    public String getConfClassName() {
        if (conf == null) {
            return "";
        }
        return getConf().getString("class");
    }

}
