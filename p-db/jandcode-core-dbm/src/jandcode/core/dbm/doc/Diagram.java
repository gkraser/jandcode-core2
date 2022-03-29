package jandcode.core.dbm.doc;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Данные для диаграммы доменов.
 * Утилитные методы для сборки набора доменов и предоставление методов для
 * генерации диаграм связей.
 */
public class Diagram extends BaseModelMember implements IConfLink {

    private String title;
    private DomainGroup domainGroup;
    private Conf conf;

    /**
     * Создать экземпляр
     *
     * @param parentDomainGroup общий список доменов, из которых будут выбиратся
     *                          конкретные для этой диаграммы
     */
    public Diagram(DomainGroup parentDomainGroup) {
        setModel(parentDomainGroup.getModel());
        this.conf = Conf.create();
        this.domainGroup = new DomainGroup(parentDomainGroup);
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
        //
        for (Conf conf : this.conf.getConfs("domain")) {
            String domainName = conf.getName();
            Domain domain = this.domainGroup.getParentDomains().get(domainName);
            this.domainGroup.getDomains().add(domain);
        }
    }

    public Conf getConf() {
        return conf;
    }

    /**
     * Домены, которые входят в эту диаграмму
     */
    public DomainGroup getDomainGroup() {
        return domainGroup;
    }

    /**
     * Список доменов на диаграмме
     */
    public NamedList<Domain> getDomains() {
        return getDomainGroup().getDomains();
    }

    /**
     * Заголовок диаграммы
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
