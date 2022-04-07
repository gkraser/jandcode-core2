package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.core.*
import jandcode.core.dbm.std.*
import jandcode.core.dbm.verdb.*
import jandcode.core.jc.*
import jandcode.jc.*

/**
 * Поддержка verdb для проекта
 */
class VerdbProject extends ProjectScript {

    protected void onInclude() throws Exception {
        List commonOpt = [
                cm.opt("m", "default", "Для какого экземпляра модели, по умолчанию - default"),
                cm.opt("vm", "default", "Для какого модуля verdb, по умолчанию - default"),
        ]
        cm.add("verdb-newver", "Создание новой версии", this.&cmVerdbNewver,
                commonOpt,
                cm.opt("u", false, "Обновить create.sql в последней незакомиченной версии"),
        )

    }

    void cmVerdbNewver(CmArgs args) {
        String modelName = args.getString("m", "default")
        String verdbModuleName = args.getString("vm", "default")
        boolean update = args.containsKey("u")
        //
        log "model:        ${modelName}"
        log "verdb module: ${verdbModuleName}"
        //
        App app = include(AppProject).app
        def dbTools = new CliDbTools(app, modelName)
        //
        VerdbService verdbSvc = dbTools.model.bean(VerdbService)
        VerdbModule verdbModule = verdbSvc.getVerdbModules().get(verdbModuleName).createInst()
        def lastVersion = verdbModule.getLastVersion()
        //
        if (!update) {
            String nextVerDirName = UtString.padLeft(UtString.toString(lastVersion.v1 + 1), 4, "0")
            String nextVerPath = UtFile.join(verdbModule.path, nextVerDirName)
            nextVerPath = UtFile.vfsPathToLocalPath(nextVerPath)
            UtFile.mkdirs(nextVerPath)
            String createSqlPath = UtFile.join(nextVerPath, VerdbConsts.CREATE_SQL_PATH)
            String createSql = dbTools.grabCreateSql()
            UtFile.cleanFile(createSqlPath)
            UtFile.saveString(createSql, new File(createSqlPath))
            log "create version dir: ${nextVerPath}"
        } else {

        }
    }

}
