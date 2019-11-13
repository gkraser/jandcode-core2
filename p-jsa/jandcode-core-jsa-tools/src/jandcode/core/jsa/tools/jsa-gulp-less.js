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


function less_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(module, taskParams)

    // вынужденная мера: отслеживаем все less во всех модулях
    if (module.isSource) {
        for (let m of jsaSupport.modules) {
            let gm = g.makeGlobs(m, {globs: ['**/*.less']})
            g.addWatchTask(taskName, gm)
        }
    }

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: module.srcPath})
            .pipe(g.showWatchError()) // error handler
            .pipe(debug({title: 'compile', showFiles: !!lastRun}))
            // сохраняем оригинальное расширение
            .pipe(through2(function(file, enc, callback) {
                file._orig_extname = file.extname
                callback(null, file)
            }))
            .pipe(less({
                plugins: [new jsaLess.JsaLessPlugin()]
            }))
            .pipe(rename(function(path, f) {
                path.extname = f._orig_extname + "--compiled";
            }))
            .pipe(gulp.dest(g.buildPathCompiled))
    })

}

module.exports = function(g) {
    g.registerTaskFactory("less", less_taskFactory)
}
