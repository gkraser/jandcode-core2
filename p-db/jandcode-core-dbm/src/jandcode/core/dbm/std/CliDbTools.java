package jandcode.core.dbm.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.mdb.*;
import org.slf4j.*;

/**
 * Набор методов для использоания в инструментах командной строки,
 * связанных с базой данной.
 */
public class CliDbTools implements IAppLink, IModelLink {

    protected static Logger log = UtLog.getLogConsole();

    private App app;
    private Model model;

    public CliDbTools(App app, String modelName) {
        this.app = app;
        this.model = getModel(app, modelName);
    }

    public CliDbTools(App app, Model model) {
        this.app = app;
        this.model = model;
    }

    public App getApp() {
        return app;
    }

    public Model getModel() {
        return model;
    }

    public DbSource getDbSource() {
        return getModel().getDbSource();
    }

    public DbManagerService getDbMan() {
        return getDbSource().bean(DbManagerService.class);
    }

    //////

    /**
     * Получить модель по имени со всеми проверками
     */
    public static Model getModel(App app, String name) {
        ModelService modelSvc = app.bean(ModelService.class);
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

    /**
     * Показать инфу о базе данных
     */
    public void showInfo() {
        System.out.println("Модель: " + getModel().getName());
        UtOutTable.outTable(getDbSource().getProps());
    }

    /**
     * Сгенерировать create.sql
     */
    public String grabCreateSql() {
        DDLService svc = getModel().bean(DDLService.class);
        DDLScript script = svc.grabScript();
        return script.getSqlScript();
    }

    /**
     * Получить список экземпляров моделей, у которых есть база данных
     */
    public NamedList<Model> getModelsWithDb() {
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

    /**
     * Проверка соединения с базой данных
     */
    public void checkConnect() throws Exception {
        log.info("Check connect...");
        Mdb mdb = getModel().createMdb(true);
        mdb.connect();
        mdb.disconnect();
        log.info("ОК");
    }

}
