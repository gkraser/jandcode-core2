package jandcode.jsa.jc;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.jc.*;
import jandcode.jc.std.*;
import jandcode.jsa.jc.impl.*;

import java.util.*;

/**
 * Сервис для поддержки jsa
 */
public class JsaService extends CtxService {

    private NamedList<JsaModuleImpl> cache = new DefaultNamedList<>();
    private Map<String, NamedList<JsaModule>> cacheByProject = new HashMap<>();

    /**
     * Возвращает jsa-модуль из lib.
     *
     * @param lib lib
     * @return null, если Lib не является jsa-модулем
     */
    public JsaModule getJsaModule(Lib lib) {
        JsaModuleImpl m = this.cache.find(lib.getName());
        if (m == null) {
            m = new JsaModuleImpl(getCtx(), lib);
            this.cache.add(m);
        }
        if (m.isJsaModule()) {
            return m;
        }
        return null;
    }

    /**
     * Возвращает список jsa-модулей из списка lib.
     *
     * @return список, в котором только JsaModule. Отсортирован по depends: сначала
     * идут зависимые, потом зависящие.
     */
    public NamedList<JsaModule> getJsaModules(List<Lib> libs) {
        ListLib tmp = new ListLib();
        tmp.addAll(libs);
        tmp.sortByDepends();
        //
        NamedList<JsaModule> res = new DefaultNamedList<>();
        for (Lib lib : tmp) {
            JsaModule m = getJsaModule(lib);
            if (m != null) {
                res.add(m);
            }
        }
        return res;
    }

    /**
     * Возвращает список jsa-модулей для проекта.
     *
     * @return список , в котором только JsaModule. Отсортирован по depends: сначала
     * идут зависимые, потом зависящие.
     */
    public NamedList<JsaModule> getJsaModules(Project p) {
        NamedList<JsaModule> res = cacheByProject.get(p.getProjectFile());
        if (res == null) {
            LibDepends deps = p.create(LibDependsUtils.class).getDepends(p);
            res = getJsaModules(deps.getAll().getLibsAll());
            cacheByProject.put(p.getProjectFile(), res);
        }
        NamedList<JsaModule> tmp = new DefaultNamedList<>();
        tmp.addAll(res);
        return tmp;
    }

    /**
     * Задачи gulpи для модуля с учетом определений в модулях,
     * от которых модуль зависит.
     */
    public Map getGulpTasks(JsaModule m) {
        Map res = new LinkedHashMap();
        NamedList<JsaModule> deps = getJsaModules(m.getLib().getDepends().getAll().getLibsAll());
        for (JsaModule mi : deps) {
            for (Object key : mi.getGulpTasks().keySet()) {
                Object value = mi.getGulpTasks().get(key);
                if (value instanceof Map) {
                    Map mvalue = (Map) value;
                    if (UtCnv.toBoolean(mvalue.get("provide"))) {
                        res.put(key, value);
                    }
                }
            }
        }
        res.putAll(m.getGulpTasks());
        return res;
    }

    public Map<String, String> sortDependsMap(Map<String, Object> deps) {
        Map<String, String> res = new LinkedHashMap<>();
        if (deps == null) {
            return res;
        }
        List<String> tmp = new ArrayList<>(deps.keySet());
        Collections.sort(tmp);
        for (String s : tmp) {
            res.put(s, UtCnv.toString(deps.get(s)));
        }
        return res;
    }

    /**
     * Отсортированный список nodeDepends из всех модулей для проекта.
     */
    public Map<String, String> getNodeDepends(Project p) {
        List<JsaModule> moduleInfos = getJsaModules(p);

        Map<String, Object> deps = new LinkedHashMap<>();
        for (int i = 0; i < moduleInfos.size(); i++) {
            JsaModule mi = moduleInfos.get(i);
            deps.putAll(mi.getNodeDepends());
        }

        return sortDependsMap(deps);
    }
}
