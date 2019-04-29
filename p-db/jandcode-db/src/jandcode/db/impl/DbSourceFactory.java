package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.db.*;

/**
 * Фабрика DbSource
 */
public class DbSourceFactory {

    /**
     * Создание DbSource
     *
     * @param params произвольные параметры, обычно эти параметры берутся из ini
     */
    public DbSource createDbSource(App app, Conf params) throws Exception {

        // будущая конфигурация DbSource
        Conf dbSourceConf = UtConf.create();

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
        DbDriver dbDriver = app.bean(DbDriverService.class).getDbDrivers()
                .get(dbDriverName).createInst();

        // драйвер может предоставить конфиг по умолчанию для dbsource
        Conf dbDriverConf = dbDriver.getConf().findConf("dbsource-default");
        if (dbDriverConf != null) {
            Conf tmp = UtConf.create();
            tmp.join(dbDriverConf);
            tmp.join(dbSourceConf);
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

        // создаем
        DbSourceImpl res = app.create(dbSourceConf, DbSourceImpl.class, (inst) -> {
            ((DbSourceImpl) inst).setDbDriver(dbDriver);
        });

        //
        return res;
    }

}
