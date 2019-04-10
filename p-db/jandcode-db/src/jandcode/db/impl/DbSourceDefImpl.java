package jandcode.db.impl;

import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.*;
import jandcode.commons.conf.*;

public class DbSourceDefImpl extends BaseComp implements DbSourceDef {

    protected DbSource inst;
    protected Conf conf;

    public DbSourceDefImpl(App app, String name, Conf conf) {
        setApp(app);
        setName(name);
        this.conf = conf;
    }

    public Conf getConf() {
        return conf;
    }

    public DbSource createInst() {
        return getApp().create(getJoinConf(), DbSourceImpl.class, inst -> {
            if (inst instanceof DbSourceImpl) {
                ((DbSourceImpl) inst).setDbSourceDef(this);
            }
        });
    }

    protected Conf getJoinConf() {
        // сначала берем cfg настройку

        Conf tmp = UtConf.create();
        tmp.join(this.conf);

        // метим dbsource
        tmp.setValue("dbsourceName", getName());

        // накладываем конфигурацию из cfg, если имеется
        // обрабатываем подстановки ${PROP}, где PROP - свойства dbsource, известные
        // с самого начала работы, т.е. переданные через тег, через который создается
        String cfgPath = tmp.getString("cfg");
        if (!UtString.empty(cfgPath)) {
            String cfgPathEx = UtString.substVar(cfgPath, v -> {
                return tmp.getString(v);
            });
            // сохраняем раскрытый
            tmp.setValue("cfg", cfgPathEx);
            //
            Conf cfgX = getApp().getConf().findConf(cfgPathEx);
            if (cfgX != null) {
                tmp.join(cfgX);
            }
        }

        // теперь уже должен быть драйвер, ибо настройку загрузили

        Conf x = UtConf.create(getName());

        // dbsource/default из драйвера
        DbService dbSvc = getApp().bean(DbService.class);
        String driverName = tmp.getString("dbdriver", DbConsts.DBDRIVER_DEFAULT);
        DbDriverDef drv = dbSvc.getDbDrivers().get(driverName);
        Conf dbsProto = drv.getConf().findConf("dbsource/default");
        if (dbsProto != null) {
            x.join(dbsProto);
        }

        // потом себя
        x.join(tmp);

        // готово
        return x;
    }

    public DbSource getInst() {
        if (inst == null) {
            synchronized (this) {
                if (inst == null) {
                    inst = createInst();
                }
            }
        }
        return inst;
    }

}
