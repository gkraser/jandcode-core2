package jandcode.core.dbm.domain.impl;

import jandcode.commons.collect.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

/**
 * Формирование списка доменов, который находится в порядке, соответствующем
 * ссылкам из полей.
 * Первыми идут домены, на которые ссылаются, потом те, которые ссылаются.
 * Кольца - игнорируются.
 */
public class DomainRefReorder {

    protected HashSet<String> used = new HashSetNoCase();
    private NamedList<Domain> domains;

    /**
     * Обработать список доменов и создать новый список в нужном порядке.
     */
    public NamedList<Domain> reorder(NamedList<Domain> lst) {
        domains = lst;
        NamedList<Domain> res = new DefaultNamedList<>();
        for (Domain d : lst) {
            add(res, d);
        }
        return res;
    }

    protected void add(List<Domain> res, Domain domain) {
        if (used.contains(domain.getName())) {
            return;
        }
        used.add(domain.getName());

        for (Field f : domain.getFields()) {
            if (f.hasRef()) {
                Domain refDomain = resolveDomain(f.getRef());
                if (refDomain != null) {
                    add(res, refDomain);
                }
            }
        }

        res.add(domain);

    }

    protected Domain resolveDomain(String name) {
        return domains.find(name);
    }

}
