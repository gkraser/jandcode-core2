package jandcode.web.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.groovy.*;
import jandcode.web.gsp.*;
import org.apache.commons.vfs2.*;

import java.util.*;
import java.util.concurrent.*;

public class GspServiceImpl extends BaseComp implements GspService {

    private NamedList<GspDef> gsps = new DefaultNamedList<>();
    private Map<String, GspDef> gspsRuntime = new ConcurrentHashMap<>();
    private List<IGspProvider> gspProviders = new ArrayList<>();
    private GspContextFactory gspContextFactory;

    //////


    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        List<Conf> z;

        // фабрика контекстов gsp
        Conf x1 = getApp().getConf().getConf("web/gspContext/default");
        gspContextFactory = getApp().create(x1, GspContextFactory.class);

        // регистрация gsp
        z = UtConf.sortByWeight(getApp().getConf().getConfs("web/gsp-provider"));
        for (Conf x : z) {
            IGspProvider q = (IGspProvider) getApp().create(x);
            gspProviders.add(q);
            List<GspDef> lst = q.loadGsps();
            gsps.addAll(lst);
        }

    }

    //////

    public NamedList<GspDef> getGsps() {
        return gsps;
    }

    public String getGspPath(String gspName) {
        try {
            GspDef g = resolveGsp(gspName);
            return g.getGspPath();
        } catch (Exception e) {
            return "";
        }
    }

    public String getGspSourceText(String gspName) {
        try {
            GspDef g = resolveGsp(gspName);
            return g.getGspSourceText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getGspClassText(String gspName) {
        try {
            GspDef g = resolveGsp(gspName);
            return g.getGspClassText();
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public Gsp createGsp(String gspName) throws Exception {
        GspDef g = resolveGsp(gspName);
        if (g == null) {
            throw new XError("Not found gsp [{0}]", gspName);
        }
        return g.createInst();
    }

    protected GspDef resolveGsp(String gspName) throws Exception {
        // сначала ищем
        GspDef g = findGsp(gspName);
        if (g != null) {
            return g;
        }

        // не нашли, создаем
        for (int i = gspProviders.size() - 1; i >= 0; i--) {
            IGspProvider p = gspProviders.get(i);
            g = p.resolveGsp(gspName);
            if (g != null) {
                break;
            }
        }

        // не смогли создать - ошибка
        if (g == null) {
            throw new XError("Gsp [{0}] not found", gspName);
        }

        // создали, ищем еще раз, вдруг имя было не нормализованное
        GspDef g1 = findGsp(g.getName());
        if (g1 != null) {
            return g1;
        }

        // не нашли, регистрируем
        putGsp(g);
        return g;
    }

    protected GspDef findGsp(String gspName) {
        GspDef g = gsps.find(gspName);
        if (g == null) {
            g = gspsRuntime.get(gspName);
        }
        return g;
    }

    protected void putGsp(GspDef gsp) {
        gspsRuntime.put(gsp.getName(), gsp);
    }

    //////

    public GroovyCompiler getGroovyCompiler() {
        return getApp().bean(GroovyService.class).getGroovyCompiler(GspService.class.getName());
    }

    /**
     * Компилировать gsp-файл
     */
    public GroovyClazz compileGsp(FileObject gspFile) throws Exception {
        GroovyCompiler gc = getGroovyCompiler();
        return gc.getClazz(BaseGsp.class, "protected void onRender()", gspFile, true);
    }

    public GspContext createGspContext() {
        return gspContextFactory.createGspContext();
    }

}
