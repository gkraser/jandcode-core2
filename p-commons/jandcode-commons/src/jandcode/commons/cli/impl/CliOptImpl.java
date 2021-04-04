package jandcode.commons.cli.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;

import java.util.*;

public class CliOptImpl implements CliOpt, Cloneable {

    private String key;
    private List<String> names = new ArrayList<>();
    private String desc;
    private String arg;
    private String defaultValue;
    private boolean multi;
    private boolean required;

    public CliOptImpl(String key) {
        if (UtString.empty(key)) {
            throw new XError("opt key not set");
        }
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public List<String> getNames() {
        return names;
    }

    public String getDesc() {
        return desc;
    }

    public boolean hasArg() {
        return !UtString.empty(arg) || isPositional();
    }

    public String getArg() {
        if (UtString.empty(arg) && isPositional()) {
            return "ARG";
        }
        return arg;
    }

    public boolean hasDefaultValue() {
        return !UtString.empty(defaultValue);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMulti() {
        return multi;
    }

    public boolean isPositional() {
        return getNames().size() == 0;
    }

    public boolean isRequired() {
        return required;
    }

    ////// builder

    public CliOpt names(String v) {
        List<String> lst = UtCnv.toList(v);
        this.names.clear();
        this.names.addAll(lst);
        return this;
    }

    public CliOpt desc(String v) {
        this.desc = v;
        return this;
    }

    public CliOpt arg(String v) {
        this.arg = v;
        return this;
    }

    public CliOpt arg(boolean v) {
        if (v) {
            this.arg = "ARG";
        } else {
            this.arg = null;
        }
        return this;
    }

    public CliOpt defaultValue(Object v) {
        if (v == null) {
            this.defaultValue = null;
        } else {
            this.defaultValue = UtCnv.toString(v);
        }
        return this;
    }

    public CliOpt multi(boolean v) {
        this.multi = v;
        return this;
    }

    public CliOpt required(boolean v) {
        this.required = v;
        return this;
    }

    //////

    public CliOpt cloneInst() {
        try {
            CliOptImpl res = (CliOptImpl) this.clone();
            res.names = new ArrayList<>();
            res.names.addAll(this.names);
            return res;
        } catch (CloneNotSupportedException e) {
            throw new XErrorWrap(e);
        }
    }

}
