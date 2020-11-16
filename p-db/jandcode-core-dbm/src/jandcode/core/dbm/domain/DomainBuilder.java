package jandcode.core.dbm.domain;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * Построитель динамических доменов
 */
public interface DomainBuilder extends IConfLink {

    /**
     * conf формируемого домена
     */
    Conf getConf();

    /**
     * Создать экземпляр домена с указанным именем.
     * Вызывать после формирования структуры.
     * Каждый вызов - новый экземпляр домена с собственным экземпляром conf.
     */
    Domain createDomain(String name);

    /**
     * Создать экземпляр conf для домена с указанным именем.
     * Вызывать после формирования структуры.
     * Каждый вызов - новый экземпляр conf.
     */
    Conf createDomainRt(String name);

    /**
     * Создать экземпляры полей для указанного домена,
     * Каждый вызов - новые экземпляры полей с собственными экземплярами conf.
     *
     * @param forDomain для какого домена. Экземпляры не добавляются в домен,
     *                  просто создаются
     * @return список полей
     */
    List<Field> createFields(Domain forDomain);

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
