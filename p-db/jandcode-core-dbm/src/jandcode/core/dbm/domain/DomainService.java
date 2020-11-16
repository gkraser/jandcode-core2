package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Сервис доменов
 */
public interface DomainService extends Comp, IModelMember, IDomainService {

    /**
     * Создать построитель динамического домена.
     *
     * @param parentDomain предок домена
     */
    DomainBuilder createDomainBuilder(String parentDomain);

    /**
     * Создать экземпляр домена по conf.
     *
     * @param x    конфигурация домена, не раскрытая, будет раскрыта перед созданием
     * @param name имя создаваемого домена
     */
    Domain createDomain(Conf x, String name);


}
