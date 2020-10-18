/*

    Поддержка less

 */

const jsaSupport = require('./jsa-support');
const path = require('path');
const gulp = require('gulp');
const fs = require('fs');

let tmpFileCounter = 0

/**
 * Поддержка import '~xxx'
 */
let JsaLessFileManager = function() {
}

let less = require('less')
let LessFileManager = less.FileManager

JsaLessFileManager.prototype = new LessFileManager();
JsaLessFileManager.prototype.loadFile = function(filename, currentDirectory, options, environment, callback) {

    //
    filename = jsaSupport.resolveAlias(filename)

    //
    if (filename.indexOf('*') !== -1) {
        let files = jsaSupport.expandPath(filename, currentDirectory, false)
        let fstr = ''
        for (let f of files) {
            fstr += '@import "' + f + '";\n';
        }
        let res = {}
        res.contents = fstr
        tmpFileCounter++
        res.filename = path.resolve(currentDirectory, "TMP-IMPORT--" + tmpFileCounter + ".less")
        callback(null, res)
        return
    }

    //
    return LessFileManager.prototype.loadFile.call(this, filename, currentDirectory, options, environment, callback)
}

////// plugin

function JsaLessPlugin(options) {
    this.options = options || {};
}

/**
 * Включение watch за импортируемыми файлами для перекомпиляции основного.
 * Требуются опции:
 * taskName: для какой задачи
 * g: ссылка на JsaGulpBuilder
 * @param options
 * @constructor
 */
let JsaLessPostProcessor = function(options) {
    this.options = options;
}
JsaLessPostProcessor.prototype = {
    process: function(src, extra) {
        let g = this.options.g
        let taskName = this.options.taskName

        if (!g || !taskName) {
            // ignore
            return src
        }

        let rootFilename = extra.imports.rootFilename
        let imports = extra.imports.files

        //
        if (g.__less_used == null) {
            g.__less_used = {}
        }
        if (g.__less_used[taskName] == null) {
            g.__less_used[taskName] = {}
        }
        let used = g.__less_used[taskName]
        //

        let globs = []
        for (let fi of imports) {
            if (used[fi]) {
                continue
            }
            used[fi] = true
            globs.push(fi)
        }
        if (globs.length > 0) {
            // есть новые импорты
            g.execForWatcher(taskName, function(watcher) {
                gulp.watch(globs, {usePolling: true}, function watchImportedLess(cb) {
                    let now = new Date()
                    fs.utimesSync(rootFilename, now, now)
                    cb()
                })
            })
        }

        return src
    }
}


JsaLessPlugin.prototype = {
    install: function(less, pluginManager) {
        pluginManager.addFileManager(new JsaLessFileManager(this.options));
        pluginManager.addPostProcessor(new JsaLessPostProcessor(this.options), 1)
    },
}

module.exports = {
    JsaLessPlugin,
}

