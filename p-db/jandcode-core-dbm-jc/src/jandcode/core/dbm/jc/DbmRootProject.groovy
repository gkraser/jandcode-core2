package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.core.*
import jandcode.core.dbm.*
import jandcode.core.dbm.dbstruct.DomainDbUtils
import jandcode.core.jc.*
import jandcode.jc.*

/**
 * Для проектов с поддержкой dbm
 */
class DbmRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        onEvent(AppProject.Event_SaveAppConf, this.&saveAppConfHandler)

        cm.add("db-doc", "Генерация документации к базе данных", this.&cmDbDoc)
    }

    void saveAppConfHandler(AppProject.Event_SaveAppConf e) {
        String outDir = e.outDir

        App app = include(AppProject).app
        BeanDef bd = app.getBeanFactory().findBean("jandcode.core.dbm.ModelService")
        if (bd == null) {
            // моделей нет
            return
        }

        ModelService svcModel = app.bean(ModelService)

        for (ModelDef md in svcModel.getModels()) {
            String fn

            fn = "${outDir}/dbm-model--${md.getName()}.cfx"
            log "save file [${fn}]"
            UtConf.save(md.getJoinConf()).toFile(fn)
        }
    }

    void cmDbDoc(CmArgs args) {
        App app = include(AppProject).app
        Model model = app.bean(ModelService).getModel()

        def script = "dbm/db-doc/db-doc.gsp"
        def outDir = wd("temp/db-doc")

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
