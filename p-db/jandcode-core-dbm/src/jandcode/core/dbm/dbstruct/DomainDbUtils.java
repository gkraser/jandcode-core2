package jandcode.core.dbm.dbstruct;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Утилитный класс для использования внутри генераторов ddl для базы данных
 */
public class DomainDbUtils implements IModelLink {

    protected Model model;
    private NamedList<Domain> dbTables;
    private DomainService domainSvc;

    public DomainDbUtils(Model model) {
        this.model = model;
        this.domainSvc = model.bean(DomainService.class);
    }

    public Model getModel() {
        return model;
    }

    /**
     * Домены, которые являются таблицами в базе данных.
     * Для них нужно генерировать структуру базы данных.
     */
    public NamedList<Domain> getDbTables() {
        prepare();
        return this.dbTables;
    }

    private void prepare() {
        if (this.dbTables != null) {
            return;
        }
        //
        NamedList<Domain> dbTables = new DefaultNamedList<>();
        NamedList<Domain> allDomains = this.domainSvc.getDomains();
        for (Domain d : allDomains) {
            DomainDb dd = d.bean(DomainDb.class);
            if (dd.isDbTable() && !dd.isDbExternal()) {
                dbTables.add(d);
            }
        }
        dbTables.sort();
        //
        this.dbTables = dbTables;
    }

    /**
     * Возвращает домен, на который ссылается поле в базе данных.
     *
     * @param f какое поле
     * @return null, если нет домена, на который ссылается.
     * Возможно такого домена нет или он не является частью структуры базы данных.
     */
    public Domain getDbRefDomain(Field f) {
        if (!f.hasRef()) {
            return null;
        }
        Domain refDomain = this.domainSvc.findDomain(f.getRef());
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
