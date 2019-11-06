/* Поддержка jsa
----------------------------------------------------------------------------- */

const jsaModules = require('jsa-modules')
const globby = require('globby')

let modules = []
let modulesByName = {}
let modulesReverse = []
let rootProjectName = ''
let resolvePaths = []

/**
 * Информация о jsa-модуле
 */
class JsaModuleInfo {

    constructor(config) {
        this._config = config
    }

    /**
     * Имя модуля (имя java-package)
     */
    get name() {
        return this._config.name
    }

    /**
     * Пакет модуля. Например 'jandcode/core/jsa/tools'
     */
    get pakPath() {
        return this._config.pakPath
    }

    /**
     * Поть до каталога src модуля
     */
    get srcPath() {
        return this._config.srcPath
    }

    /**
     * Путь до корня модуля (srcPath+pakPath)
     */
    get modulePath() {
        return this.srcPath + "/" + this.pakPath
    }

    /**
     * Задачи компиляции модуля
     */
    get gulpTasks() {
        return this._config.gulpTasks;
    }

}

/**
 * Распознать путь с алиасом
 * @param path путь. Распознается:
 *  ~PATH -> PATH
 *  ~MODULENAME/PATH -> MODULEPATH/PATH
 *  MODULENAME -> MODULEPATH
 */
function resolveAlias(path) {
    if (!path) {
        return ""
    }
    if (path.startsWith('~')) {
        path = path.substring(1)
    }
    path = path.replace(/\\/g, '/')

    let a = path.indexOf('/')
    if (a === -1) {
        let m = modulesByName[path]
        if (m) {
            path = m.modulePath
        }
    } else {
        let p = path.substring(0, a)
        let m = modulesByName[p]
        if (m) {
            path = m.modulePath + path.substring(a)
        }
    }
    return path;
}

/**
 * Раскрывает путь с '*' и возвращает массив путей
 * @param pt Распознается:
 *   [*]/MASK - во всех модулях, MASK относительно каталога модуля
 *   [*]/MASK|reverse - во всех модулях, MASK относительно каталога модуля, реверсивный
 *   MASK     - относительно basedir
 * @param basedir относительно какого каталога искать
 * @param absolute true - выдавать абсолютные пути
 * @return {Array}
 */
function expandPath(pt, basedir, absolute) {
    let res = []
    if (!pt) {
        return res
    }
    if (pt.startsWith("[*]/")) {
        pt = pt.substring(4)
        let modList = modulesReverse

        // реверсивный список: базовые в конце
        let ppos = pt.lastIndexOf('|')
        if (ppos !== -1) {
            let param = pt.substring(ppos + 1)
            pt = pt.substring(0, ppos)
            if (param === 'reverse') {
                modList = modules
            }
        }

        for (let m of modList) {
            let fnd = globby.sync(pt, {cwd: m.modulePath, absolute: absolute})
            for (let fnd1 of fnd) {
                if (absolute) {
                    res.push(fnd1)
                } else {
                    res.push(m.pakPath + '/' + fnd1)
                }
            }
        }
    } else if (basedir) {
        res = globby.sync(pt, {cwd: basedir, absolute: absolute})

    } else {
        throw new Error("Not assigned basedir")
    }
    return res
}

function init() {
    rootProjectName = jsaModules.rootProjectName || 'noname'
    for (let mcfg of jsaModules.modules) {
        let m = new JsaModuleInfo(mcfg)
        modules.push(m)
        modulesByName[m.name] = m
    }
    modulesReverse = modules.slice()
    modulesReverse.reverse()
    resolvePaths = jsaModules.resolvePaths
}

init();

module.exports = {
    modules,
    modulesReverse,
    modulesByName,
    resolveAlias,
    expandPath,
    rootProjectName,
    resolvePaths,
}
