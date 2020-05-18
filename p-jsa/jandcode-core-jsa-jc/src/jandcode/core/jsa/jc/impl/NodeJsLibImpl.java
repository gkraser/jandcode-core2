package jandcode.core.jsa.jc.impl;


import jandcode.core.jsa.jc.*;

import java.util.*;

public class NodeJsLibImpl implements NodeJsLib {

    private String name;
    private String version;
    private boolean client;
    private List<String> includeClient = new ArrayList<>();
    private List<String> excludeClient = new ArrayList<>();
    private Map<String, String> moduleMapping = new LinkedHashMap<>();
    private List<String> excludeRequire = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public List<String> getIncludeClient() {
        return includeClient;
    }

    public List<String> getExcludeClient() {
        return excludeClient;
    }

    public Map<String, String> getModuleMapping() {
        return moduleMapping;
    }

    public List<String> getExcludeRequire() {
        return excludeRequire;
    }

    public String toString() {
        return "NodeJsLib{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", client=" + client +
                '}';
    }

}
