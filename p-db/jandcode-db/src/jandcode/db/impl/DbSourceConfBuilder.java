package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.db.*;

/**
 * Построитель конфигурации DbSource
 */
public class DbSourceConfBuilder {

    /**
     * Построить полный вариант конфигурации DbSource
     *
     * @param params произвольные параметры, обычно эти параметры берутся из ini
     */
    public Conf buildConf(App app, Conf params) throws Exception {

        // будущая конфигурация DbSource
        Conf dbSourceConf = UtConf.create();

        if (params.getBoolean("$full")) {
            // передан вариант, который уже обработан
            dbSourceConf.join(params);
            return dbSourceConf;
        }

        // конфигурация по умолчанию
        Conf defaultConfig = app.getConf().getConf("db/dbsource-default");

        // собираем предварительный вариант
        dbSourceConf.join(defaultConfig);
        dbSourceConf.join(params);

        // получаем dbdriver
        String dbDriverName = dbSourceConf.getString("dbdriver", DbConsts.DBDRIVER_DEFAULT);
        if (UtString.empty(dbDriverName)) {
            throw new XError("Не указан параметр dbdriver");
        }
        DbDriver dbDriver = app.bean(DbDriverService.class).getDbDriver(dbDriverName);

        // драйвер может предоставить конфиг по умолчанию для dbsource
        Conf dbDriverConf = dbDriver.getConf().findConf("dbsource-default");
        if (dbDriverConf != null) {
            Conf tmp = UtConf.create();
            tmp.join(defaultConfig);
            tmp.join(dbDriverConf);
            tmp.join(params);
            dbSourceConf = tmp;
        }

        // в конфигурации есть бины, собираем из них конфигурацию
        Conf beanConf = UtConf.create();
        for (Conf bc : dbSourceConf.getConfs("bean")) {
            Conf dcDs = bc.findConf("dbsource-default");
            if (dcDs != null) {
                beanConf.join(dcDs);
            }
        }
        dbSourceConf.join(beanConf);
        dbSourceConf.setValue("dbdriver", dbDriver.getName());

        // метим, что обработали
        dbSourceConf.setValue("$full", true);

        //
        return dbSourceConf;
    }

}
