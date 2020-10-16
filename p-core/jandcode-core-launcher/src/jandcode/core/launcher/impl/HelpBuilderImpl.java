package jandcode.core.launcher.impl;


import jandcode.core.launcher.*;

import java.util.*;

public class HelpBuilderImpl implements HelpBuilder, Help {

    private Map<String, OptImpl> opts = new LinkedHashMap<>();
    private String desc;

    ////// HelpBuilder

    public OptBuilder opt(String name) {
        OptImpl opt = opts.get(name);
        if (opt == null) {
            opt = new OptImpl();
            opt.setName(name);
            opts.put(name, opt);
        }
        return new OptBuilderImpl(opt);
    }

    public OptBuilder opt(String name, String help) {
        return opt(name).desc(help);
    }

    public OptBuilder opt(String name, String help, String argName) {
        return opt(name).desc(help).arg(argName);
    }

    public OptBuilder opt(String name, String help, String argName, Object defaultArgValue) {
        return opt(name).desc(help).arg(argName).defaultArgValue(defaultArgValue);
    }

    public OptBuilder opt(String name, String help, boolean hasArg) {
        return opt(name).desc(help).arg(hasArg);
    }

    public OptBuilder opt(String name, String help, boolean hasArg, Object defaultArgValue) {
        return opt(name).desc(help).arg(hasArg).defaultArgValue(defaultArgValue);
    }

    public void desc(String v) {
        this.desc = v;
    }

    ////// Help

    public Collection<Opt> getOpts() {
        return (Collection) opts.values();
    }

    public String getDesc() {
        return desc;
    }

}
