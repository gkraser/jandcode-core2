package jandcode.mdoc.web.impl;

import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.web.*;

import java.util.*;

public class WebMDocServiceImpl extends BaseComp implements WebMDocService {

    private Map<String, OutBuilder> builders = new LinkedHashMap<>();
    private List<String> apps = new ArrayList<>();

    public void registerBuilder(String name, OutBuilder builder) {
        builders.put(name, builder);
    }

    public OutBuilder getBuilder(String name) {
        OutBuilder b = this.builders.get(name);
        if (b == null) {
            throw new XError("OutBuilder {0} not registred", name);
        }
        return b;
    }

    public void registerApp(String name) {
        this.apps.add(name);
    }

    public List<String> getRegisterApps() {
        return apps;
    }

}
