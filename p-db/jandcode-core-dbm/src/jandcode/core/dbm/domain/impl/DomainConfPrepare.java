package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;

import java.util.*;

/**
 * Подготовка собранной конфигурации к раскрытию и использованию.
 */
public class DomainConfPrepare {

    /**
     * Подготовить конфигурацию.
     *
     * @param root конфигурация, собранная из модели
     */
    public void prepareRoot(Conf root) {

        // бины базовых объектов
        prepareBaseBeans(root);

        // domain
        for (Conf x : root.getConfs("domain")) {
            prepareDomain(root, x);
        }

        // base domain
        // удаляем все поля из базового домена. На всякий случай.
        Conf d = root.findConf("domain/base");
        d.remove("field");
    }

    /**
     * Подготовить конфигурацию домена
     */
    public void prepareDomain(Conf root, Conf domain) {
        prepareDomain_include(root, domain);

        // если есть узлы ref, для каждого ставим ref=имя_домена
        if (domain.findConf("ref") != null) {
            for (Conf ref : domain.getConfs("ref")) {
                ref.setValue("ref", domain.getName());
            }
        }

        // ставим ref=имя_домена по имени домена для ref по умолчанию, даже если ее нет
        Conf ref = domain.findConf("ref/default", true);
        ref.setValue("ref", domain.getName());

        // tag.db
        if (UtConf.isTagged(domain, "tag.db")) {
            if (UtString.empty(domain.getString("dbtablename"))) {
                domain.setValue("dbtablename", domain.getName());
            }
        }

        // tag.dbview
        if (UtConf.isTagged(domain, "tag.dbview")) {
            if (UtString.empty(domain.getString("dbtablename"))) {
                domain.setValue("dbtablename", domain.getName());
            }
        }

    }

    //////

    private void prepareBaseBeans(Conf root) {
        // забираем все бины доменов и полей базовых в отдельный узел,
        // что бы лишнего не наследовалось

        Conf bean, dest, base;

        // поля
        base = root.findConf("field/base", true);
        bean = base.findConf("bean", true);
        dest = root.findConf("system/field-base/bean", true);
        dest.join(bean);
        base.remove("bean");

        // домены
        base = root.findConf("domain/base", true);
        bean = base.findConf("bean", true);
        dest = root.findConf("system/domain-base/bean", true);
        dest.join(bean);
        base.remove("bean");

    }

    private void prepareDomain_include(Conf root, Conf domain) {

        // включаемые домены, аналог mixin
        Collection<Conf> lst = domain.getConfs("include");
        if (lst.size() > 0) {
            // есть include, сохраняем оригинал
            Conf tmp = Conf.create();
            tmp.join(domain);

            // чистим, добиваясь эффекта, что include срабатывает до свойст домена
            domain.clear();

            // накладываем
            for (Conf inc : lst) {
                Conf domainInc = root.findConf("domain/" + inc.getName());
                if (domainInc == null) {
                    throw new XError("Не найден домен {0} в include для {1}", inc.getName(), domain.getName());
                }
                domain.join(domainInc);
            }

            // в конце накладываем то что было
            domain.join(tmp);

            // удаляем не нужное
            domain.remove("include");
        }

    }

}
