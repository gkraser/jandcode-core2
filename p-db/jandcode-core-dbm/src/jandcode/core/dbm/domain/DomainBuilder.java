package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;

/**
 * Построитель динамических доменов
 */
public interface DomainBuilder extends IConfLink {

    /**
     * conf формируемого домена.
     * Можно править ручками.
     */
    Conf getConf();

    /**
     * Создать экземпляр домена с указанным именем.
     * Вызывать после формирования структуры.
     * Каждый вызов - новый экземпляр домена с собственным экземпляром conf.
     */
    Domain createDomain(String name);

    /**
     * Добавить поле
     *
     * @param name   имя поля
     * @param parent предок для поля. Можно указывать типы полей или поля глобальных
     *               доменов
     * @return conf с конфигурацией поля
     */
    Conf addField(String name, String parent);

}
