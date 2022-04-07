package jandcode.core.apx.cli;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.named.*;
import jandcode.core.cli.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.std.*;

/**
 * Команда cli: проверка соединения с базой данных
 */
public class DbCheckCliCmd extends BaseAppCliCmd {

    public void exec() throws Exception {

        if (isShowList()) {
            NamedList<Model> lst = CliDbTools.getModelsWithDb(getApp());
            for (Model m : lst) {
                System.out.println(m.getName());
            }
            return;
        }

        String mn = getModelName();
        if (!UtString.empty(mn)) {
            checkModel(CliDbTools.getModel(getApp(), mn));
        } else {
            NamedList<Model> lst = CliDbTools.getModelsWithDb(getApp());
            for (Model m : lst) {
                checkModel(m);
                if (lst.size() > 1) {
                    System.out.println("\n");
                }
            }
        }

    }

    public void cliConfigure(CliDef b) {
        b.desc("Проверка соединения с базой данных");
        b.opt("showList")
                .names("-l")
                .desc("Показать список моделей с базой данных");
        b.opt("modelName")
                .names("-m").arg("MODEL")
                .desc("Для какой модели")
                .defaultValue("для всех");
    }

    private boolean showList;
    private String modelName;

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    private void checkModel(Model model) {
        CliDbTools dbTools = new CliDbTools(getApp(), model);
        dbTools.showInfo();
        try {
            dbTools.checkConnect();
        } catch (Exception e) {
            String err = UtError.createErrorInfo(e).getText();
            System.out.println("ERROR: " + err);
        }
    }

}
