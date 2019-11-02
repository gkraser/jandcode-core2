const gulp = require('gulp')
const babel = require('gulp-babel')
const rename = require('gulp-rename')
const through2 = require('through2').obj;
const gulpif = require('gulp-if');
const cached = require('gulp-cached')
const debug = require('gulp-debug');
const findRequires = require('find-requires')
const Vinyl = require('vinyl');

const sass = require('gulp-sass')
sass.compiler = require('node-sass')

const jsaSupport = require('./jsa-support');

function sass_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(module, taskParams)

    // вынужденная мера: отслеживаем все scss/sass во всех модулях
    for (let m of jsaSupport.modules) {
        let gm = g.makeGlobs(m, {globs: ['**/*.scss', '**/*.sass']})
        g.addWatchTask(taskName, gm)
    }

    //
    function sass_imp(url, prev, done) {
        url = jsaSupport.resolveAlias(url)
        done({file: url})
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
            .pipe(sass({
                outputStyle: 'expanded',
                includePaths: jsaSupport.resolvePaths,
                importer: sass_imp
            }))
            .pipe(rename(function(path, f) {
                path.extname = f._orig_extname + "--compiled";
            }))
            .pipe(gulp.dest(g.buildPathCompiled))
    })

}

module.exports = function(g) {
    g.registerTaskFactory("sass", sass_taskFactory)
}
