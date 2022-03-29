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
    }

    /**
     * Возвращает информацию о поле-ссылке в контексте текущего набора доменов.
     * Если поле не является ссылкой, возвращается null.
     *
     * @param f для какого поля
     * @return null, если поле не является ссылкой
     */
    public FieldRefInfo getRefInfo(Field f) {
        if (!f.hasRef()) {
            return null;
        }
        return new FieldRefInfo(f, this);
    }

}
