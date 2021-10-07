package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.core.*
import jandcode.core.dbm.*
import jandcode.core.jc.*
import jandcode.jc.*

/**
 * Для проектов с поддержкой dbm
 */
class DbmRootProject extends ProjectScript {

    protected void onInclude() throws Exception {
        onEvent(AppProject.Event_SaveAppConf, this.&saveAppConfHandler)
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
}
