package jandcode.core.dbm.doc;

import jandcode.commons.*;
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
    private NamedList<DomainInfo> domainInfos = new DefaultNamedList<>();

    /**
     * Информация о домене в диаграмме
     */
    public static class DomainInfo implements INamed {
        private Conf conf;
        private Domain domain;
        private boolean showfields = true;
        private boolean showrefs = true;

        public DomainInfo(Conf conf, Domain domain) {
            this.conf = conf;
            this.domain = domain;
            UtReflect.getUtils().setProps(this, conf);
        }

        public String getName() {
            return getDomain().getDbTableName();
        }

        public Conf getConf() {
            return conf;
        }

        /**
         * Домен
         */
        public Domain getDomain() {
            return domain;
        }

        /**
         * Показывать ли поля для этого домена
         */
        public boolean isShowfields() {
            return showfields;
        }

        public void setShowfields(boolean showfields) {
            this.showfields = showfields;
        }

        /**
         * @return
         */
        public boolean isShowrefs() {
            return showrefs;
        }

        public void setShowrefs(boolean showrefs) {
            this.showrefs = showrefs;
        }
    }

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
            this.domainInfos.add(new DomainInfo(conf, domain));
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
     * Список оберток доменов с атрибутами для этой диаграммы
     */
    public NamedList<DomainInfo> getDomainInfos() {
        return domainInfos;
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
