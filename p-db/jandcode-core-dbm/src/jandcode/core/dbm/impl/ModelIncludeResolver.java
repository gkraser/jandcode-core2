package jandcode.core.dbm.impl;

import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.*;

import java.text.*;
import java.util.*;

/**
 * Получение списка моделей, которые включены.
 * Анализируются теги include и строится список имен в правильном порядке.
 */
public class ModelIncludeResolver {

    protected HashSet<String> used = new HashSetNoCase();
    protected ModelService svc;

    public ModelIncludeResolver(ModelService svc) {
        this.svc = svc;
    }

    /**
     * Получение списка имен моделей, которые включены в указанную
     *
     * @param name что раскрывать
     * @return раскрытая conf
     */
    public List<String> resolveIncludeModel(String name) {
        List<String> res = new ArrayList<>();
        used.clear();

        if (DbmConsts.CORE_MODEL.equalsIgnoreCase(name)) {
            return res;
        }

        // сначала core
        res.add(DbmConsts.CORE_MODEL);
        used.add(DbmConsts.CORE_MODEL);

        // потом остальные
        add(res, name, false);

        return res;
    }

    protected void add(List<String> res, String name, boolean joinSelf) {
        if (used.contains(name)) {
            return;
        }
        used.add(name);

        try {
            Conf conf = resolveModelConf(name);
            for (Conf inc : conf.getConfs("include")) {
                add(res, inc.getName(), true);
            }
        } catch (Exception e) {
            throw new XErrorMark(e, MessageFormat.format("для модели {1}", name));
        }

        if (joinSelf) {
            res.add(name);
        }

    }

    protected Conf resolveModelConf(String name) {
        return svc.getModels().get(name).getConf();
    }

}
