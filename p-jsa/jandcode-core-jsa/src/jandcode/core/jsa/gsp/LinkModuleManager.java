package jandcode.core.jsa.gsp;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.jsmodule.impl.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;

import java.util.*;

public class LinkModuleManager extends BaseComp {

    private List<ModuleItem> moduleItems = new ArrayList<>();
    private ModuleJoiner joiner;

    class ModuleItem {
        String path;
        boolean used;

        public ModuleItem(String path) {
            this.path = path;
        }
    }

    public int addModules(String path) {
        this.moduleItems.add(new ModuleItem(path));
        return this.moduleItems.size() - 1;
    }

    public void outLink(Gsp g, int key) {
        ModuleItem mi = moduleItems.get(key);
        boolean first = joiner == null;
        if (joiner == null) {
            joiner = new ModuleJoiner(getApp().bean(JsModuleService.class));
        }

        if (first) {
            // первый вывод
            String mBoot = getApp().bean(JsaCfg.class).getLinkModuleBoot();

            joiner.addModule(mi.path);

            Collection<JsModule> inc = joiner.getTop();
            Collection<JsModule> exc = new ArrayList<>();

            if (!UtString.empty(mBoot)) {
                outLinkBoot(g, mBoot);
            }

            outLink(g, inc, exc);

        } else {

            ModuleJoiner tmpJoiner = new ModuleJoiner(getApp().bean(JsModuleService.class));
            tmpJoiner.addModule(mi.path);

            Set<JsModule> all = joiner.getAll();
            List<JsModule> inc = new ArrayList<>();
            for (JsModule m : tmpJoiner.getModules()) {
                if (!all.contains(m)) {
                    inc.add(m);
                }
            }

            Collection<JsModule> exc = joiner.getTop();
            outLink(g, inc, exc);

            joiner.addModule(mi.path);
        }

    }

    private void outLink(Gsp g, Collection<JsModule> inc, Collection<JsModule> exc) {
        if (inc.size() == 0) {
            return;
        }
        WebService webSvc = g.getApp().bean(WebService.class);
        Request request = webSvc.getRequest();
        Map<String, String> params = new LinkedHashMap<>();
        StringBuilder incStr = new StringBuilder();
        for (JsModule m : inc) {
            incStr.append(m.getId());
        }
        params.put("p", incStr.toString());
        if (exc.size() > 0) {
            StringBuilder excStr = new StringBuilder();
            for (JsModule m : exc) {
                excStr.append(m.getId());
            }
            params.put("e", excStr.toString());
        }
        String href = request.ref("jsa/m", params);
        g.out("<script src=\"");
        g.out(href);
        g.out("\"></script>");
        if (getApp().getEnv().isDev() || getApp().getEnv().isTest()) {
            g.out("\n<!-- include=");
            g.out(inc);
            g.out(" exclude=");
            g.out(exc);
            g.out(" -->\n\n");
        }
    }

    private void outLinkBoot(Gsp g, String mod) {
        WebService webSvc = g.getApp().bean(WebService.class);
        Request request = webSvc.getRequest();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("p", mod);
        String href = request.ref("jsa/s", params);
        g.out("<script src=\"");
        g.out(href);
        g.out("\"></script>\n");

        // критически важная информация
        g.out("<script>");
        Map<String, Object> initialCfg = new LinkedHashMap<>();
        initialCfg.put("baseUrl", ((BaseGsp) g).ref("/"));
        if (getApp().getEnv().isDev()) {
            initialCfg.put("envDev", true);
        }
        g.out("Jc.cfg=");
        g.out(UtJson.toJson(initialCfg));
        g.out(";");
        g.out("</script>\n");
        if (getApp().getEnv().isDev()) {
            g.out("\n");
        }
    }

}
