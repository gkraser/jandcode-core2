package jandcode.core.dbm.test;

import jandcode.commons.*;
import jandcode.commons.test.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;
import jandcode.core.test.*;

import java.util.*;

/**
 * Расширение для тестов: поддержка dbm.
 * <p>
 * Используются следующие настройки из конфигурации приложения:
 * <ul>
 *     <li>test/dbm/model-default - имя модели, которая будет использоватся в тестах.
 *     По умолчанию - 'default'.</li>
 *     <li>test/dbm/create-db - создавать ли базу данных, если она не существует.
 *     По умолчанию - false.</li>
 * </ul>
 */
public class DbmTestSvc extends BaseAppTestSvc {

    private String modelName;
    private Db db;
    private Mdb mdb;

    public void setUp() throws Exception {
        super.setUp();
        this.utils = testSvc(UtilsTestSvc.class);

        // берем имя модели из test.cfx или default
        this.modelName = getApp().getConf().getString("test/dbm/model-default", "default");

    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (db != null) {
            if (db.isConnected()) {
                if (db.isTran()) {
                    try {
                        db.rollback();
                    } catch (Exception e) {
                        utils.showError(e);
                    }
                }
                try {
                    db.disconnectForce();
                } catch (Exception e) {
                    utils.showError(e);
                }
            }
        }
    }

    ////// model

    /**
     * Сервис модели
     */
    public ModelService getModelService() {
        return getApp().bean(ModelService.class);
    }

    /**
     * Имя модели, которая будет использоватся в тестах как модель по умолчанию.
     * Изначально установлено в 'default'.
     * Если есть настройка: "test/dbm/model-default",
     * то имя модели берется оттуда.
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Установить имя модели по умолчанию для тестов.
     * Если это реально нужно, то вызывать в setUp
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * Модель по умолчанию для этого объекта.
     * Имя модели в {@link DbmTestSvc#getModelName()}.
     */
    public Model getModel() {
        return getModelService().getModel(getModelName());
    }


    ////// db

    /**
     * База данных. Соединение будет установлено автоматически
     * при вызовах методов доступа к базе.
     * disconnect будет сделан автоматически при выходе из теста.
     * Транзакция не стартует после соединения.
     */
    public Db getDb() {
        if (this.db == null) {
            Db dbOrig = getModel().createDb();
            this.db = new TestDbWrapper(dbOrig, this);
        }
        return this.db;
    }

    /**
     * Экземпляр {@link Mdb} для базы {@link DbmTestSvc#getDb()}.
     */
    public Mdb getMdb() {
        if (this.mdb == null) {
            this.mdb = getModel().createMdb(getDb());
        }
        return this.mdb;
    }

    /**
     * Показать настройки db
     */
    public void showDb() {
        utils.outMap(getModel().getDbSource().getProps());
    }

    ////// store assert

    /**
     * Перевод store в строку для дальнейшего сравнения в assert
     */
    public String toAssertString(Store store) {
        StringBuilder sb = new StringBuilder();
        for (StoreRecord rec : store) {
            if (sb.length() != 0) {
                sb.append("\n");
            }
            sb.append(rec.getValues().toString());
        }
        return sb.toString();
    }

    /**
     * Перевод store в строку для дальнейшего сравнения в assert
     */
    public String toAssertString(StoreRecord rec) {
        return rec.getValues().toString();
    }

    ////// store

    /**
     * Создать store со структурой указанного store
     *
     * @param store для какого store создать структуру
     * @return store со структурой
     */
    public Store createStoreStruct(Store store) {
        Store st = getMdb().createStore();
        st.addField("name", "string");
        st.addField("type", "string");
        st.addField("size", "int");
        st.addField("dict", "string");

        for (StoreField f : store.getFields()) {
            StoreRecord rec = st.add();
            rec.setValue("name", f.getName());
            rec.setValue("type", f.getStoreDataType().getName());
            rec.setValue("size", f.getSize());
            rec.setValue("dict", f.getDict());
        }

        return st;
    }

    ////// dao

    /**
     * Создать экземпляр dao.
     *
     * @param cls            для какого класса
     * @param daoInvokerName какой daoInvoker использовать.
     *                       Если не указано, то используется invoker текущей модели,
     *                       полученной из {@link DbmTestSvc#getModelName()}.
     *                       Можно например указать модельный 'model:default'
     */
    public <A> A createDao(Class<A> cls, String daoInvokerName) throws Exception {
        if (UtString.empty(daoInvokerName)) {
            daoInvokerName = "model:" + getModelName();
        }
        DaoService svc = getApp().bean(DaoService.class);
        DaoInvoker di = svc.getDaoInvoker(daoInvokerName);
        return di.createDao(cls);
    }

    /**
     * Создать экземпляр dao для daoInvoker текущей модели
     *
     * @param cls для какого класса
     */
    public <A> A createDao(Class<A> cls) throws Exception {
        return createDao(cls, null);
    }

    ////// database

    /**
     * Удалить таблицу, если она существует.
     * Ошибки игнорируются.
     */
    public void dropDbTable(String tableName) {
        try {
            db.execQuery("drop table " + tableName);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Создать таблицу в базе со структурой и данными как у store.
     * Без индексов.
     * Если таблица уже существует, она удаляется.
     *
     * @param tableName имя таблицы
     * @param store     структура таблицы
     */
    public void createDbTable(String tableName, Store store) throws Exception {
        dropDbTable(tableName);

        DbDriver drv = getDb().getDbSource().getDbDriver();
        StringBuilder sbCreate = new StringBuilder();
        for (StoreField f : store.getFields()) {
            DbDataType dbt = drv.getDbDataTypes().get(f.getStoreDataType().getName());
            String s1 = dbt.getSqlType(f.getSize());
            if (sbCreate.length() != 0) {
                sbCreate.append(",\n");
            }
            sbCreate.append("  ").append(f.getName()).append(" ").append(s1);
        }
        String ddl = "create table " + tableName + "(\n" + sbCreate + "\n)";
        db.execQueryNative(ddl);
        //
        insertDbTable(tableName, store);
    }

    protected String generateInsertSql(String tableName, Store store) {
        StringBuilder sb = new StringBuilder();

        List<String> fields = new ArrayList<>();
        List<String> params = new ArrayList<>();

        for (StoreField f : store.getFields()) {
            fields.add(f.getName());
            params.add(":" + f.getName());
        }

        sb.append("insert into ");
        sb.append(tableName);
        sb.append("(");
        sb.append(UtString.join(fields, ","));
        sb.append(") values (");
        sb.append(UtString.join(params, ","));
        sb.append(")");

        return sb.toString();
    }

    /**
     * Вставить данные из store в таблицу в базе данных.
     *
     * @param tableName в какую таблицу
     * @param store     данные
     */
    public void insertDbTable(String tableName, Store store) throws Exception {
        String sql = generateInsertSql(tableName, store);
        DbQuery q = getDb().createQuery(sql);
        getDb().startTran();
        try {
            for (StoreRecord rec : store) {
                q.setParams(rec);
                q.exec();
            }
            getDb().commit();
        } catch (Exception e) {
            getDb().rollback(e);
        }
    }

}
