package jandcode.jsa.gsp;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.jsa.jsmodule.*;
import jandcode.jsa.jsmodule.impl.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.gsp.*;

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
        boolean first = joiner == null;
        if (joiner == null) {
            joiner = new ModuleJoiner(getApp().bean(JsModuleService.class));
        }

        ModuleItem mi = moduleItems.get(key);

        if (first) {
            // первый вывод
            Conf linkModConf = getApp().getConf().getConf("web/jsa-linkModule");
            String mBoot = linkModConf.getString("boot");
            String mCore = linkModConf.getString("core");
            String mBootInline = linkModConf.getString("boot-inline");

            if (!UtString.empty(mCore)) {
                joiner.addModule(mCore);
            }
            joiner.addModule(mi.path);

            Collection<JsModule> inc = joiner.getTop();
            Collection<JsModule> exc = new ArrayList<>();

            if (!UtString.empty(mBoot)) {
                outLinkBoot(g, mBoot);
            }

            outLink(g, inc, exc);

            if (!UtString.empty(mBootInline)) {
                outLinkBootInline(g, mBootInline);
            }
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
        if (getApp().isDebug() || getApp().isTest()) {
            g.out("\n<!-- include=");
            g.out(inc);
            g.out(" exclude=");
            g.out(exc);
            g.out(" -->\n");
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
    }

    private void outLinkBootInline(Gsp g, String mod) {
        JsModuleService svc = getApp().bean(JsModuleService.class);
        JsModule bim = svc.getModule(mod);
        g.out("<script>\n");
        g.out(bim.getText().trim());
        g.out("</script>\n");
    }

}
