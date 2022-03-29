package jandcode.core.dbm.doc;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dbstruct.*;

import java.util.*;

/**
 * Утилиты для диаграм в рамках модели
 */
public class DiagramDbUtils implements IModelLink {

    protected Model model;
    protected DomainDbUtils domainDbUils;

    public DiagramDbUtils(Model model) {
        this.model = model;
        this.domainDbUils = new DomainDbUtils(this.model);
    }

    public Model getModel() {
        return model;
    }

    /**
     * Создает пустую диаграмму
     */
    public DiagramDb createDiagram() {
        return new DiagramDb(getModel(), domainDbUils);
    }

    /**
     * Создать диаграмму по конфигурации
     *
     * @param conf конфигурация диаграммы
     */
    public DiagramDb createDiagram(Conf conf) {
        DiagramDb d = createDiagram();
        try {
            d.beanConfigure(new DefaultBeanConfig(conf));
        } catch (Exception e) {
            throw new XErrorMark(e, conf.origin().toString());
        }
        return d;
    }

    /**
     * Загружает все диаграммы из модели
     */
    public List<DiagramDb> loadDiagrams() {
        List<DiagramDb> res = new ArrayList<>();
        for (Conf conf : getModel().getConf().getConfs("diagram")) {
            DiagramDb d = createDiagram(conf);
            res.add(d);
        }
        return res;
    }

}
