package jandcode.core.dbm.jc

import jandcode.commons.*
import jandcode.commons.named.*
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
        onEvent(AppProject.Event_ShowInfo, this.&showInfoHandler)
        include(DbDocProject)
        include(DbCmdProject)
        include(VerdbProject)
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

    void showInfoHandler(AppProject.Event_ShowInfo e) {
        App app = e.app

        BeanDef bd = app.getBeanFactory().findBean("jandcode.core.dbm.ModelService")
        if (bd == null) {
            // моделей нет
            return
        }
        ModelService svcModel = app.bean(ModelService)

        Map m
        //
        println ut.makeDelim("models")
        m = [:]

        NamedList<ModelDef> models = new DefaultNamedList<>()
        models.addAll(svcModel.getModels())
        models.sort()

        for (ModelDef md in models) {
            String name = md.name

            Map value = [:]

            //
            if (md.isInstance()) {
                value['instance'] = md.instanceOf.name
            } else {
                value['instance'] = false
            }

            //
            def origin = md.conf.origin().toString()
            if (!UtString.empty(origin)) {
                origin = origin.split("\\n").toList()
            }
            value['files'] = origin

            //
            m[name] = value
        }
        ut.printMap(m)
    }


}
