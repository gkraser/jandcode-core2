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
     * @param x конфигурация домена. Полная, раскрытая.
     */
    Domain createDomain(Conf x);

    /**
     * Создать поле для домена по conf.
     * Не включается в домен, просто создается экземпляр.
     *
     * @param x конфигурация поля. Полная, раскрытая.
     */
    @Deprecated
    Field createField(Conf x, Domain forDomain);

    /**
     * Найти поле.
     * В качестве имени можно указывать:
     * <ul>
     * <li>NAME - глобальное поле NAME (например string, int ...)</li>
     * <li>DOMAIN/NAME - поле NAME домена DOMAIN (например Abonent/addr)</li>
     * <li>DOMAIN/ref - поле ссылка на домен DOMAIN (например Abonent/ref)</li>
     * </ul>
     *
     * @param name имя
     * @return
     */
    Field findField(String name);

}
