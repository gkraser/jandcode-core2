package jandcode.commons.cli.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;

import java.util.*;

public class CliDefImpl implements CliDef {

    private Map<String, CliOpt> opts = new LinkedHashMap<>();
    private String cmdName;
    private String desc;
    private String footer;

    public Collection<CliOpt> getOpts() {
        return opts.values();
    }

    public String getCmdName() {
        return cmdName;
    }

    public String getDesc() {
        return desc;
    }

    public String getFooter() {
        return footer;
    }

    ////// builder

    public CliDef desc(String v) {
        this.desc = v;
        return this;
    }

    public CliDef cmdName(String v) {
        this.cmdName = v;
        return this;
    }

    public CliOpt opt(String key) {
        CliOpt cur = this.opts.get(key);
        if (cur == null) {
            cur = new CliOptImpl(key);
            this.opts.put(key, cur);
        }
        return cur;
    }

    public void removeOpt(String key) {
        this.opts.remove(key);
    }

    public CliDef footer(String v) {
        this.footer = v;
        return this;
    }

    //////

    public void joinFrom(CliDef cliDef) {
        if (UtString.empty(getFooter())) {
            footer(cliDef.getFooter());
        }
        if (UtString.empty(getDesc())) {
            desc(cliDef.getDesc());
        }
        if (UtString.empty(getCmdName())) {
            cmdName(cliDef.getCmdName());
        }
        for (var opt : cliDef.getOpts()) {
            CliOpt newOpt = ((CliOptImpl) opt).cloneInst();
            this.opts.put(newOpt.getKey(), newOpt);
        }
    }
}
