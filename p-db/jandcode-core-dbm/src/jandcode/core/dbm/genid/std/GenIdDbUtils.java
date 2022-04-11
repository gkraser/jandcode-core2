package jandcode.core.dbm.genid.std;

import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;

import java.util.*;

/**
 * Уилитя для генерации ddl
 */
public class GenIdDbUtils implements IModelLink {

    private Model model;

    public GenIdDbUtils(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    /**
     * Список генераторов для указанного драйвера
     */
    public List<GenId> getGenIds(GenIdDriver driver) {
        NamedList<GenId> res = new DefaultNamedList<>();
        GenIdService svc = getModel().bean(GenIdService.class);
        for (GenId genId : svc.getGenIds()) {
            if (genId.getDriver() == driver) {
                res.add(genId);
            }
        }
        res.sort();
        return res;
    }
}
