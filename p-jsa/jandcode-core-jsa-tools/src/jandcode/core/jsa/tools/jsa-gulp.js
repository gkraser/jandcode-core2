/* Подержка gulp для jsa
----------------------------------------------------------------------------- */

const jsaSupport = require('./jsa-support')
const fs = require('fs');
const gulp = require('gulp')
const path = require('path')
const del = require('del');

const gulpif = require('gulp-if');
const notify = require('gulp-notify')
const plumber = require('gulp-plumber');

const JSA_GULP_ADDON = "jsa-gulp-addon.js"

/**
 * api для taskFactory
 * @param g ссылка на JsaGulpBuilder
 * @param taskName имя задачи
 * @param module модуль
 * @param taskParams параметры задачи
 */
function API_TaskFactory(g, taskName, module, taskParams) {}


/**
 * Построитель gulp
 */
class JsaGulpBuilder {

    constructor() {

        // production режим
        this.isProd = process.env.NODE_ENV === 'production' || process.env.NODE_ENV === 'prod'

        // фабрики задач
        this.taskFactorys = {}

        // задачи по этапам
        this.buildTasksByStage = {
            prepare: [],
            build: [],
            afterBuild: [],
        }

        // куда собирать
        this.buildPath = path.resolve('_gen/jsa-webroot')

        // куда собирать скомпиленное
        this.buildPathCompiled = this.buildPath + '/_jsa/_compiled'

        // куда собирать скомпиленное node_modules
        this.buildPathCompiledNodeModules = this.buildPath + '/_jsa/_compiled/_jsa/_node_modules'

        // куда собирать скомпиленное
        this.buildPathNodeModules = this.buildPath + '/_jsa/_node_modules'

        // каталог с node_modules
        this.nodeModulesPath = path.resolve('node_modules')

        // индиктор того, что работает команда watch
        this.watchWorking = false

        // задачи watch. Каждый элемент: {glob:[glob], task:taskName}.
        // т.е. при изменениях в glob выполнить taskName
        this.watchTasks = []

        // экземпляры ватчеров, ключ - имя задачи, значение - экземпляр
        this.watchInsts = {}
    }

    /**
     * Инициализация gulp-среды
     */
    init(fn) {
        let th = this

        if (this.isProd) {
            console.info("gulp PRODUCTION build");
        }

        // clean
        gulp.task("clean:build", function(cb) {
            del.sync(th.buildPath);
            fs.mkdirSync(th.buildPath, {recursive: true})
            cb()
        })
        this.addBuildTask('clean:build', 'prepare')

        this._initAddons()
        this._initModuleTasks()

        function watchMark(cb) {
            th.watchWorking = true
            cb()
        }

        function watchTasks(cb) {
            process.title = 'gulp watch: ' + jsaSupport.rootProjectName
            for (let t of th.watchTasks) {
                let w = gulp.watch(t.globs, {usePolling: true}, gulp.series(t.task))
                th.watchInsts[t.task] = w
                // вызываем отложенные функции, заказанные в execForWatcher
                for (let wCb of t.initCb) {
                    wCb(w)
                }
            }
            cb()
        }

        if (fn) {
            fn(this)
        }

        let buildTasks = [].concat(
            this.buildTasksByStage.prepare,
            this.buildTasksByStage.build,
            this.buildTasksByStage.afterBuild
        )

        gulp.task("build", gulp.series(...buildTasks))

        gulp.task("watch", gulp.series('build', watchMark, watchTasks))

    }

    _initAddons() {
        for (let m of jsaSupport.modules) {
            let p1 = m.modulePath + "/" + JSA_GULP_ADDON
            if (fs.existsSync(p1)) {
                let p2 = require(p1)
                p2(this)
            }
        }
    }

    _initModuleTasks() {
        for (let module of jsaSupport.modulesReverse) {
            for (let tn in module.gulpTasks) {
                let taskParams = module.gulpTasks[tn];
                let gulpTaskName = taskParams.name + ":" + module.name;
                let taskFactory = this.taskFactorys[taskParams.factory]
                if (!taskFactory) {
                    throw new Error("task factory [" + taskParams.factory + "] not found (module: " + module.name + ")");
                }
                taskFactory(this, gulpTaskName, module, taskParams)
                this.addBuildTask(gulpTaskName, taskParams.stage)
            }
        }
    }

