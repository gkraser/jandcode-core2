package jandcode.groovy.impl;

import jandcode.commons.*;
import jandcode.commons.event.*;
import jandcode.commons.groovy.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.groovy.*;

import java.util.*;
import java.util.concurrent.*;

public class GroovyServiceImpl extends BaseComp implements GroovyService {

    private Map<String, GroovyCompiler> compilers = new ConcurrentHashMap<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // тушим все экземпляры компиляторов
        getApp().onEvent(App.Event_AppShutdown.class, this::onAppShutdown);
    }

    public GroovyCompiler getGroovyCompiler(String name) {
        GroovyCompiler res = compilers.get(name);
        if (res == null) {
            synchronized (this) {
                res = compilers.get(name);
                if (res == null) {
                    res = UtGroovy.createCompiler();
                    compilers.put(name, res);
                }
            }
        }
        return res;
    }

    public List<String> getGroovyCompilerNames() {
        List<String> res = new ArrayList<>();
        res.addAll(compilers.keySet());
        return res;
    }

    /**
     * Потушить все экземпляры компиляторов,что бы не загрязняться ими
     * при частой перезагрузки в рещиме разработки.
     */
    protected void onAppShutdown(Event e) throws Exception {
        for (Map.Entry<String, GroovyCompiler> entry : compilers.entrySet()) {
            UtGroovy.destroyCompiler(entry.getValue());
        }
        compilers = new HashMap<>();
    }

    public void checkChangedResource(CheckChangedResourceInfo info) throws Exception {
        for (Map.Entry<String, GroovyCompiler> entry : compilers.entrySet()) {
            entry.getValue().checkChangedResource();
        }
    }

}
