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
                gulp.watch(t.globs, {usePolling: true}, gulp.series(t.task))
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
        let w = {task: taskName, globs: globs}
        this.watchTasks.push(w)
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
     * @param taskParams параметры задачи
     */
    makeGlobsAllModules(taskParams) {
        let res = []
        for (let module of jsaSupport.modules) {
            let globs = this.makeGlobs(module, taskParams)
            res = res.concat(globs)
        }
        return res
    }

    /**
     * Сделать gulp.src для всех модулей.
     * @param taskParams параметры задачи
     * @return gulp.pipe
     */
    makeSrcAllModules(taskParams) {
        let res
        for (let module of jsaSupport.modules) {
            let globs = this.makeGlobs(module, taskParams)
            if (!res) {
                res = gulp.src(globs, {base: module.srcPath})
            } else {
                res = res.pipe(gulp.src(globs, {base: module.srcPath}))
            }
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

