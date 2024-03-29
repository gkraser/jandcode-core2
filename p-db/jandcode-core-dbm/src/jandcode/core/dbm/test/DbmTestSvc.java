package jandcode.core.dbm.test;

import jandcode.commons.*;
import jandcode.commons.test.*;
import jandcode.core.dao.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.fixture.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.std.*;
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

    /**
     * Пересоздать базу данных
     */
    public void recreateDb() throws Exception {
        System.out.println("RECREATE DATABASE:");
        showDb();
        getDb().getDbSource().disconnectAll();
        DbManagerService dbManSvc = getModel().getDbSource().bean(DbManagerService.class);
        if (dbManSvc.existDatabase()) {
            System.out.println("DROP DATABASE");
            dbManSvc.dropDatabase();
        }
        System.out.println("CREATE DATABASE");
        dbManSvc.createDatabase();
    }

    /**
     * Удалить базу данных
     */
    public void dropDb() throws Exception {
        getDb().getDbSource().disconnectAll();
        DbManagerService dbManSvc = getModel().getDbSource().bean(DbManagerService.class);
        if (dbManSvc.existDatabase()) {
            System.out.println("DROP DATABASE");
            showDb();
            dbManSvc.dropDatabase();
        }
    }

    /**
     * Возвращает собранный по модели create.sql
     */
    public String grabCreateSql() {
        CliDbTools dbTools = new CliDbTools(getApp(), getModel());
        return dbTools.grabCreateSql();
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
            rec.setValue("dict", UtString.empty(f.getDict()) ? "" : f.getDict());
        }

        return st;
    }

    /**
     * Показать структуру store
     */
    public void showStoreStruct(Store st) {
        utils.outTable(createStoreStruct(st));
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

    /**
     * Выполнить dao
     *
     * @param holderName имя хранилища dao, например 'api'
     * @param methodName ммя метода, например 'my/dao/method1'
     * @param args       аргументы метода
     * @return результат выполнения dao
     */
    public Object invokeDao(String holderName, String methodName, Object... args) throws Exception {
        DaoService svc = getApp().bean(DaoService.class);
        DaoHolder holder = svc.getDaoHolder(holderName);
        return holder.invokeDao(methodName, args);
    }

    /**
     * Получение {@link DaoHolderItem}
     *
     * @param holderName имя хранилища dao, например 'api'
     * @param methodName ммя метода, например 'my/dao/method1'
     * @return null, если не найдено
     */
    public DaoHolderItem findDaoHolderItem(String holderName, String methodName) {
        DaoService svc = getApp().bean(DaoService.class);
        DaoHolder holder = svc.getDaoHolder(holderName);
        return holder.getItems().find(methodName);
    }

    ////// database

    /**
     * Удалить таблицу, если она существует.
     * Ошибки игнорируются.
     */
    public void dropDbTable(String tableName) {
        try {
            getDb().execQuery("drop table " + tableName);
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
        getDb().execQueryNative(ddl);
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

    ////// fixtures

    /**
     * Записать fixture в базу, старые данные не удалять
     */
    public void saveFixture(Fixture fx) throws Exception {
        new FixtureMdbUtils(getMdb()).saveFixture(fx, true);
    }

    /**
     * Удалить данные fixture
     */
    public void cleanFixture(Fixture fx) throws Exception {
        new FixtureMdbUtils(getMdb()).cleanFixture(fx, true);
    }

    /**
     * Удалить данные fixture, а потом записать новые данные
     */
    public void updateFixture(Fixture fx) throws Exception {
        new FixtureMdbUtils(getMdb()).updateFixture(fx, true);
    }

    /**
     * Записать fixture в базу
     *
     * @param doClean true - сначала удаить старые данные
     */
    public void saveFixtureSuite(String fixtureSuiteName, boolean doClean) throws Exception {
        getMdb().connect();
        try {
            System.out.println("fixture-suite: " + fixtureSuiteName);
            FixtureService svc = getApp().bean(FixtureService.class);
            FixtureSuite suite = svc.createFixtureSuite(fixtureSuiteName);
            List<FixtureBuilder> bs = suite.createBuilders();
            for (FixtureBuilder b : bs) {
                System.out.println("fixture-builder: " + b.getClass().getName());
                Fixture fx = b.build(getModel());
                if (doClean) {
                    cleanFixture(fx);
                }
                saveFixture(fx);
            }
        } finally {
            getMdb().disconnect();
        }
    }

    /**
     * Записать fixture в базу, старые данные не удалять
     */
    public void saveFixtureSuite(String fixtureSuiteName) throws Exception {
        saveFixtureSuite(fixtureSuiteName, false);
    }

    /**
     * Записать fixture в базу, удалить старые данные
     */
    public void updateFixtureSuite(String fixtureSuiteName) throws Exception {
        saveFixtureSuite(fixtureSuiteName, true);
    }

    /**
     * Восстановить все genId, задействованные в фикстуре, на последние значения в базе
     *
     * @param fx фикстура
     */
    public void recoverGenIds(Fixture fx) {
        new FixtureMdbUtils(getMdb()).recoverGenIds(fx);
    }

    /**
     * Установить все генераторы на последние значения в таблицах фикстур.
     * Метод используется для тестирования вставок записей.
     * <p>
     * Считаем, что для фикстуры задан rangeId. Если есть значения id в этом диапазоне,
     * то выставляем genId на следующее после максимального.
     * Если значений нет, то выставляем на начало rangeId.
     *
     * @param fx фикстура
     */
    public void updateGenIds(Fixture fx) {
        new FixtureMdbUtils(getMdb()).updateGenIds(fx);
    }

}
