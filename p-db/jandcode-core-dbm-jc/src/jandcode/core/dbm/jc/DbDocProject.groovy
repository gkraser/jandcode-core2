package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.core.*
import jandcode.core.dbm.*
import jandcode.core.dbm.dbstruct.*
import jandcode.core.jc.*
import jandcode.jc.*

class DbDocProject extends ProjectScript {

    /**
     * Какой скрипт использовать для генерации
     */
    String srcScript = "dbm/db-doc/db-doc.gsp"

    /**
     * Куда выводить по умолчанию
     */
    String outDir = "temp/db-doc"

    protected void onInclude() throws Exception {
        cm.add("db-doc", "Генерация документации к базе данных", this.&cmDbDoc,
                cm.opt("m", "default", "Для какой модели, по умолчанию - default"),
                cm.opt("o", "temp/db-doc", "В какой каталог вывдить, по умолчанию temp/db-doc")
        )
    }

    void cmDbDoc(CmArgs args) {

        String modelName = args.getString("m", "default")
        String outDir = args.getString("o", this.outDir).trim()
        if (UtString.empty(outDir)) {
            throw new XError("outDir не указан")
        }
        outDir = wd(outDir)

        App app = include(AppProject).app
        Model model = app.bean(ModelService).getModel(modelName)

        def script = srcScript

        ut.cleandir(outDir)
        GspScript gs = create(script)

        def dbUtils = new DomainDbUtils(model)

        log "Generating db-doc..."
        ut.stopwatch.start()
        gs.generate("${outDir}/out.txt", [
                dbUtils: dbUtils,
        ])
        log "db-doc generated to: ${outDir}"
        ut.stopwatch.stop()
    }

}
