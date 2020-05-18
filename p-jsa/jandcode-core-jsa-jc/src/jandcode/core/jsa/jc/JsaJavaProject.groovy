package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.jc.*
import jandcode.jc.std.*

class JsaJavaProject extends ProjectScript {

    /**
     * Определение задач для gulp
     */
    Map<String, Object> gulpTasks = new LinkedHashMap<>()

    /**
     * Список библиотек nodejs, от которых зависит модуль.
     */
    List<String> nodeJsDepends = []


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
            data.gulpTasks = gulpTasks
            data.nodeJsDepends = nodeJsDepends
            attribute(name: JsaConsts.MANIFEST_JSA_DATA, value: JsaUtJson.toJson(data))

        }
    }

    /**
     * Определение задачи gulp для модуля.
     * Параметры зависят от фабрики, которая будет создавать задачу.
     *
     * @param data.name имя задачи
     * @param data.factory фабрика создания задачи (см jsa-gulp.js)
     * @param data.stage на каком этапе сборки запускать (prepare, build, afterBuild).
     *        По умолчанию - build
     * @param data.provide при значении true задача наследуется всеми модулями,
     *        которые зависят от этого модуля
     * @param data.globs список масок включаемых файлов (относительно корня модуля)
     *        и исключаемых (должны начинатся с '!')
     */
    Map gulpTask(Map data) {
        if (UtString.empty(data.name)) {
            throw new XError("Параметр name не указан")
        }
        Map t = this.gulpTasks[data.name]
        if (t != null) {
            if (data.size() > 1) {
                throw new XError("Попытка переопределения gulpTask: ${data.name}")
            }
            return t  // уже существует
        }
        if (UtString.empty(data.factory)) {
            data.factory = data.name
        }
        if (UtString.empty(data.stage)) {
            data.stage = "build"
        }
        if (data.globs == null) {
            data.globs = []
        }
        if (!(data.globs instanceof List)) {
            throw new XError("globs должен быть списком")
        }
        this.gulpTasks[data.name] = data
    }

    /**
     * Добавить указанные библиотеки nodejs в зависимости
     * @param libs
     */
    void nodeJsDepends(String... data) {
        if (data == null || data.size() == 0) {
            return
        }
        this.nodeJsDepends.addAll(data)
    }

}
