const gulp = require('gulp')
const babel = require('gulp-babel')
const rename = require('gulp-rename')
const through2 = require('through2').obj;
const gulpif = require('gulp-if');
const cached = require('gulp-cached')
const debug = require('gulp-debug');
const findRequires = require('find-requires')
const Vinyl = require('vinyl');

const fs = require('fs')
const fse = require('fs-extra')
const path = require('path')

const lessCompiler = require('less')

const jsaSupport = require('./jsa-support');
const jsaVue = require('./jsa-vue');
const jsaLess = require('./jsa-less')

function vue_taskFactory(g, taskName, module, taskParams) {
    let th = this

    let globs = g.makeGlobs(module, taskParams)

    g.addWatchTask(taskName, globs)

    function vue(file, enc, callback) {

        function less_compiler(text) {
            let errRes
            let outRes
            lessCompiler.render(text,
                {
                    filename: file.path,
                    syncImport: true,
                    sync: true,
                    plugins: [new jsaLess.JsaLessPlugin()]

                }
                , function(err, out) {
                    errRes = err
                    outRes = out
                }
            )
            if (errRes) {
                throw errRes
            }
            return outRes.css.toString();
        }

        try {
            let text = file.contents.toString()
            let jsText = jsaVue.compileVue(text, g.babelConfig, {
                less: less_compiler,
            })
            file.contents = Buffer.from(jsText)
        } catch(e) {
            console.info("ERROR in file:", file.path);
            callback(e)
            return
        }
        callback(null, file);
    }

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: module.srcPath})
            .pipe(g.showWatchError()) // error handler
            .pipe(cached('vue'))
            .pipe(debug({title: 'compile', showFiles: !!lastRun}))
            .pipe(through2(vue))
            .pipe(rename(function(path) {
                path.extname += "--compiled";
            }))
            // extract requires
            .pipe(through2(function(file, enc, callback) {
                let src = file.contents.toString()
                let r = findRequires(src)
                var rfile = new Vinyl({
                    cwd: file.cwd,
                    base: file.base,
                    path: file.path + '-req',
                    contents: Buffer.from(JSON.stringify(r))
                });
                this.push(rfile)
                callback(null, file)
            }))
            .pipe(gulp.dest(g.buildPathCompiled))
    })
}

module.exports = function(g) {
    g.registerTaskFactory("vue", vue_taskFactory)
}