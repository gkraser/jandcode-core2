package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.jc.*
import jandcode.jc.std.*

class JsaJavaProject extends ProjectScript {

    /**
     * Зависимости для node. Формат как в package.json dependencies 
     */
    Map<String, String> nodeDepends = new LinkedHashMap<>()

    /**
     * Определение задач для gulp
     */
    Map<String, Object> gulpTasks = new LinkedHashMap<>()


    protected void onInclude() throws Exception {

        // для JavaProject
        afterLoad {
            JavaProject jm = getIncluded(JavaProject)
            if (jm == null) {
                throw new XError("${JavaProject.class.name} должен быть включен в проект для ${this.class.name}")
            }

            // дополнительная обработка после компиляции
            onEvent(JavaProject.Event_AfterCompile, this.&afterCompile)
        }

    }

    void afterCompile() {
        // информация о проекте jsa помещается в jar
        JavaProject jm = getIncluded(JavaProject)

        // правим манифест по умолчанию
        ant.manifest(file: jm.fileManifest, mode: "update") {
            //
            attribute(name: JsaConsts.MANIFEST_JSA_PROJECT, value: true)

            //
            Map data = [:]
            data.nodeDepends = nodeDepends
            data.gulpTasks = gulpTasks
            attribute(name: JsaConsts.MANIFEST_JSA_DATA, value: JsaUtJson.toJson(data))
        }
    }

    /**
     * Определение задачи gulp для модуля.
     * Параметры зависят от фабрики, которая будет создавть задачу.
     *
     * @param data.name имя задачи
     * @param data.factory фабрика создания задачи (см jsa-gulp.js)
     * @param data.provide при значении true задача наследуется всеми модулями,
     *        которые зависят от этого модуля
     * @param data.globs список масок включаемых файлов (относительно корня модуля)
     *        и исключаемых (должны начинатся с '!')
     */
    void gulpTask(Map data) {
        if (UtString.empty(data.name)) {
            throw new XError("Параметр name не указан")
        }
        if (UtString.empty(data.factory)) {
            data.factory = data.name
        }
        if (data.globs == null) {
            data.globs = []
        }
        if (!(data.globs instanceof List)) {
            throw new XError("globs должен быть списком")
        }
        this.gulpTasks[data.name] = data
    }

}
