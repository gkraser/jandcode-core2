package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;

/**
 * Объект хранит структуры зарегистрированных доменов
 */
public class DomainConfHolder {

    private Conf root;
    private ConfExpander expander;
    private Conf fieldConf;
    private Conf domainConf;
    private Conf systemConf;
    private Conf fieldBaseConf;
    private Conf domainBaseConf;

    /**
     * Построить раскрытые конфигурации
     *
     * @param modelConf конфигурация модели
     */
    public void buildConf(Conf modelConf) {

        // это все что мы собрали для себя, обработанная копия
        this.root = Conf.create();

        // собираем все, что нам нужно из modelConf
        new DomainConfGrab().grab(modelConf, root);

        // обрабатываем собранное
        new DomainConfPrepare().prepareRoot(root);

        // создаем expander
        this.expander = UtConf.createExpander(root);

        // настраиваем expander
        this.expander.addRuleContainer("domain", "ref", "field");
        this.expander.addRuleContainer("domain", "field", "field");
        this.expander.addRuleNotInherited("tag.*");
        this.expander.addRuleNotInherited("abstract");
        this.expander.addRuleParent("field", (type, parent) -> {
            String[] ar = parent.split("/");
            if (ar.length == 2) {
                if (ar[1].equals("ref")) {
                    return "domain/" + ar[0] + "/ref/default";
                } else {
                    return "domain/" + ar[0] + "/field/" + ar[1];
                }
            } else if (ar.length == 4) {
                return parent;
            }
            return null;
        });

        // раскрываем
        this.fieldConf = this.expander.expand("field");
        this.domainConf = this.expander.expand("domain");

        // системные конфиги, тут например общие бины для полей и доменов
        this.systemConf = root.getConf("system");
        this.fieldBaseConf = this.systemConf.getConf("field-base");
        this.domainBaseConf = this.systemConf.getConf("domain-base");

    }

    /**
     * {@link ConfExpander}, который используется для раскрытия.
     */
    public ConfExpander getExpander() {
        return expander;
    }

    /**
     * Конфиги раскрытые всех полей
     */
    public Conf getFieldConf() {
        return fieldConf;
    }

    /**
     * Конфиги раскрытые всех доменов
     */
    public Conf getDomainConf() {
        return domainConf;
    }

    /**
     * Системные конфиги, например тут общие бины для полей и доменов
     */
    public Conf getSystemConf() {
        return systemConf;
    }

    /**
     * Базовая конфигурация для поля
     */
    public Conf getFieldBaseConf() {
        return fieldBaseConf;
    }

    /**
     * Базовая конфигурация для домена
     */
    public Conf getDomainBaseConf() {
        return domainBaseConf;
    }

    /**
     * Раскрыть конфигурацию домена
     *
     * @param domainConf конфигурация домена не раскрытая. Не изменяется.
     * @param domainName какое имя домена будет использоватся в возвращаемой
     *                   конфигурации.
     * @return раскрытая конфигурация домена
     */
    public Conf expandDomainConf(Conf domainConf, String domainName) {
        if (UtString.empty(domainName)) {
            domainName = domainConf.getName();
        }
        Conf tmp = Conf.create(domainName);
        tmp.join(domainConf);

        // обработка такая же, как и для доменов ис структуры
        new DomainConfPrepare().prepareDomain(root, tmp);
        //

        return getExpander().expand("domain", tmp);
    }

}
