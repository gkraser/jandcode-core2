package jandcode.core.dbm.test;

import jandcode.commons.test.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.impl.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;
import jandcode.core.test.*;

/**
 * Расширение для тестов: поддержка dbm
 */
public class DbmTestSvc extends BaseTestSvc {

    private String modelName;
    private Db db;
    private Mdb mdb;

    protected UtilsTestSvc utils;

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

    /**
     * Ссылка на приложение
     */
    public App getApp() {
        return testSvc(AppTestSvc.class).getApp();
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
            this.db = new ModelDbWrapper(dbOrig, true, false);
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
}
