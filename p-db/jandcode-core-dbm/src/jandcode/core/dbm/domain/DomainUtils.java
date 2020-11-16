package jandcode.core.dbm.domain;

import jandcode.commons.*;
import jandcode.commons.conf.*;

import java.util.*;

/**
 * Утилитки для доменов
 */
public class DomainUtils {

    /**
     * Записать домен в conf.
     *
     * @param dest   куда записать
     * @param domain домен
     * @param self   true - нераскрытая conf
     */
    public static void saveDomainToRt(Conf dest, Domain domain, boolean self) {

        if (self) {
            dest.join(domain.getConf());
        } else {
            dest.join(domain.getConf());
        }

        Conf flds = dest.findConf("field", true);
        flds.clear();

        for (Field f : domain.getFields()) {
            Conf frt = flds.findConf(f.getName(), true);

            if (self) {
                frt.join(f.getConf());
            } else {
                frt.join(f.getConf());
            }

        }

    }

    /**
     * Записать домен в conf.
     *
     * @param domain домен
     * @param self   true - нераскрытая conf
     * @return conf с инфой о домене
     */
    public static Conf saveDomainToRt(Domain domain, boolean self) {
        Conf r = UtConf.create();
        saveDomainToRt(r, domain, self);
        return r;
    }

    /**
     * Записать все домены в conf
     *
     * @param domains список доменов
     * @param self    true - нераскрытая conf
     * @return conf с инфой о доменах
     */
    public static Conf saveDomainsToRt(List<Domain> domains, boolean self) {
        Conf conf = UtConf.create("domain");
        for (Domain d : domains) {
            Conf x = conf.findConf(d.getName(), true);
            saveDomainToRt(x, d, self);
        }
        return conf;
    }

}