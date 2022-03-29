package jandcode.core.dbm.doc;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dbstruct.*;
import jandcode.core.dbm.domain.*;

/**
 * Диаграмма для доменов в базе данных.
 * Утилитные методы для сборки набора доменов и предоставление методов для
 * генерации диаграм связей.
 */
public class DiagramDb extends BaseModelMember implements IConfLink {

    private String title;
    private NamedList<Domain> domains = new DefaultNamedList<>();
    protected DomainDbUtils domainDbUils;
    private Conf conf;

    public DiagramDb(Model model, DomainDbUtils domainDbUils) {
        setModel(model);
        this.domainDbUils = domainDbUils;
        this.conf = Conf.create();
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
        //
//        NamedList<Domain> dbTables = domainDbUils.getDbTables();
//        for (Conf conf : this.conf.getConfs("domain")) {
//            String domainName = conf.getName();
//            Domain domain = dbTables.get(domainName);
//            this.domains.add(domain);
//        }
    }

    public Conf getConf() {
        return conf;
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

    /**
     * Список доменов на диаграмме
     */
    public NamedList<Domain> getDomains() {
        return domains;
    }

}
