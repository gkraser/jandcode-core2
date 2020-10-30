const gulp = require('gulp')
const babel = require('gulp-babel')
const rename = require('gulp-rename')
const through2 = require('through2').obj;
const gulpif = require('gulp-if');
const cached = require('gulp-cached')
const debug = require('gulp-debug');
const findRequires = require('find-requires')
const Vinyl = require('vinyl');
const jsaJs = require("./jsa-js");


function js_taskFactory(g, taskName, module, taskParams) {
    let globs = g.makeGlobs(module, taskParams)

    if (module.isSource) {
        g.addWatchTask(taskName, globs)
    }

    gulp.task(taskName, function() {
        let lastRun = gulp.lastRun(taskName)

        return gulp.src(globs, {base: module.srcPath})
            .pipe(g.showWatchError()) // error handler
            .pipe(cached('js'))
            .pipe(debug({title: 'compile', showFiles: !!lastRun}))
            .pipe(babel(g.babelConfig))
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

    /**
     * Конфигурация babel-env
     */
    g.babelConfigEnv = {
        targets: {
            "chrome": "67",
            "firefox": "60"
        },
        modules: "commonjs"
    }

    /**
     * Конфигурация babel
     */
    g.babelConfig = {
        presets: [
            [
                '@babel/env',
                g.babelConfigEnv
            ],
            [
                '@vue/babel-preset-jsx',
            ]
        ],
        plugins: [
            "@babel/plugin-external-helpers"
        ]
    }

    g.terserConfig = {
        compress: false,
        mangle: false,
        output:{
            comments: false
        },
    }

    //
    g.registerTaskFactory("js", js_taskFactory)

}
