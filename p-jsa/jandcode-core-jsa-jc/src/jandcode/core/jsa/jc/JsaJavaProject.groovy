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
     * Определение зависимостей для node.
     * Формат как в package.json dependencies.
     * Ключ - имя библиотеки, значение - версия.
     */
    void nodeDepends(Map deps) {
        if (deps == null) {
            return
        }
        this.nodeDepends.putAll(deps)
    }

    /**
     * Определение файлов из node_modules, которые будут доступны для клиентского
     * приложения. Формат:
     *
     * Ключ - имя каталога в node_modules.
     * Может быть пустой строкой, тогда маски задаются относительно каталога
     * node_modules.
     *
     * Значение - массив масок относительно этого каталога.
     * Если маска начинается с '!', то это exclude.
     */
    void nodeDependsClient(Map data) {
        if (data == null || data.size() == 0) {
            return
        }
        //
        String taskName = 'nm'
        Map gt = gulpTasks[taskName]
        if (!gt) {
            gulpTask(name: taskName, stage: 'prepare')
            gt = gulpTasks[taskName]
        }
        List globs = gt.globs
        //
        for (item in data) {
            String dir = item.key
            if (!(item.value instanceof List)) {
                throw new XError("Список масок для ключа ${dir} должен быть списком")
            }
            List masks = item.value
            if (dir != "" && !dir.endsWith("/")) {
                dir = dir + "/"
            }
            if (dir != "") {
                globs.add(dir + JsaConsts.PACKAGE_JSON)
            }
            for (String m in masks) {
                if (m.startsWith("!")) {
                    globs.add("!" + dir + m.substring(1))
                } else {
                    globs.add(dir + m)
                }
            }
        }
    }

    /**
     * mapping модулей node.
     * ключ - имя модуля.
     * знакчение - путь до модуля, как его будут видеть остальные модули.
     */
    void nodeModuleMapping(Map data) {
        if (data == null || data.size() == 0) {
            return
        }
        //
        String taskName = 'nm-module-mapping'
        Map gt = gulpTasks[taskName]
        if (!gt) {
            gulpTask(name: taskName, stage: 'afterBuild', mapping: [:])
            gt = gulpTasks[taskName]
        }
        for (item in data) {
            String lib = item.key
            String mapLib = item.value
            gt.mapping[lib] = mapLib
        }
    }

    /**
     * Список масок файлов в клиентских node_modules, для которых нужно
     * extract-require. По умолчанию - все попавшие *.js.
     * Поэтому в этом методе указываем маски, которые не нуждаются в обработке
     * в виде масок исключений '!mask'.
     */
    void nodeExtractRequire(Object... data) {
        if (data == null || data.size() == 0) {
            return
        }
        //
        String taskName = 'nm-extract-require-globs'
        Map gt = gulpTasks[taskName]
        if (!gt) {
            gulpTask(name: taskName)
            gt = gulpTasks[taskName]
        }
        List globs = gt.globs
        for (item in data) {
            globs.add(item)
        }
    }

}
