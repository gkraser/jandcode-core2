package jandcode.core.dbm.verdb;

import jandcode.commons.error.*;
import jandcode.core.db.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.verdb.impl.*;

public class VerdbProcessor {

    private VerdbModule verdbModule;
    private DbSource dbSource;
    private Mdb mdb;

    public VerdbProcessor(VerdbModule verdbModule, DbSource dbSource) {
        this.verdbModule = verdbModule;
        this.dbSource = dbSource;
    }

    public VerdbModule getVerdbModule() {
        return verdbModule;
    }

    public DbSource getDbSource() {
        return dbSource;
    }

    public Mdb getMdb() {
        if (this.mdb == null) {
            this.mdb = createMdb();
        }
        return mdb;
    }

    protected Mdb createMdb() {
        return getVerdbModule().getModel().createMdb(getDbSource().createDb(true));
    }

    /**
     * Инициализировать базу данных и внутренние объекты для работы verdb.
     * При невозможности инициализироать, генерируется ошибка.
     *
     * @param autoCreate true - создавать, если не существует
     */
    public void init(boolean autoCreate) throws Exception {
        DbSource dbs = getDbSource();
        DbManagerService dbMan = dbs.bean(DbManagerService.class);
        DbMetadataService dbMeta = dbs.bean(DbMetadataService.class);

        // проверяем, что база данных существует
        boolean existDb = dbMan.existDatabase();
        if (!existDb) {
            if (autoCreate) {
                dbMan.createDatabase();
            } else {
                throw new XError("База данных не существует");
            }
        }

        if (existDb) {
            // если сами не создали, то проверяем ее
            boolean existVerdb = dbMeta.hasTable(VerdbConsts.TABLE_VERDB_INFO);
            if (!existVerdb) {
                // не существует таблица verdb_info, база должна быть пустой
                if (dbMeta.hasTables()) {
                    throw new XError("База данных не пустая");
                }
            }
        }

        // инициализируем таблицу verdb_info модулем
        Mdb mdb = getMdb();
        mdb.connect();
        try {
            VerdbSystemDb sysDb = new VerdbSystemDb(mdb, getVerdbModule().getModuleName());
            sysDb.install();
        } finally {
            mdb.disconnect();
        }
    }

}
