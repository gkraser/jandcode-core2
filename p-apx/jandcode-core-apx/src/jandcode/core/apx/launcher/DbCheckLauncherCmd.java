package jandcode.core.apx.launcher;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.launcher.*;

/**
 * Команда launcher: проверка соединения с базой данных
 */
public class DbCheckLauncherCmd extends BaseLauncherCmd {

    public void help(HelpBuilder h) throws Exception {
        h.desc("Проверка соединения с базой данных");
        h.opt("m", "Для какой модели", "MODEL", "для всех");
        h.opt("l", "Список моделей с базой данных");
    }

    public void exec() throws Exception {

        if (getArgs().containsKey("l")) {
            NamedList<Model> lst = getModels();
            for (Model m : lst) {
                System.out.println(m.getName());
            }
            return;
        }

        String mn = getArgs().getString("m");
        if (!UtString.empty(mn)) {
            checkModel(getModel(mn));
        } else {
            NamedList<Model> lst = getModels();
            for (Model m : lst) {
                checkModel(m);
                if (lst.size() > 1) {
                    System.out.println("\n");
                }
            }
        }
    }

    private void checkModel(Model model) {
        System.out.println("Модель: " + model.getName());
        UtOutTable.outTable(model.getDbSource().getProps());

        //
        System.out.println("check connect...");
        //
        Mdb mdb = model.createMdb();
        try {
            mdb.connect();
            mdb.disconnect();
            System.out.println("OK");
        } catch (Exception e) {
            String err = UtError.createErrorInfo(e).getText();
            System.out.println("ERROR: " + err);
        }
    }

    private NamedList<Model> getModels() {
        NamedList<Model> res = new DefaultNamedList<>();
        ModelService modelSvc = getApp().bean(ModelService.class);
        for (ModelDef md : modelSvc.getModels()) {
            if (md.isInstance()) {
                if (!md.getInst().getDbSource().getDbType().equals("base")) {
                    // только для моделей с явными базами
                    res.add(md.getInst());
                }
            }
        }
        return res;
    }

    private Model getModel(String name) {
        ModelService modelSvc = getApp().bean(ModelService.class);
        ModelDef md = modelSvc.getModels().find(name);
        if (md == null) {
            throw new XError("Модель не найдена: {0}", name);
        }
        if (!md.isInstance()) {
            throw new XError("Модель не является экземпляром: {0}", name);
        }
        if (md.getInst().getDbSource().getDbType().equals("base")) {
            throw new XError("Модель не имеет явно определенной базы данных: {0}", name);
        }
        return md.getInst();
    }

}
