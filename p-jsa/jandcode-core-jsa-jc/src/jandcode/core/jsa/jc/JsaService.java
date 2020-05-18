package jandcode.core.jsa.jc;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.jsa.jc.impl.*;
import jandcode.jc.*;
import jandcode.jc.std.*;

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
     * Задачи gulp для модуля с учетом определений в модулях,
     * от которых модуль зависит.
     */
    public Map getGulpTasks(JsaModule m) {
        Map<String, Object> res = new LinkedHashMap<>();
        NamedList<JsaModule> deps = getJsaModules(m.getLib().getDepends().getAll().getLibsAll());
        for (JsaModule mi : deps) {
            for (String key : mi.getGulpTasks().keySet()) {
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
//        List<JsaModule> moduleInfos = getJsaModules(p);
//
//        Map<String, Object> deps = new LinkedHashMap<>();
//        for (int i = 0; i < moduleInfos.size(); i++) {
//            JsaModule mi = moduleInfos.get(i);
//            deps.putAll(mi.getNodeDepends());
//        }
//
//        return sortDependsMap(deps);

        NodeJsLibList libs = getNodeJsLibs(p);
        Map<String, Object> deps = new LinkedHashMap<>();
        for (NodeJsLib lib : libs) {
            deps.put(lib.getName(), lib.getVersion());
        }

        return sortDependsMap(deps);
    }

    /**
     * Получить список всех nodejs библиотек из всех модулей для проекта
     */
    public NodeJsLibList getNodeJsLibs(Project p) {
        NodeJsLibService svc = getCtx().service(NodeJsLibService.class);
        List<JsaModule> moduleInfos = getJsaModules(p);
        NodeJsLibList res = new NodeJsLibList();
        for (int i = 0; i < moduleInfos.size(); i++) {
            JsaModule mi = moduleInfos.get(i);
            List<String> deps = mi.getNodeJsDepends();
            for (String dep : deps) {
                NodeJsLib lib = svc.findLib(dep);
                if (lib == null) {
                    throw new XError("Не найдена nodejs зависимость [{0}], указанная в модуле [{1}]", dep, mi.getName());
                }
                if (res.find(lib.getName()) == null) {
                    res.add(lib);
                }
            }
        }
        res.sort();
        return res;
    }

    /**
     * Получить список всех клиентских nodejs библиотек из всех модулей для проекта
     */
    public NodeJsLibList getNodeJsLibs(Project p, boolean client) {
        NodeJsLibList res = new NodeJsLibList();
        NodeJsLibList tmp = getNodeJsLibs(p);

        for (NodeJsLib z : tmp) {
            if (z.isClient() == client) {
                res.add(z);
            }
        }

        return res;
    }

}
