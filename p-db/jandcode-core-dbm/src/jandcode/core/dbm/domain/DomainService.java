package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.store.*;

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

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domain для какого домена
     */
    Store createStore(Domain domain);


}
