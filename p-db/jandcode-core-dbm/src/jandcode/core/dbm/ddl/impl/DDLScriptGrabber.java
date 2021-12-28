package jandcode.core.dbm.ddl.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

import java.util.*;

public class DDLScriptGrabber {

    public void grabScript(DDLScript script, Conf fromConf) {
        Model model = script.getModel();

        // собираем провайдеров
        List<IDDLProvider> providers = new ArrayList<>();
        for (Conf conf : fromConf.getConfs("ddl")) {
            IDDLProvider p = model.create(conf, DefaultDDLProvider.class);
            providers.add(p);
        }

        // идем по стадиям и собираем операторы
        List<DDLStage> stages = DDLStage.getStages();
        for (DDLStage stage : stages) {
            for (IDDLProvider p : providers) {
                try {
                    List<DDLOper> ops = p.loadOpers(stage);
                    if (ops != null && ops.size() > 0) {
                        script.getItems().addAll(ops);
                    }
                } catch (Exception e) {
                    String mark = p.getClass().getName();
                    if (p instanceof IConfLink) {
                        mark = ((IConfLink) p).getConf().origin().toString();
                    } else if (p instanceof INamed) {
                        mark = ((INamed) p).getName();
                    }
                    throw new XErrorMark(e, mark);
                }
            }
        }

    }

}
