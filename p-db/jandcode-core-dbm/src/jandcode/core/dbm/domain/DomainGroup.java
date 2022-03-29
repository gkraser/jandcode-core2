package jandcode.core.dbm.domain;

import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dbstruct.*;

/**
 * Группа доменов.
 * Является подгруппой набора доменов из родительского хранилища.
 * По умолчанию родительским хранилищем является сервис {@link DomainService}
 */
public class DomainGroup implements IModelLink, IDomainHolder {

    private NamedList<Domain> domains = new DefaultNamedList<>();
    protected Model model;
    protected IDomainHolder parentHolder;

    public DomainGroup(Model model) {
        this.model = model;
        this.parentHolder = model.bean(DomainService.class);
    }

    public DomainGroup(Model model, IDomainHolder parentHolder) {
        this.model = model;
        this.parentHolder = parentHolder;
    }

    public DomainGroup(DomainGroup parentHolder) {
        this.model = parentHolder.getModel();
        this.parentHolder = parentHolder;
    }

    public Model getModel() {
        return model;
    }
    
    /**
     * Все домены из модели
     */
    public NamedList<Domain> getAllDomains() {
        return model.bean(DomainService.class).getDomains();
    }

    /**
     * Все домены, на основе которых построена эта группа
     */
    public NamedList<Domain> getParentDomains() {
        return parentHolder.getDomains();
    }

    /**
     * Домены в группе
     */
    public NamedList<Domain> getDomains() {
        return domains;
    }

    /**
     * Заполняет хранилище доменами, которые являются таблицами в структуре базы данных
     */
    public void grabDbTables() {
        for (Domain d : getParentDomains()) {
            DomainDb dd = d.bean(DomainDb.class);
            if (dd.isDbTable() && !dd.isDbExternal()) {
                getDomains().add(d);
            }
        }
        getDomains().sort();
    }

    /**
     * Возвращает информацию о поле-ссылке в контексте текущего набора доменов.
     * Если поле не является ссылкой, возвращается null.
     *
     * @param f     для какого поля
     * @param force false - если поле является ссылкой, но домен, на который ссылается
     *              отсутсвует в группе, возвращает null.
     *              true - возвращет информацию о ссыле, даже если домен отсутвует в группе
     * @return null, если поле не является ссылкой или ссылка ведет в никуда в группе (при
     * force=false)
     */
    public FieldRefInfo getRefInfo(Field f, boolean force) {
        if (!f.hasRef()) {
            return null;
        }
        FieldRefInfo res = new FieldRefInfo(f, this);
        if (!force) {
            if (res.getRefDomain() == null) {
                return null;
            }
        }
        return res;
    }

    /**
     * force=false
     *
     * @see DomainGroup#getRefInfo(jandcode.core.dbm.domain.Field, boolean)
     */
    public FieldRefInfo getRefInfo(Field f) {
        return getRefInfo(f, false);
    }

}
