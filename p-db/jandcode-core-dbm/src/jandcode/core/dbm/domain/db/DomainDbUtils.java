package jandcode.core.dbm.domain.db;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Утилитный класс для использования внутри генераторов ddl для базы данных
 */
public class DomainDbUtils implements IModelLink {

    protected Object forInst;
    protected Model model;
    private NamedList<Domain> domains;
    private NamedList<Domain> domainsMy;

    public DomainDbUtils(Object forInst) {
        this.forInst = forInst;
        if (forInst instanceof Model) {
            this.model = (Model) forInst;
        } else if (forInst instanceof IModelLink) {
            this.model = ((IModelLink) forInst).getModel();
        } else {
            throw new XError("Агрумент forInst должен быть моделью или IModelLink");
        }
    }

    public Model getModel() {
        return model;
    }

    /**
     * Домены для базы данных. Все.
     */
    public NamedList<Domain> getDomains() {
        if (this.domains == null) {
            this.domains = grabDomains();
        }
        return this.domains;
    }

    protected NamedList<Domain> grabDomains() {
        NamedList<Domain> res = new DefaultNamedList<>();
        for (Domain d : getModel().bean(DomainService.class).getDomains()) {
            if (d.hasTagDb()) {
                res.add(d);
            }
        }
        res.sort();
        return res;
    }

    /**
     * Собственные домены для базы данных. Только те, для которых
     * стоит генерировать ddl.
     */
    public NamedList<Domain> getDomainsMy() {
        if (domainsMy == null) {
            domainsMy = grabDomainsMy();
        }
        return domainsMy;
    }

    protected NamedList<Domain> grabDomainsMy() {
        NamedList<Domain> res = new DefaultNamedList<>();
        for (Domain d : getDomains()) {
            DomainDb dd = d.bean(DomainDb.class);
            if (dd.isDbTable() && !dd.isExternal()) {
                res.add(d);
            }
        }
        return res;
    }

    /**
     * Возвращает домен, на который ссылается поле в базе данных.
     *
     * @param f какое поле
     * @return null, если нет домена, на который ссылается. Например, это домен не в базе
     */
    public Domain getDbRefDomain(Field f) {
        if (f.isCalc() || !f.hasRef()) {
            return null;
        }
        Domain refDomain = getModel().bean(DomainService.class).findDomain(f.getRef());
        if (refDomain == null) {
            return null;
        }
        if (refDomain.findField("id") == null) {
            return null; // нет id в домене, куда ссылаемся
        }
        DomainDb refDbDomain = refDomain.bean(DomainDb.class);
        if (!refDbDomain.isDbTable()) {
            return null; // ссылка на не db-домен
        }
        return refDomain;
    }

    /**
     * Максимальная длина идентификатора в базе данных
     */
    public int getIdnMaxLength() {
        return getModel().getConf().getInt("cfg/db-params/idn.maxlength", 25);
    }

    /**
     * Делает короткий идентификатор длиной maxLength
     *
     * @param src       исходный
     * @param maxLength длина
     * @return часть исходного и crc, если src длиной более чем maxLength
     */
    public String makeShortIdn(String src, int maxLength) {
        if (src.length() <= maxLength) {
            return src;
        }
        String crc = UtString.md5Str(src).substring(24);
        String s = src.substring(0, maxLength - crc.length() - 1);
        return s + "_" + crc;
    }

    /**
     * Делает короткий идентификатор длиной maxLength
     *
     * @param src исходный
     * @return часть исходного и crc, если src длиной более чем допустимо для базы данных
     */
    public String makeShortIdn(String src) {
        return makeShortIdn(src, getIdnMaxLength());
    }

}
