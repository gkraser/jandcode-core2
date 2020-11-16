package jandcode.core.dbm.domain.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.dbm.domain.*;

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
    public void prepare(Conf root) {

        // бины базовых объектов
        prepareBaseBeans(root);

        // include
        for (Conf x : root.getConfs("domain")) {
            prepare_include(root, x);
        }

        // system.fields
        prepare_systemFields(root);  //todo под вопросом - нужно ли???

        // domain
        for (Conf x : root.getConfs("domain")) {
            prepareDomain(root, x);
        }

        // base domain
        // удаляем все поля из базового домена. На всякий случай.
        Conf d = root.findConf("domain/base");
        d.remove("field");
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

    /**
     * Создает домен system.fields со всеми полями верхнего уровня (string, int ...)
     */
    @Deprecated
    private void prepare_systemFields(Conf root) {
        Conf systemDomain = root.findConf("domain/" + DomainConsts.DOMAIN_SYSTEM_FIELDS, true);
        systemDomain.clear();
        Conf systemFields = systemDomain.findConf("field", true);
        for (Conf f : root.getConfs("field")) {
            if ("base".equals(f.getName())) {
                continue;
            }
            Conf ff = systemFields.findConf(f.getName(), true);
            ff.setValue("parent", f.getName());
        }
    }

    private void prepare_include(Conf root, Conf domain) {

        // включаемые домены, аналог mixin
        Collection<Conf> lst = domain.getConfs("include");
        if (lst.size() > 0) {
            // есть include, сохраняем оригинал
            Conf tmp = UtConf.create();
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

    private void prepareDomain(Conf root, Conf domain) {

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
            domain.setValue("dbtablename", domain.getName());
        }

        // tag.dbview
        if (UtConf.isTagged(domain, "tag.dbview")) {
            domain.setValue("dbtablename", domain.getName());
        }

    }

}
