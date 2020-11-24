const gulp = require('gulp')
const babel = require('gulp-babel')
const rename = require('gulp-rename')
const through2 = require('through2').obj;
const gulpif = require('gulp-if');
const cached = require('gulp-cached')
const debug = require('gulp-debug');
const findRequires = require('find-requires')
const Vinyl = require('vinyl');

const less = require('gulp-less')

const jsaSupport = require('./jsa-support');
const jsaLess = require('./jsa-less')
const jsaCss = require("./jsa-css");


function less_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobsAllModules(module, taskParams, true)
    g.addWatchTask(taskName, globs)

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return g.makeSrcAllModules(module, taskParams, {since: lastRun})
            .pipe(g.showWatchError()) // error handler
            .pipe(debug({title: 'compile', showFiles: !!lastRun}))
            // сохраняем оригинальное расширение
            .pipe(through2(function(file, enc, callback) {
                file._orig_extname = file.extname
                callback(null, file)
            }))
            .pipe(less({
                plugins: [new jsaLess.JsaLessPlugin({
                    taskName: taskName, g: g, module: module
                })]
            }))
            .pipe(rename(function(path, f) {
                path.extname = f._orig_extname + "--compiled";
            }))
            .pipe(gulpif(g.isProd, through2(function(file, enc, callback) {
                jsaCss.minifyCss(g, file, this)
                callback(null, file)
            })))
            .pipe(gulp.dest(g.buildPathCompiled))
    })

}

module.exports = function(g) {
    g.registerTaskFactory("less", less_taskFactory)
}
