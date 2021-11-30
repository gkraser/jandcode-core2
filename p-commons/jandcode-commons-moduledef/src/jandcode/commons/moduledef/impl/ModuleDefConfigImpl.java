package jandcode.commons.moduledef.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.moduledef.*;

import java.util.*;

public class ModuleDefConfigImpl implements ModuleDefConfig {

    private Conf conf = Conf.create();
    private List<String> files = new ArrayList<>();
    private List<String> depends = new ArrayList<>();
    private Map<String, String> confVars = new LinkedHashMap<>();

    public Conf getConf() {
        return conf;
    }

    public List<String> getFiles() {
        return files;
    }

    public List<String> getDepends() {
        return depends;
    }

    public Map<String, String> getConfVars() {
        return confVars;
    }
}
