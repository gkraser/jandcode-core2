package jandcode.core.dbm.genid.std;

import jandcode.commons.conf.*;
import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.ddl.impl.*;
import jandcode.core.dbm.genid.*;

import java.util.*;

/**
 * Перенаправляет сбор в конфигурации задействованных драйверов genid
 */
public class GenIdDDLProvider extends BaseDDLProvider {

    private List<IDDLProvider> ddlProviders;

    /**
     * ddl провайдеры, которые используются для создания необходимых
     * структур в базе данных.
     */
    protected List<IDDLProvider> getDDLProviders() {
        if (ddlProviders == null) {
            synchronized (this) {
                if (ddlProviders == null) {
                    ddlProviders = grabDDLProviders();
                }
            }
        }
        return ddlProviders;
    }

    protected List<IDDLProvider> grabDDLProviders() {
        List<IDDLProvider> tmp = new ArrayList<>();

        GenIdService genIdSvc = getModel().bean(GenIdService.class);
        for (GenIdDriver drv : genIdSvc.getDrivers()) {
            for (Conf x : drv.getConf().getConfs("ddl")) {
                IDDLProvider p = getModel().create(x, DefaultDDLProvider.class);
                if (p instanceof BaseDDLProvider p1) {
                    p1.setContext(drv);
                }
                tmp.add(p);
            }
        }
        return tmp;
    }

    protected void onLoad(List<DDLOper> res, DDLStage stage) throws Exception {
        for (IDDLProvider prv : getDDLProviders()) {
            List<DDLOper> tmp = prv.loadOpers(stage);
            if (tmp != null) {
                res.addAll(tmp);
            }
        }
    }

}
