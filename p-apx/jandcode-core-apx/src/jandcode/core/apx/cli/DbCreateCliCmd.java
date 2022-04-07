package jandcode.core.apx.cli;

import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.core.cli.*;
import jandcode.core.db.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.std.*;

/**
 * Команда cli: создание баз данных по текущему состоянию модели
 */
public class DbCreateCliCmd extends BaseAppCliCmd {

    public void exec() throws Exception {
        CliDbTools dbTools = new CliDbTools(getApp(), getModelName());
        dbTools.showInfo();

        DbManagerService dbMan = dbTools.getDbMan();

        if (dbMan.existDatabase()) {
            if (isDropExist()) {
                System.out.println("drop exist database");
                dbMan.dropDatabase();
            } else {
                throw new XError("База данных уже существует");
            }
        }

        System.out.println("create empty database");
        dbMan.createDatabase();

        System.out.println("exec create.sql");
        String createSql = dbTools.grabCreateSql();
        Mdb mdb = dbTools.getModel().createMdb(true);
        mdb.connect();
        try {
            mdb.execScript(createSql);
        } finally {
            mdb.disconnect();
        }
        System.out.println("ok");

    }

    public void cliConfigure(CliDef b) {
        b.desc("Создание базы данных по текущему состоянию модели");
        b.opt("modelName")
                .names("-m").arg("MODEL")
                .desc("Для какой модели")
                .defaultValue("default");
        b.opt("dropExist")
                .names("-drop")
                .desc("Удалить существующую базу");
    }

    private String modelName = "default";
    private boolean dropExist = false;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isDropExist() {
        return dropExist;
    }

    public void setDropExist(boolean dropExist) {
        this.dropExist = dropExist;
    }
}
