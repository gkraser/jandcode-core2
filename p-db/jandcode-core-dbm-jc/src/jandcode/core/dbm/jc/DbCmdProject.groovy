package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.core.*
import jandcode.core.dbm.*
import jandcode.core.dbm.fixture.*
import jandcode.core.dbm.std.*
import jandcode.core.jc.*
import jandcode.jc.*

/**
 * Команды для работы с базой данных в проекте
 */
class DbCmdProject extends ProjectScript {

    protected void onInclude() throws Exception {
        List commonOpt = [
                cm.opt("m", "default", "Для какого экземпляра модели, по умолчанию - default"),
        ]
        cm.add("db-create", "Создание базы данных по текущему состоянию модели", this.&cmDbCreate,
                commonOpt,
                cm.opt("e", false, "Не создавать структуру, только пустую базу"),
                cm.opt("g", false, "Сгенерировать скрипт на создание, базу не создавать"),
                cm.opt("s", "", "Имена fixture-suite через запятую, которые нужно загрузить после создания"),
        )
        cm.add("db-info", "Информация о базе данных и проверка соединения", this.&cmDbInfo,
                commonOpt,
                cm.opt("n", false, "Не проверять соединение с базой данных"),
                cm.opt("a", false, "Для всех моделей с базами данных"),
        )
        cm.add("db-loadtestdata", "Загрузить тестовые данные в базу", this.&cmLoadTestData,
                commonOpt,
                cm.opt("s", "", "Имена fixture-suite через запятую, которые нужно загрузить"),
        )
    }

    void cmDbCreate(CmArgs args) {
        String modelName = args.getString("m", "default")
        boolean empty = args.containsKey("e")
        boolean genOnly = args.containsKey("g")
        //
        App app = include(AppProject).app
        def dbTools = new CliDbTools(app, modelName)
        dbTools.showInfo()
        //

        String createSql = dbTools.grabCreateSql()
        String outFile = wd("temp/db-create/${dbTools.model.name}/create.sql")
        ut.cleanfile(outFile)
        UtFile.saveString(createSql, new File(outFile))
        log "save file: ${outFile}"

        if (genOnly) {
            return
        }

        def dbMan = dbTools.dbMan

        if (dbMan.existDatabase()) {
            log "drop exist database"
            dbMan.dropDatabase()
        }

        log "create empty database"
        dbMan.createDatabase()

        if (empty) {
            return
        }

        log "exec create.sql"
        def mdb = dbTools.model.createMdb(true)
        mdb.connect()
        try {
            mdb.execScript(createSql)
        } finally {
            mdb.disconnect()
        }

        // loadtestdata
        if (args.containsKey("s")) {
            String suiteNames = args.getString("s")
            FixtureService fxSvc = app.bean(FixtureService)
            if (UtString.empty(suiteNames)) {
                throw new XError("Не указаны имена fixture-suite в параметре -s, допустимые имена: ${fxSvc.getFixtureSuiteNames()}")
            }
            dbTools.loadTestData(suiteNames)
        }

    }

    void cmDbInfo(CmArgs args) {
        String modelName = args.getString("m", "default")
        boolean noConnect = args.containsKey("n")
        boolean allModels = args.containsKey("a")
        //
        App app = include(AppProject).app
        def dbTools = new CliDbTools(app, modelName)
        //
        List<Model> models = []
        if (allModels) {
            models.addAll(dbTools.getModelsWithDb())
        } else {
            models.add(dbTools.model)
        }
        //
        def errorModels = []
        for (m in models) {
            def dbt = new CliDbTools(app, m)
            dbt.showInfo()
            if (!noConnect) {
                try {
                    dbt.checkConnect()
                } catch (e) {
                    errorModels.add(m.name)
                    String err = UtError.createErrorInfo(e).getText()
                    println("ERROR: " + err)
                }
            }
        }
        if (errorModels) {
            throw new XError("Ошибки в моделях: ${errorModels}")
        }

    }

    void cmLoadTestData(CmArgs args) {
        String modelName = args.getString("m", "default")
        String suiteNames = args.getString("s")
        //
        //
        App app = include(AppProject).app
        def dbTools = new CliDbTools(app, modelName)
        dbTools.showInfo()
        //
        FixtureService fxSvc = app.bean(FixtureService)

        if (UtString.empty(suiteNames)) {
            throw new XError("Не указаны имена fixture-suite в параметре -s, допустимые имена: ${fxSvc.getFixtureSuiteNames()}")
        }

        dbTools.loadTestData(suiteNames)
    }

}
