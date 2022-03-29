package jandcode.core.dbm.doc;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

/**
 * Утилиты для диаграм в рамках модели
 */
public class DiagramUtils implements IModelLink {

    protected Model model;
    protected DomainGroup domainGroup;

    public DiagramUtils(Model model) {
        this.model = model;
        this.domainGroup = new DomainGroup(model);
    }

    public DiagramUtils(DomainGroup domainGroup) {
        this(domainGroup.getModel());
        this.domainGroup.getDomains().addAll(domainGroup.getDomains());
    }

    public Model getModel() {
        return model;
    }

    /**
     * Все домены, которые потенциально могут быть использованы на диаграммах.
     */
    public DomainGroup getDomainGroup() {
        return domainGroup;
    }

    /**
     * Создает пустую диаграмму
     */
    public Diagram createDiagram() {
        return new Diagram(getDomainGroup());
    }

    /**
     * Создать диаграмму по конфигурации
     *
     * @param conf конфигурация диаграммы
     */
    public Diagram createDiagram(Conf conf) {
        Diagram d = createDiagram();
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
    public List<Diagram> loadDiagrams() {
        List<Diagram> res = new ArrayList<>();
        for (Conf conf : getModel().getConf().getConfs("diagram")) {
            Diagram d = createDiagram(conf);
            res.add(d);
        }
        return res;
    }

}
