const gulp = require('gulp')
const babelCore = require("@babel/core")
const through2 = require('through2').obj;
const rename = require('gulp-rename')
const gulpif = require('gulp-if');
const jsaJs = require("./jsa-js");

function babelHelpers_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(module, taskParams)
    let txt = babelCore.buildExternalHelpers()

    gulp.task(taskName, function() {
        return gulp.src(globs, {base: module.srcPath})
            .pipe(through2(function(file, enc, callback) {
                file.contents = Buffer.from(txt)
                callback(null, file)
            }))
            .pipe(rename(function(path) {
                path.extname += "--compiled";
            }))
            .pipe(gulpif(g.isProd, through2(async function(file, enc, callback) {
                if (file.path.endsWith('--compiled')) {
                    await jsaJs.minifyJs(g, file, this)
                }
                callback(null, file)
            })))
            .pipe(gulp.dest(g.buildPathCompiled))
    })
}

///
module.exports = function(g) {
    g.registerTaskFactory("babelHelpers", babelHelpers_taskFactory)
}