    //////

    registerTaskFactory(name, fn) {
        this.taskFactorys[name] = fn
    }

    addBuildTask(taskName, stage) {
        if (gulp.task(taskName)) {
            if (!stage) {
                stage = 'build'
            }
            let lst = this.buildTasksByStage[stage]
            if (lst.indexOf(taskName) === -1) {
                lst.push(taskName)
            }
        }
    }

    addWatchTask(taskName, globs) {
        if (!globs || globs.length === 0) {
            return  // нечего отслеживать
        }
        // initCb - набор функций, которые будут вызваны при создании watcher
        let w = {task: taskName, globs: globs, initCb: []}
        this.watchTasks.push(w)
    }

    /**
     * Выполнить функцию cb для watcher указанной задачи.
     * Если watcher еще не создан, то выполнение задачи откладывается на момент
     * создания watcher
     *
     * @param taskName для какой задачи
     * @param cb вызываемая функция, в качестве параметра получает watcher
     */
    execForWatcher(taskName, cb) {
        let watcher = this.watchInsts[taskName]
        if (watcher) {
            cb(watcher)
        } else {
            for (let wt of this.watchTasks) {
                if (wt.task === taskName) {
                    wt.initCb.push(cb)
                    return
                }
            }
            throw new Error("Not found task " + taskName + " for execForWatcher")
        }
    }

    /**
     * Сделать globs из параметры задачи
     * @param module или модуль, или корневой каталог для globs
     * @param taskParams параметры задачи
     */
    makeGlobs(module, taskParams) {
        let path = module
        if (!path) {
            throw new Error('path not defined in makeGlobs')
        }
        if (typeof path !== 'string') {
            path = path.modulePath
        }
        let globs = []
        if (taskParams.globs) {
            for (let mask of taskParams.globs) {
                if (mask.startsWith("!")) {
                    globs.push("!" + path + "/" + mask.substring(1))
                } else {
                    globs.push(path + "/" + mask)
                }
            }
        }
        return globs
    }

    /**
     * Сделать globs из параметров задачи для всех модулей
     * @param baseModule начиная с какого модуля и всех зависящих от него.
     *                   Если null - то для всех.
     * @param taskParams параметры задачи
     * @param sourcesOnly true - только для модулей в исходниках
     */
    makeGlobsAllModules(baseModule, taskParams, sourcesOnly) {
        let res = []
        let need = baseModule == null
        for (let module of jsaSupport.modulesReverse) {
            if (!need && module === baseModule) {
                need = true
            }
            if (sourcesOnly && !module.isSource) {
                continue
            }
            if (need) {
                let globs = this.makeGlobs(module, taskParams)
                res = res.concat(globs)
            }
        }
        return res
    }

    /**
     * Сделать gulp.src для всех модулей.
     * @param taskParams параметры задачи
     * @param baseModule начиная с какого модуля и всех зависящих от него.
     *                   Если null - то для всех.
     * @param srcParams объект с дополнительными параметрами для каждого src.
     *                  Если не нужен - может быть null
     * @return gulp.pipe
     */
    makeSrcAllModules(baseModule, taskParams, srcParams) {
        let res
        let need = baseModule == null
        for (let module of jsaSupport.modulesReverse) {
            if (!need && module === baseModule) {
                need = true
            }
            if (need) {
                let globs = this.makeGlobs(module, taskParams)
                let sp = {base: module.srcPath}
                if (srcParams) {
                    Object.assign(sp, srcParams)
                }
                if (!res) {
                    res = gulp.src(globs, sp)
                } else {
                    res = res.pipe(gulp.src(globs, sp))
                }
            }
        }
        if (!res) {
            throw new Error("no src in makeSrcAllModules")
        }
        return res
    }

    /**
     * Показ ошибки при watch.
     * использование:
     * .pipe(g.showWatchError())
     */
    showWatchError() {
        let th = this
        return gulpif(th.watchWorking, plumber({
            errorHandler: notify.onError(function(err) {
                return {
                    title: 'Error in watch',
                    message: err.message
                }
            })
        }))
    }
}

//////

module.exports = {
    JsaGulpBuilder
}

