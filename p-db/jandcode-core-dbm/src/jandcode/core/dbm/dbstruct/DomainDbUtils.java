package jandcode.core.dbm.dbstruct;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;

/**
 * Утилитный класс для использования внутри генераторов ddl и документации для базы данных.
 */
public class DomainDbUtils implements IModelLink {

    protected Model model;
    protected DomainGroup domainGroup;

    public DomainDbUtils(Model model) {
        this.model = model;
        this.domainGroup = new DomainGroup(model);
        this.domainGroup.grabDbTables();
    }

    public Model getModel() {
        return model;
    }

    /**
     * Группа доменов, с которыми нужно работать. Это таблицы в базе данных.
     */
    public DomainGroup getDomainGroup() {
        return domainGroup;
    }

    /**
     * Список доменов, с которыми нужно работать
     */
    public NamedList<Domain> getDomains() {
        return getDomainGroup().getDomains();
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
